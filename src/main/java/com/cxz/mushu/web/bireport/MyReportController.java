package com.cxz.mushu.web.bireport;

import com.alibaba.fastjson.JSONObject;
import com.cxz.mushu.entity.bireport.OlapInfo;
import com.cxz.mushu.service.bireport.OlapService;
import com.cxz.mushu.util.BaseController;
import com.cxz.mushu.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 */
@Controller
@RequestMapping(value = "/bireport")
public class MyReportController extends BaseController {
	
	@Autowired
	private OlapService service;

	/**
	 *
	 */
	@RequestMapping(value="/listReport.action")
	public @ResponseBody Object list(String keyword){
		List<OlapInfo> ret = service.listreport(keyword);
		return super.buildSucces(ret);
	}

	/**
	 *
	 */
	@RequestMapping(value="/getReport.action")
	public @ResponseBody Object getReport(Integer pageId){
		OlapInfo olap = service.getOlap(pageId);
		return super.buildSucces(olap);
	}

	/**
	 *
	 */
	@RequestMapping(value="/saveReport.action", method = RequestMethod.POST)
	public @ResponseBody
    Object save(OlapInfo info){
		if(service.olapExist(info.getPageName()) > 0){
			return super.buildError("报表名存在。");
		}
		if(info.getPageId() == null){
			info.setPageId(service.maxOlapId());
			info.setUserId(RSBIUtils.getLoginUserInfo().getUserId());
			JSONObject page = JSONObject.parseObject(info.getPageInfo());
			page.put("id", info.getPageId());
			info.setPageInfo(page.toString());
			service.insertOlap(info);
		}else{
			service.updateOlap(info);
		}
		//返回ID
		return super.buildSucces(info.getPageId());
	}

	/**
	 *
	 */
	@RequestMapping(value="/deleteReport.action")
	public @ResponseBody
    Object deleteReport(Integer id){
		service.deleteOlap(id);
		return this.buildSucces();
	}

	/**
	 *
	 */
	@RequestMapping(value="/renameReport.action")
	public @ResponseBody
    Object rename(OlapInfo info){
		service.renameOlap(info);
		return this.buildSucces();
	}

}
