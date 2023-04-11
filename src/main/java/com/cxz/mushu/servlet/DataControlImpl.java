package com.cxz.mushu.servlet;

import com.cxz.mushu.entity.frame.User;

/**
 * 默认数据权限控制类，如果需要用户可以再扩展
 */
public class DataControlImpl implements DataControlInterface {

	@Override
	public String process(User u, String tname) {
		return "";
	}

}
