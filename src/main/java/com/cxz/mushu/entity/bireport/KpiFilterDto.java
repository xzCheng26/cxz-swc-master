package com.cxz.mushu.entity.bireport;

import com.cxz.mushu.entity.common.BaseEntity;

public class KpiFilterDto extends BaseEntity {

	private Integer kpi;
	private String filterType; //>,<,=,qj 四种
	private Double val1;
	private Double val2; //在区间匹配的时候，需要val2
	
	
	public String getFilterType() {
		return filterType;
	}
	
	public Integer getKpi() {
		return kpi;
	}

	public void setKpi(Integer kpi) {
		this.kpi = kpi;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	public Double getVal1() {
		return val1;
	}
	public Double getVal2() {
		return val2;
	}
	public void setVal1(Double val1) {
		this.val1 = val1;
	}
	public void setVal2(Double val2) {
		this.val2 = val2;
	}
	@Override
	public void validate() {
		 
	 }
}
