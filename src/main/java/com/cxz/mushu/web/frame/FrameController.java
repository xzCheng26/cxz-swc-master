package com.cxz.mushu.web.frame;

import com.cxz.mushu.service.frame.MenuService;
import com.cxz.mushu.util.BaseController;
import com.cxz.mushu.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限管理
 */
@Controller
@RequestMapping(value = "/frame")
public class FrameController extends BaseController {
	
	@Autowired
	private MenuService service;

	/**
	 * 获取权限管理菜单列表（包括用户管理、角色管理、菜单管理）
	 */
	@RequestMapping(value="/Menus.action")
	public @ResponseBody
	Object execute() {
		Integer userId = RSBIUtils.getLoginUserInfo().getUserId();
		return super.buildSucces(service.listUserMenus(userId));
	}
}
