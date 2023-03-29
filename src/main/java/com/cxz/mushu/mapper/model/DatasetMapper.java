package com.cxz.mushu.mapper.model;

import com.cxz.mushu.entity.model.Dataset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DatasetMapper {

	List<Dataset> listDataset();
	
	void updateDset(Dataset ds);
	
	void insertDset(Dataset ds);
	
	void deleteDset(@Param("dsetId") String dsetId);
	
	String getDatasetCfg(@Param("dsetId") String dsetId);
	
	void updateDsetCfg(Dataset ds);
}
