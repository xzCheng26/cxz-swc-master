package com.cxz.mushu.service.model;

import com.cxz.mushu.entity.model.Dimension;
import com.cxz.mushu.mapper.model.DimensionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DimensionService {

	@Autowired
	private DimensionMapper mapper;
	
	public Dimension getDimInfo(Integer dimId, Integer cubeId){
		return mapper.getDimInfo(dimId, cubeId);
	}
}
