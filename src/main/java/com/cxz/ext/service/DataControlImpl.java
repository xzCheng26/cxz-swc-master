package com.cxz.ext.service;

import com.cxz.mushu.entity.frame.User;
import com.cxz.mushu.service.bireport.ModelCacheService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 默认数据权限控制类，如果需要用户可以再扩展
 * @author hq
 *
 */
public class DataControlImpl implements DataControlInterface {
	
	@Autowired
	private ModelCacheService cacheService;

	@Override
	public String process(User u, String tname) {
		
		return "";
	}

}
