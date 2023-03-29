package com.cxz.mushu.mapper.model;

import com.cxz.mushu.entity.model.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DataSourceMapper {

	List<DataSource> listDataSource();
	
	void insertDataSource(DataSource ds);
	
	void updateDataSource(DataSource ds);
	
	void deleteDataSource(@Param("dsid") String dsid);
	
	DataSource getDataSource(@Param("dsid") String dsid);
}
