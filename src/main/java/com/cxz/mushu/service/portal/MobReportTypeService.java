package com.cxz.mushu.service.portal;

import com.cxz.mushu.entity.portal.MobReportType;
import com.cxz.mushu.mapper.portal.MobReportTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobReportTypeService {
	
	@Autowired
	private MobReportTypeMapper mapper;

	public List<MobReportType> listcataTree(){
		return mapper.listcataTree();
	}
	
	public void insertType(MobReportType type){
		mapper.insertType(type);
	}
	
	public void updateType(MobReportType type){
		mapper.updateType(type);
	}
	
	public void deleleType(Integer id){
		mapper.deleleType(id);
	}
	
	public MobReportType getType(Integer id){
		return mapper.getType(id);
	}
	
	public Integer cntReport(Integer id){
		return mapper.cntReport(id);
	}
	
	public Integer maxTypeId(){
		return mapper.maxTypeId();
	}
}
