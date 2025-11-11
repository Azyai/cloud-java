package com.cn.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageWrapper<T> {
	protected boolean isIsMulti = false;
	protected List<T> tbody = new ArrayList<>();
	protected PageInfo pageInfo;

	public PageWrapper<T> setIsMulti(final boolean isIsMulti){
		this.isIsMulti = isIsMulti;
		return this;
	}
}