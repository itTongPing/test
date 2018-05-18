package com.aukey.report.vo;

import java.util.List;

public class DepartmentResult {

	private String success;

	private String message;

	private List<DepartmentVO> data;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<DepartmentVO> getData() {
		return data;
	}

	public void setData(List<DepartmentVO> data) {
		this.data = data;
	}

	public class DepartmentVO {

		private int groupId;
		private String groupName;
		private String code;
		private String categoryName;
		private String categoryId;

		public int getGroupId() {
			return groupId;
		}

		public void setGroupId(int groupId) {
			this.groupId = groupId;
		}

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public String getCategoryId() {
			return categoryId;
		}

		public void setCategoryId(String categoryId) {
			this.categoryId = categoryId;
		}

	}

}
