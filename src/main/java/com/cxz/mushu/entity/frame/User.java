package com.cxz.mushu.entity.frame;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cxz.mushu.entity.common.BaseEntity;
import com.cxz.mushu.util.RSBIUtils;

import java.io.Serializable;
import java.util.Date;

public final class User extends BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6096757156465671644L;
	
	private Integer userId;
	private String staffId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date loginTime;
	private Date lastActive;
	private String loginIp;
	private String sessionId;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date logoutTime;
	private Integer logCnt;
	private String loginName;

	private String password;
	private String password2;  //在修改密码时使用

	private String gender;
	private String mobilePhone;
	private String email;
	private String officeTel;
	private int state; //1 为启用， 0为停用。
		
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public User() {
		
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public Date getLastActive() {
		return lastActive;
	}
	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Date getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getOfficeTel() {
		return officeTel;
	}
	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}
	
	@Override
	public String toString() {
		return "id = " + this.userId + ", name = " + this.loginName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getLogCnt() {
		return logCnt;
	}
	public void setLogCnt(Integer logCnt) {
		this.logCnt = logCnt;
	}
	
	@Override
	public void validate() {
		this.staffId = RSBIUtils.htmlEscape(this.staffId);
		this.loginName = RSBIUtils.htmlEscape(this.loginName);
	}

	@JsonIgnore
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}
}
