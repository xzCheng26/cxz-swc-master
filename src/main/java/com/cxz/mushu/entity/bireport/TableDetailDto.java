package com.cxz.mushu.entity.bireport;

import com.cxz.mushu.entity.common.PageParam;

import java.util.Map;

/**
 * 明细提取dto
 */
public class TableDetailDto extends PageParam {
	
	private Map<String, String> pms;
	
	private String dsetId;
	private String dsid;

	public Map<String, String> getPms() {
		return pms;
	}

	public void setPms(Map<String, String> pms) {
		this.pms = pms;
	}

	public String getDsetId() {
		return dsetId;
	}

	public void setDsetId(String dsetId) {
		this.dsetId = dsetId;
	}

	public String getDsid() {
		return dsid;
	}

	public void setDsid(String dsid) {
		this.dsid = dsid;
	}


}
