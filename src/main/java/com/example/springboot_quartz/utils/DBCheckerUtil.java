package com.example.springboot_quartz.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.springboot_quartz.mapper.customer.ResultMapper;
import com.example.springboot_quartz.model.case_po.CheckReslut;
import com.example.springboot_quartz.model.case_po.DBChecker;
import com.example.springboot_quartz.model.po.Student;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class DBCheckerUtil {
	@Autowired
	ResultMapper resultMapper;

		/**数据验证
		 * @param preValidateSql
		 * @return
		 */
		public static String doValuedate(String preValidateSql){
			log.info("验证脚本，json格式的数组字符串为："+preValidateSql);
			//将json数组数据解析为字符串，将要执行的sql和编号封装到对象中保存
//			List<DBChecker> dbCheckers = JSONObject.parseArray(preValidateSql, DBChecker.class);
			Gson gson = new Gson();
			List<DBChecker> dbCheckers = gson.fromJson(preValidateSql, new TypeToken<List<DBChecker>>() {
			}.getType());
			log.info("需要执行的sql条数为："+dbCheckers.size());
			List<CheckReslut> checkResluts = new ArrayList<CheckReslut>();
			//遍历集合，获取每一个对象里保存的sql脚本
			log.info("***************************开始数据库脚本查询***************************");
			for (DBChecker dbChecker : dbCheckers) {
					//拿sql脚本
					String sql = dbChecker.getSql();
					//拿no编号
					String no = dbChecker.getNo();
					log.info("正在执行的脚本编号为：【"+no+"】，脚本为：【"+sql+"】");
					//Object fildsValue = new Object();
//					Map<String, Object> columLableAndValueMap =JDBCUtil.query(sql);
					CheckReslut checkReslut = new CheckReslut();
					checkReslut.setNo(no);
					checkReslut.setColumLableAndValueMap(Maps.newHashMap());
					checkResluts.add(checkReslut);
			}
			//将对象转化为字符串（fastJSON）
			String result = JSONObject.toJSONString(checkResluts);
			log.info("数据验证，查询结果为："+result);
			log.info("***************************数据库脚本查询结束***************************");
			return result;
			
		}
}
