/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.cxz.mushu.mapper.portal;

import com.cxz.mushu.entity.portal.ShareUrl;
import org.apache.ibatis.annotations.Param;

public interface ShareUrlMapper {
	
    int deleteByPrimaryKey(@Param("token") String token);

    int insert(ShareUrl record);

    ShareUrl selectByPrimaryKey(@Param("token") String token);

    int updateByPrimaryKey(ShareUrl record);
}