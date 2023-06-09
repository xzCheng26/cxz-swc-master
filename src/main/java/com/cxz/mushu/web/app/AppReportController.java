package com.cxz.mushu.web.app;

import com.cxz.mushu.service.portal.MobReportTypeService;
import com.cxz.mushu.service.portal.PortalService;
import com.cxz.mushu.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/app")
public class AppReportController {

	@Autowired
	private MobReportTypeService service;
	
	@Autowired
	private PortalService portalService;
	
	@RequestMapping(value="/Report!listCata.action")
	public @ResponseBody
    Object listCata() {
		return service.listcataTree(); 
	}
	
	@RequestMapping(value="/Report!listReport.action")
	public @ResponseBody
    Object listReport(Integer cataId, HttpServletRequest request) {
		Integer userId = RSBIUtils.getLoginUserInfo().getUserId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		List<Map<String, Object>> ls = portalService.listAppReport(userId, cataId);
		for(int i=0; i<ls.size(); i++){
			Map<String, Object> m = ls.get(i);
			Object o = m.get("dt");
			m.put("dt", sdf.format(o));
			String url = basePath + "app/Report!view.action?rid=" + m.get("rid");
			m.put("url", url);
		}
		return ls;
	}
}
