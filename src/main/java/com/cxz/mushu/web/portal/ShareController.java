/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.cxz.mushu.web.portal;

import com.cxz.mushu.entity.portal.ShareUrl;
import com.cxz.mushu.service.portal.ShareUrlService;
import com.cxz.mushu.util.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * 报表分享
 */
@Controller
@RequestMapping(value = "/portal")
public class ShareController extends BaseController {
	
	private static Logger log = Logger.getLogger(ShareController.class);
	

	
	@Autowired
	private ShareUrlService urlService;
	
	@RequestMapping(value="/copyUrl.action", method = RequestMethod.POST)
	public @ResponseBody
    Object copyUrl(ShareUrl dto) {
		dto.setrType(1);
		urlService.saveShareUrl(dto);
		return super.buildSucces(dto.getToken());
	}

}
