package com.cxz.mushu.web.model;

import com.rsbi.ext.engine.view.exception.ExtConfigException;
import com.cxz.mushu.entity.model.DataSource;
import com.cxz.mushu.service.model.DataSourceService;
import com.cxz.mushu.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/model")
public class DataSourceController extends BaseController {

	@Autowired
	private DataSourceService dsService;
	
	@RequestMapping(value="/listDataSource.action")
	public @ResponseBody
    Object list(){
		return super.buildSucces(dsService.listDataSource());
	}
	
	@RequestMapping(value="/getDataSource.action")
	public @ResponseBody
    Object get(String dsid){
		return super.buildSucces(dsService.getDataSource(dsid));
	}
	
	@RequestMapping(value="/deleteDataSource.action")
	public @ResponseBody
    Object delete(String dsid){
		dsService.deleteDataSource(dsid);
		return this.buildSucces();
	}
	@RequestMapping(value="/testDataSource.action", method = RequestMethod.POST)
	public @ResponseBody
    Object test(DataSource ds) throws ExtConfigException {
		ds.setUse("jdbc");
		return dsService.testDataSource(ds);
	}
	@RequestMapping(value="/testJndi.action", method = RequestMethod.POST)
	public @ResponseBody
    Object testJndi(DataSource ds){
		ds.setUse("jndi");
		return dsService.testJNDI(ds);
	}
	
	@RequestMapping(value="/saveDataSource.action", method = RequestMethod.POST)
	public @ResponseBody
    Object save(DataSource ds){
		dsService.insertDataSource(ds);
		return this.buildSucces();			
	}
	
	@RequestMapping(value="/updateDataSource.action", method = RequestMethod.POST)
	public @ResponseBody
    Object update(DataSource ds){
		dsService.updateDataSource(ds);
		return this.buildSucces();			
	}
}
