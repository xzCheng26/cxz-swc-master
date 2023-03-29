package com.cxz.mushu.entity.bireport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.cxz.mushu.entity.common.BaseEntity;
import com.cxz.mushu.util.RSBIUtils;

import java.util.Date;

public class OlapInfo extends BaseEntity {

	private Integer pageId;
	private Integer userId;
	private String pageInfo;
	private String pageName;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date crtDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updateDate;
	private String crtuser;
	
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(String pageInfo) {
		this.pageInfo = pageInfo;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public Date getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(Date crtDate) {
		this.crtDate = crtDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getCrtuser() {
		return crtuser;
	}
	public void setCrtuser(String crtuser) {
		this.crtuser = crtuser;
	}
	@Override
	public void validate() {
		this.pageName = RSBIUtils.htmlEscape(this.pageName);
	}
}
