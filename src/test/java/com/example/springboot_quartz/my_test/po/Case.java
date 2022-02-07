package com.example.springboot_quartz.my_test.po;

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
}
