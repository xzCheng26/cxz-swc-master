package com.cxz.mushu.web.m;

import com.alibaba.fastjson.JSONObject;
import com.cxz.mushu.entity.portal.MobReportType;
import com.cxz.mushu.service.portal.MobReportTypeService;
import com.cxz.mushu.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/m")
public class MobileController extends BaseController {
	
	@Autowired
	private MobReportTypeService service;

	@RequestMapping(value="/typeTree.action")
	public @ResponseBody
    Object tree() {
		return service.listcataTree(); 
	}
	
	@RequestMapping(value="/addType.action", method = RequestMethod.POST)
	public @ResponseBody
    Object addType(MobReportType type) {
		service.insertType(type);
		Integer maxId = service.maxTypeId();
		return super.buildSucces(maxId);
	}
	
	@RequestMapping(value="/updateType.action", method = RequestMethod.POST)
	public @ResponseBody
    Object updateType(MobReportType type) {
		service.updateType(type);
		return super.buildSucces();
	}
	
	@RequestMapping(value="/deleteType.action")
	public @ResponseBody
    Object deleteType(Integer id) {
		if(service.cntReport(id) > 0){
			return super.buildError("分类下存在报表,不能删除。");
		}else{
			service.deleleType(id);
			return super.buildSucces();
		}
	}
	
	@RequestMapping(value="/getType.action")
	public @ResponseBody
    Object getType(Integer id) {
		return service.getType(id);
	}
}
