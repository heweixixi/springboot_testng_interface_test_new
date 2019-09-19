package com.example.springboot_quartz.model.case_po;

import java.util.Map;

public class CheckReslut {
		private String no;
		private Map<String, Object> columLableAndValueMap;
		public String getNo() {
			return no;
		}
		public void setNo(String no) {
			this.no = no;
		}
		public Map<String, Object> getColumLableAndValueMap() {
			return columLableAndValueMap;
		}
		public void setColumLableAndValueMap(Map<String, Object> columLableAndValueMap) {
			this.columLableAndValueMap = columLableAndValueMap;
		}
}
