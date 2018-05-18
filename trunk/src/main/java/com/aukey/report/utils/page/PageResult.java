package com.aukey.report.utils.page;

import java.util.Collections;
import java.util.List;

/**
 * 分页组件.
 *
 * @author liangxiaofa
 * @date 2016年2月4日
 *
 */
public class PageResult<T> {

	private static final int DEFAULT_PAGE_SIZE = 20;

	/** 当前第几页 . */
	private int pageNo;
	/** 每页显示条目数. */
	private int pageSize = DEFAULT_PAGE_SIZE;
	/** 一共多少页. */
	@SuppressWarnings("unused")
	private int pageCounts = -1;
	/** 一共多少行. */
	private int recordsTotal;
	/**
	 * 是否有下一页. private boolean next;
	 */

	private List<T> results = Collections.EMPTY_LIST;

	public PageResult() {
	}

	public PageResult(int pageNo) {
		this.pageNo = pageNo;
	}

	public PageResult(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCounts() {
		if (pageCounts == -1) {
			int p = recordsTotal / pageSize;
			if (recordsTotal % pageSize > 0) {
				++p;
			}
			pageCounts = p;
		}
		return pageCounts;
	}

	public void setPageCounts(int pageCounts) {
		this.pageCounts = pageCounts;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public boolean getNext() {
		return getPageCounts() > pageNo;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public int getStart() {
		return (pageNo - 1) * pageSize;
	}

	public int getLimit() {
		return pageSize;
	}

}
