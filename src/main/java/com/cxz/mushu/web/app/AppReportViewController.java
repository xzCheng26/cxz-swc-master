package com.cxz.mushu.web.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.view.context.MVContext;
import com.cxz.mushu.service.portal.PortalPageService;
import com.cxz.mushu.service.portal.PortalService;
import com.cxz.mushu.util.CompPreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Scope("prototype")
@RequestMapping(value = "/app")
public class AppReportViewController {
	
	@Autowired
	private PortalService portalService;
	
	@Autowired
	private PortalPageService pageService;
	
	@RequestMapping(value="/Report!view.action")
	public String view(String rid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		String cfg = portalService.getPortalCfg(rid);
		if(cfg == null){
			return "找不到报表文件。";
		}
		JSONObject json = (JSONObject)JSON.parse(cfg);
		MVContext mv = pageService.json2MV(json, false, false);
		CompPreviewService ser = new CompPreviewService(req, res, req.getServletContext());
		ser.setParams(pageService.getMvParams());
		ser.initPreview();
		String ret = ser.buildMV(mv, req.getServletContext());
		req.setAttribute("str", ret);
		return "app/Report-view"; 
	}

}
