package com.cxz.mushu.web.app;

import com.cxz.mushu.entity.app.Collect;
import com.cxz.mushu.service.app.CollectService;
import com.cxz.mushu.util.BaseController;
import com.cxz.mushu.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/app")
public class CollectController extends BaseController {
	
	@Autowired
	private CollectService service;
	
	@RequestMapping(value="/Collect!list.action")
	public @ResponseBody
    Object list(HttpServletRequest request) {
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		Integer userId = RSBIUtils.getLoginUserInfo().getUserId();
		return service.listCollect(userId, basePath);
	}

	@RequestMapping(value="/Collect!add.action")
	public @ResponseBody
    Object add(Collect collect) {
		Integer userId = RSBIUtils.getLoginUserInfo().getUserId();
		collect.setUserId(userId);
		Map<String, Object> ret = new HashMap<String, Object>();
		int cnt = service.collectExist(collect);
		if(cnt > 0){
			ret.put("result", false);
		}else{
			ret.put("result", true);
			service.addCollect(collect);
		}
		return ret;
	}
	
	@RequestMapping(value="/Collect!delete.action")
	public @ResponseBody
    Object delete(Collect collect) {
		Integer userId = RSBIUtils.getLoginUserInfo().getUserId();
		collect.setUserId(userId);
		service.delCollect(collect);
		return super.buildSucces();
	}

}
