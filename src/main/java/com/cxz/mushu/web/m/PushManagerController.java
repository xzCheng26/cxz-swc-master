package com.cxz.mushu.web.m;

import com.alibaba.fastjson.JSONObject;
import com.cxz.mushu.entity.portal.Portal;
import com.cxz.mushu.service.portal.MobReportTypeService;
import com.cxz.mushu.service.portal.PortalService;
import com.cxz.mushu.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/m")
public class PushManagerController extends BaseController {

	@Autowired
	private MobReportTypeService service;
	
	@Autowired
	private PortalService portalService;
	
	@RequestMapping(value="/pushList.action")
	public @ResponseBody
    Object list(Integer cataId) {
		List<Portal> ls = portalService.list3g(cataId);
		return ls;
	}
}
