package com.cxz.mushu.mapper.frame;

import com.cxz.mushu.entity.frame.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

	User getUserByStaffId(String staffId);
	
	User getUserById(@Param("userId") Integer userId);
	
	void updateuser(User user);
	
	void insertuser(User user);
	
	List<Map<String, Object>> listUserMenus(@Param("userId") Integer userId);

	List<User> listUsers(@Param("keyword") String keyword);

	void updateLogDateAndCnt(@Param("userId") Integer userId, @Param("dbName") String dbName);
	
	String checkPsd(@Param("userId") Integer userId);
	
	void modPsd(User user);
	
	Map<String, Object> appUserinfo(@Param("userId") Integer userId);

	int userExist(@Param("staffId") String staffId);

	int maxUserId( );
}
