package com.example.springboot_quartz.model.case_po;

import lombok.Data;

@Data
public class Case {
		private String caseId;
		private String apiId;
		private String RequestData;
		private String ExpectedResponseData;
		private String ActualResponseData;
		private String PreValidateSql;
		private String PreValidateResult;
		private String AfterValidateSql;
		private String AfterValidateResult;
		private String header;
}
