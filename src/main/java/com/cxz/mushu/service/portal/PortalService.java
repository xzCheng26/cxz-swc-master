package com.cxz.mushu.service.portal;

import com.cxz.mushu.entity.portal.Portal;
import com.cxz.mushu.mapper.portal.PortalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PortalService {
	
	@Autowired
	private PortalMapper mapper;
	
	public List<Portal> listPortal(){
		return mapper.listPortal();
	}
	
	public String getPortalCfg(String pageId){
		return mapper.getPortalCfg(pageId);
	}
	
	public List<Portal> list3g(Integer cataId){
		return mapper.list3g(cataId);
	}
	
	public void insertPortal(Portal portal){
		mapper.insertPortal(portal);
	}
	
	public void deletePortal(String pageId){
		mapper.deletePortal(pageId);
	}
	
	public void updatePortal(Portal portal){
		mapper.updatePortal(portal);
	}
	
	public Portal getPortal(String pageId){
		return mapper.getPortal(pageId);
	}
	
	public void renamePortal(Portal portal){
		mapper.renamePortal(portal);
	}
	
	public List<Map<String, Object>> listAppReport(Integer userId, Integer cataId){
		return mapper.listAppReport(userId, cataId);
	}
}
