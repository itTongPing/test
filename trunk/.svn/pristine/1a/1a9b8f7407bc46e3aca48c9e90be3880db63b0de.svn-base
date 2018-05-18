package com.aukey.report.base;

import java.io.Serializable;
import java.util.*;

/**
 * Boss的统一输出json格式.
 * 
 * @author xiehz
 * @date 2016年2月4日
 *
 */
public class ResponseResult implements Serializable {

	public static interface STATUS {
		/** 操作成功. */
		int S_200 = 200;
		/** 服务器内部异常. */
		int S_500 = 500;
		/** 没有操作权限. */
		int S_403 = 403;
	}

	private static final long serialVersionUID = 1L;

	/** 每个状态码表示一个操作结果状态，如200为成功. */
	private int status;

	/** 提示消息. */
	private String message;

	private Map<String, Object> data = new LinkedHashMap<String, Object>();

	public ResponseResult() {
	}

	public ResponseResult(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public ResponseResult setStatus(int status) {
		this.status = status;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ResponseResult setMessage(String message) {
		this.message = message;
		return this;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public ResponseResult setData(Map<String, Object> data) {
		this.data = data;
		return this;
	}

	public ResponseResult setDataPageNo(Integer pageNo) {
		this.data.put("pageNo", pageNo);
		return this;
	}

	public ResponseResult setDataPageSize(Integer pageSize) {
		this.data.put("pageSize", pageSize);
		return this;
	}

	public ResponseResult setDataPageCounts(Integer pageCounts) {
		this.data.put("pageCounts", pageCounts);
		return this;
	}

	public ResponseResult setDataRecordsTotal(Integer recordsTotal) {
		this.data.put("recordsTotal", recordsTotal);
		return this;
	}

	public ResponseResult setDataResult(List<? extends Object> tBody) {
		if (this.data.containsKey("results")) {
			Results results = (Results) this.data.get("results");
			results.settBody(tBody);
			return this;
		}
		final Results results = new Results();
		results.settBody(tBody);
		this.data.put("results", results);
		return this;
	}

	public ResponseResult setDataResult(List<? extends Object> tBody, List<String> tFooter) {
		if (this.data.containsKey("results")) {
			Results results = (Results) this.data.get("results");
			results.settBody(tBody);
			results.settFooter(tFooter);
			return this;
		}
		final Results results = new Results();
		results.settBody(tBody);
		results.settFooter(tFooter);
		this.data.put("results", results);
		return this;
	}

	public ResponseResult setDataResult(Set<String> headers, List<Collection<Object>> values) {
		if (this.data.containsKey("results")) {
			Results results = (Results) this.data.get("results");
			results.settHeader(headers);
			results.settBody(values);
			return this;
		}
		final Results results = new Results();
		results.settHeader(headers);
		results.settBody(values);
		this.data.put("results", results);
		return this;
	}

	public ResponseResult setDataResults(Set<String> headers, List<List<String>> values) {
		if (this.data.containsKey("results")) {
			Results results = (Results) this.data.get("results");
			results.settHeader(headers);
			results.settBody(values);
			return this;
		}
		final Results results = new Results();
		results.settHeader(headers);
		results.settBody(values);
		this.data.put("results", results);
		return this;
	}

	public ResponseResult setData(String key, Object value) {
		this.data.put(key, value);
		return this;
	}

	public static class Results {

		private List<? extends Object> tBody = new ArrayList<Object>();

		private List<String> tFooter = new ArrayList<String>();

		private Set<String> tHeader = new HashSet<>();

		public Results() {
		}

		public void settHeader(Set<String> tHeader) {
			this.tHeader = tHeader;

		}

		public Results(List<? extends Object> tBody) {
			super();
			this.tBody = tBody;
		}

		public Results(List<? extends Object> tBody, List<String> tFooter) {
			super();
			this.tBody = tBody;
			this.tFooter = tFooter;
		}

		public List<? extends Object> gettBody() {
			return tBody;
		}

		public void settBody(List<? extends Object> tBody) {
			this.tBody = tBody;
		}

		public List<String> gettFooter() {
			return tFooter;
		}

		public void settFooter(List<String> tFooter) {
			this.tFooter = tFooter;
		}

		public Set<String> gettHeader() {
			return tHeader;
		}
	}
}
