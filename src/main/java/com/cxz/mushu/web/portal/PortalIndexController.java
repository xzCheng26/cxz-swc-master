package com.cxz.mushu.web.portal;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxz.mushu.entity.common.PageParam;
import com.cxz.mushu.entity.frame.User;
import com.cxz.mushu.entity.portal.Portal;
import com.cxz.mushu.entity.portal.ShareUrl;
import com.cxz.mushu.service.portal.PortalService;
import com.cxz.mushu.service.portal.ShareUrlService;
import com.cxz.mushu.util.BaseController;
import com.cxz.mushu.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/portal")
public class PortalIndexController extends BaseController {
	
	@Autowired
	private PortalService portalService;

	@Autowired
	private ShareUrlService urlService;
	
	@RequestMapping(value="/delete.action")
	public @ResponseBody
    Object delete(String pageId) {
		portalService.deletePortal(pageId);
		return this.buildSucces();
	}

	@RequestMapping(value="/get.action")
	public @ResponseBody
	Object get(String pageId) {
		String str = portalService.getPortalCfg(pageId);
		if(str == null){
			return this.buildError("报表不存在");
		}
		return this.buildSucces(str);
	}

	@RequestMapping(value="/share/get.action")
	public @ResponseBody
	Object shareGet(String token) {
		ShareUrl url = urlService.getByToken(token);
		String str = portalService.getPortalCfg(url.getReportId());
		if(str == null){
			return this.buildError("报表不存在");
		}
		return this.buildSucces(str);
	}

	@RequestMapping(value="/list.action")
	public @ResponseBody
	Object list(PageParam page) {
		if(page != null && page.getPage() != null && page.getRows() != null){
			PageHelper.startPage(page.getPage(), page.getRows());
		}
		List<Portal> ls = portalService.listPortal();
		PageInfo<Portal> pageInfo=new PageInfo<>(ls);
		return this.buildSucces(pageInfo);
	}
	
	@RequestMapping(value="/rename.action", method = RequestMethod.POST)
	public @ResponseBody
    Object rename(Portal portal) {
		portalService.renamePortal(portal);
		return this.buildSucces();
	}
	
	@RequestMapping(value="/save.action", method = RequestMethod.POST)
	public @ResponseBody
    Object save(Portal portal){
		String pageId = portal.getPageId();
		if(pageId == null || pageId.length() == 0){
			JSONObject obj = JSONObject.parseObject(portal.getPageInfo());
			String id = RSBIUtils.getUUIDStr();
			obj.put("id", id);
			portal.setPageId(id);
			portal.setUserId(RSBIUtils.getLoginUserInfo().getUserId());
			portal.setPageInfo(obj.toString());
			portal.setIs3g("y".equals(portal.getIs3g())?"y":"n");
			portalService.insertPortal(portal);
		}else{
			portalService.updatePortal(portal);
		}
		return super.buildSucces(portal.getPageId());
	}
}
