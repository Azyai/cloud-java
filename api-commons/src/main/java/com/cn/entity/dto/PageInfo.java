package com.cn.entity.dto;

import lombok.Data;

@Data
public class PageInfo {
	private Long page;
	private Long size;
	private Long total;

	public PageInfo() {
	}

	public PageInfo(Long page, Long size, Long total) {
		this.page = page;
		this.size = size;
		this.total = total;
	}

	public Long getPage() {
		if (page == null || page < 1L) {
			page = 1L;
		}
		return page;
	}

	public Long getSize() {
		if (size == null || size < 1L) {
			size = 10L;
		}
		return size;
	}

	public Long getTotal() {
		if (total == null || total < 1L) {
			total = 0L;
		}
		return total;
	}
}
