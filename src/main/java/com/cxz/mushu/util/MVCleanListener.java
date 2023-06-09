package com.cxz.mushu.util;

import com.rsbi.ext.engine.control.ContextListener;
import com.rsbi.ext.engine.view.context.ExtContext;
import com.rsbi.ext.engine.view.context.MVContext;

import javax.servlet.ServletContextEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 清除 MV 缓存得 listerner
 */
public class MVCleanListener implements ContextListener {
	
	private boolean isgo = true;

	@Override
	public void contextInit(ServletContextEvent arg0) {
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while(isgo) {
					try {
						//移除超过1小时得mv缓存
						long now = new Date().getTime();
						List<String> removeIds = new ArrayList<String>();
						Map<String, MVContext> mvs = ExtContext.getInstance().getAllMV();
						for(Map.Entry<String, MVContext> mv : mvs.entrySet()) {
							Date crtdate = mv.getValue().getCreateDate();
							if(crtdate != null && now - crtdate.getTime() >= 1000 * 60  * 60 ) {
								removeIds.add(mv.getKey());
							}
						}
						for(String mvId : removeIds) {
							ExtContext.getInstance().removeMV(mvId);
						}
						
						Thread.sleep(60 * 1000);
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
				}
			}
			
		});
		t.start();
		
	}

	@Override
	public void contextDest(ServletContextEvent arg0) {
		isgo = false;
	}

}
