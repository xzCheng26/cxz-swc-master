package com.cxz.mushu.service.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.view.builder.dsource.DataSourceBuilder;
import com.rsbi.ispire.dc.cube.DataSet;
import com.cxz.mushu.entity.common.DSColumn;
import com.cxz.mushu.entity.model.DataSource;
import com.cxz.mushu.entity.model.Dataset;
import com.cxz.mushu.mapper.model.DatasetMapper;
import com.cxz.mushu.mapper.model.DimensionMapper;
import com.cxz.mushu.service.bireport.BaseCompService;
import com.cxz.mushu.service.bireport.ModelCacheService;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatasetService extends BaseCompService {

	@Autowired
	private DatasetMapper mapper;

	@Autowired
	private DataSourceService dsService;

	@Autowired
	private ModelCacheService cacheService;

	@Autowired
	private DimensionMapper dimMapper;

	private static Logger logger = Logger.getLogger(DatasetService.class);

	public List<Dataset> listDataset(){
		return mapper.listDataset();
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateDset(Dataset ds){
		mapper.updateDset(ds);
		//同步立方体表的字段类型
		JSONObject obj = (JSONObject)JSON.parse(ds.getCfg());
		JSONArray cols = obj.getJSONArray("cols");
		for(int i=0; i<cols.size(); i++){
			JSONObject col = cols.getJSONObject(i);
			String isupdate = (String)col.get("isupdate");
			if("y".equals(isupdate)){
				String type = col.getString("type");
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("vtype", type);
				param.put("tname", col.getString("tname"));
				param.put("col", col.getString("name"));
				param.put("dset", obj.get("dsetId"));
				dimMapper.updateColType(param);
			}
		}

		//删除缓存
		cacheService.removeDset(ds.getDsid());
	}

	/**
	 * 重新加载数据集的字段
	 * @param dsetId
	 * @throws Exception
	 */
	public void reloadDset(String dsetId, String dsid) throws Exception{
		String cfg = mapper.getDatasetCfg(dsetId);
		JSONObject json = JSON.parseObject(cfg);
		JSONArray oldCols = json.getJSONArray("cols");
		List<DSColumn> cols = this.queryMetaAndIncome(json, dsid);
		//添加新的字段到原数据集中
		List<DSColumn> addList = new ArrayList<DSColumn>();
		for(DSColumn col : cols){
			if(!existCol(col.getName(), oldCols)){
				addList.add(col);
			}
		}
		if(addList.size() == 0){
			//删除缓存
			cacheService.removeDset(dsetId);
			return;
		}
		for(DSColumn col : addList){
			oldCols.add(JSON.toJSON(col));
		}
		String newCfg = json.toJSONString();
		Dataset ds = new Dataset();
		ds.setDsetId(dsetId);
		ds.setCfg(newCfg);
		mapper.updateDsetCfg(ds);
		//删除缓存
		cacheService.removeDset(ds.getDsetId());
	}

	private boolean existCol(String colName, JSONArray cols){
		boolean ext = false;
		for(int i=0; i<cols.size(); i++){
			JSONObject col = cols.getJSONObject(i);
			String name = col.getString("name");
			if(name.equals(colName)){
				ext = true;
				break;
			}
		}
		return ext;
	}

	public void insertDset(Dataset ds){
		mapper.insertDset(ds);
	}

	public void deleteDset(String dsetId){
		mapper.deleteDset(dsetId);
		//删除缓存
		cacheService.removeDset(dsetId);
	}

	public String getDatasetCfg(String dsetId){
		return mapper.getDatasetCfg(dsetId);
	}

	public List<DSColumn> listTableColumns(String dsid, String tname) throws Exception{
		DataSource ds = dsService.getDataSource(dsid);
		Connection conn = null;
		try {
			if(ds.getUse().equals("jndi")){
				conn = dsService.getJndi(ds);
			}else if(ds.getUse().equals("jdbc")){
				conn = dsService.getJDBC(ds);
			}
			String sql = "select * from "+ tname;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setMaxRows(1);
			ResultSet rs = ps.executeQuery();
			List<DSColumn> cols = copyValue(rs);
			rs.close();
			ps.close();
			return cols;
		}catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("sql 执行报错.");
		}finally{
			if(conn != null){
				conn.close();
			}
		}
	}

	public List<DSColumn> copyValue(ResultSet rs) throws SQLException{
		ResultSetMetaData meta = rs.getMetaData();
		List<DSColumn> cols = new ArrayList<DSColumn>();
		for(int i=0; i<meta.getColumnCount(); i++){
			String name = meta.getColumnName(i+1);
			if(name.indexOf(".") >= 0){
				name = name.substring(name.indexOf(".") + 1, name.length());
			}
			String tp = meta.getColumnTypeName(i+1);
			//meta.get
			//tp转换
			tp = columnType2java(tp);
			DSColumn col = new DSColumn();
			col.setName(name);
			col.setType(tp);
			col.setIsshow(true);
			col.setIdx(i+1);
			if("Date".equals(tp)){
				//日期不设置长度
			}else{
				col.setLength(meta.getColumnDisplaySize(i + 1));
			}
			cols.add(col);
		}
		return cols;
	}

	/**
	 * java类型到 数据库类型
	 * @param tp
	 * @return
	 */
	public String columnType2java(String tp){
		tp = tp.replaceAll(" UNSIGNED", ""); //mysql 存在 UNSIGNED 类型, 比如： INT UNSIGNED
		String ret = null;
		if("varchar".equalsIgnoreCase(tp) || "bpchar".equalsIgnoreCase(tp) || "varchar2".equalsIgnoreCase(tp) || "nvarchar2".equalsIgnoreCase(tp) || "char".equalsIgnoreCase(tp) || "string".equalsIgnoreCase(tp)
				|| tp.toLowerCase().indexOf("text") >= 0 || tp.toLowerCase().indexOf("string") >= 0 || tp.toLowerCase().indexOf("enum8") >= 0){
			ret = "String";
		}else if("int".equalsIgnoreCase(tp) || "int4".equalsIgnoreCase(tp) || "float4".equalsIgnoreCase(tp) ||  "INTEGER".equalsIgnoreCase(tp) || "MEDIUMINT".equalsIgnoreCase(tp) || "smallint".equalsIgnoreCase(tp) || "TINYINT".equalsIgnoreCase(tp)
				|| "BIT".equalsIgnoreCase(tp) || "UInt32".equalsIgnoreCase(tp) || "UInt8".equalsIgnoreCase(tp)){
			ret = "Int";
		}else if( "int8".equalsIgnoreCase(tp) || "BIGINT".equalsIgnoreCase(tp)){
			ret = "Long";
		}else if("number".equalsIgnoreCase(tp) || "numeric".equalsIgnoreCase(tp) || "DECIMAL".equalsIgnoreCase(tp) || "Float".equalsIgnoreCase(tp) || "Double".equalsIgnoreCase(tp) || "REAL".equalsIgnoreCase(tp) || "dec".equalsIgnoreCase(tp)
				|| "Float32".equalsIgnoreCase(tp)){
			ret = "Double";
		}else if("DATETIME".equalsIgnoreCase(tp) || "Timestamp".equalsIgnoreCase(tp)){
			ret = "Datetime";
		}else if("DATE".equalsIgnoreCase(tp)){
			ret = "Date";
		}
		if(ret == null){
			System.out.println("tp = " + tp+" 字段类型未映射成功");
		}
		return ret;
	}

	/**
	 * 查询数据集的字段
	 * @param dataset
	 * @param dsid
	 * @return
	 * @throws Exception
	 */
	public List<DSColumn> queryMetaAndIncome(JSONObject dataset, String dsid) throws Exception{
		DataSource ds = this.dsService.getDataSource(dsid);
		List<String> tables = new ArrayList<String>();
		//需要进行关联的表
		JSONArray joinTabs = (JSONArray)dataset.get("joininfo");
		//生成sql
		StringBuffer sb = new StringBuffer("select a0.* ");
		//添加 列的分隔符，方便识别列是从哪个表来
		if(joinTabs!=null&&joinTabs.size() != 0){ //无关联表，不需要该字段
			sb.append(",'' a$idx ");
		}

		List<String> tabs = new ArrayList<String>(); //需要进行关联的表，从joininfo中获取，剔除重复的表
		for(int i=0; joinTabs!=null&&i<joinTabs.size(); i++){
			JSONObject join = joinTabs.getJSONObject(i);
			String ref = join.getString("ref");
			if(!tabs.contains(ref)){
				tabs.add(ref);
			}
		}

		for(int i=0; i<tabs.size(); i++){
			sb.append(", a"+(i+1)+".* ");
			if(i != tabs.size() - 1){
				//添加 列的分隔符，方便识别列是从哪个表来
				sb.append(",'' a$idx");
			}
		}
		sb.append("from ");
		String master = dataset.getString("master");
		sb.append( master + " a0 ");
		tables.add(dataset.getString("master"));
		for(int i=0; i<tabs.size(); i++){
			String tab = tabs.get(i);
			sb.append(", " +tab);
			sb.append(" a"+(i+1)+" ");
			tables.add(tab);
		}
		sb.append("where 1=2 ");
		for(int i=0; i<tabs.size(); i++){
			String tab = tabs.get(i);
			List<JSONObject> refs = getJoinInfoByTname(tab, joinTabs);
			for(int k=0; k<refs.size(); k++){
				JSONObject t = refs.get(k);
				sb.append("and a0."+t.getString("col")+"=a"+(i+1)+"."+t.getString("refKey"));
				sb.append(" ");
			}
		}

		Connection conn  = null;
		try {
			if(ds.getUse().equals("jndi")){
				conn = this.dsService.getJndi(ds);
			}else if(ds.getUse().equals("jdbc")){
				conn = this.dsService.getJDBC(ds);
			}
			PreparedStatement ps = conn.prepareStatement(sb.toString());
			ps.setMaxRows(1);
			ResultSet rs = ps.executeQuery();

			ResultSetMetaData meta = rs.getMetaData();
			List<DSColumn> cols = new ArrayList<DSColumn>();
			String tname = tables.get(0);
			int idx = 1;
			for(int i=0; i<meta.getColumnCount(); i++){
				String name = meta.getColumnName(i+1);
				if(name.indexOf(".") >= 0){
					name = name.substring(name.indexOf(".") + 1, name.length());
				}
				String tp = meta.getColumnTypeName(i+1);
				//遇到a$idx 表示字段做分割, 需要变换字段所属表信息
				if("a$idx".equalsIgnoreCase(name)){
					tname = tables.get(idx);
					idx++;
					continue;
				}
				tp = columnType2java(tp);
				DSColumn col = new DSColumn();
				col.setIdx(idx);
				col.setDispName("");
				col.setExpression("");
				col.setName(name);
				col.setType(tp);
				col.setTname(tname);
				col.setIsshow(true);
				cols.add(col);
			}
			rs.close();
			ps.close();
			return cols;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("sql 执行报错.");
		}finally{
			if(conn != null){
				conn.close();
			}
		}
	}

	/**
	 * 查询数据集数据
	 * @param dsetId
	 * @param dsid
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryDsetDatas(String dsid, String dsetId) throws Exception{
		JSONObject dataset = JSONObject.parseObject(this.getDatasetCfg(dsetId));
		DataSource ds = this.dsService.getDataSource(dsid);
		Map<String, String> tableAlias = createTableAlias(dataset);
		//需要进行关联的表
		JSONArray joinTabs = (JSONArray)dataset.get("joininfo");
		//生成sql
		StringBuffer sb = new StringBuffer("select ");
		for(Map.Entry<String, String> ent : tableAlias.entrySet()){
			if(ent.getValue().equals("a0")){
				continue;
			}
			sb.append( ent.getValue()+".*,");
		}
		sb.append("a0.*");

		String master = dataset.getString("master");
		sb.append(" from " + master + " a0");
		for(int i=0; joinTabs!=null&&i<joinTabs.size(); i++){  //通过主表关联
			JSONObject tab = joinTabs.getJSONObject(i);
			String ref = tab.getString("ref");
			String refKey = tab.getString("refKey");
			String jtype = (String)tab.get("jtype");
			if("left".equals(jtype) || "right".equals(jtype)){
				sb.append(" " + jtype);
			}
			sb.append(" join " + ref+ " " + tableAlias.get(ref));
			sb.append(" on a0."+tab.getString("col")+"="+tableAlias.get(ref)+"."+refKey);
			sb.append(" ");
		}
		Map<String, Object> ret = new HashMap<>();
		List<Map<String, Object>> datas = new ArrayList<>();
		List<String> heads = new ArrayList<>();
		Connection conn  = null;
		PreparedStatement ps = null;
		try {
			if(ds.getUse().equals("jndi")){
				conn = this.dsService.getJndi(ds);
			}else if(ds.getUse().equals("jdbc")){
				conn = this.dsService.getJDBC(ds);
			}
			String sql = sb.toString();
			logger.info(sql);
			ps = conn.prepareStatement(sql);
			ps.setMaxRows(100);
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			for(int i=0; i<meta.getColumnCount(); i++){
				String name = meta.getColumnName(i+1).toLowerCase();
				if(!heads.contains(name)) {
					heads.add(name.toLowerCase());
				}
			}
			while(rs.next()){
				Map<String, Object> dt = new CaseInsensitiveMap<>();
				for(String col : heads){
					Object val = DataSourceBuilder.getResultSetValue(rs, col);
					dt.put(col, val);
				}
				datas.add(dt);
			}
			rs.close();
			ret.put("header", heads);
			ret.put("datas", datas);
			return ret;
		}finally{
			if(ps != null){
				ps.close();
			}
			if(conn != null){
				conn.close();
			}
		}
	}

	private List<JSONObject> getJoinInfoByTname(String tname, JSONArray joins){
		List<JSONObject> ret = new ArrayList<JSONObject>();
		for(int i=0; joins!=null&&i<joins.size(); i++){
			JSONObject join = joins.getJSONObject(i);
			String ref = join.getString("ref");
			if(ref.equals(tname)){
				ret.add(join);
			}
		}
		return ret;
	}
}
