package com.example.springboot_quartz.my_test.util;

import lombok.extern.slf4j.Slf4j;
import org.testng.collections.Maps;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
@Slf4j
public class JDBCUtil {
    private static Properties properties;

    static {
        properties = new Properties();
        try(FileInputStream inputStream = new FileInputStream(new File("src/test/resources/jdbc.properties"))){
            properties.load(inputStream);
        }catch (Exception e){
            log.error("加载配置文件出错");
        }
    }

    public static Map<String,Object> query(String sql, Object ... objects){
        Map<String,Object> cellName2ValueMap = Maps.newHashMap();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            Class.forName(properties.getProperty("jdbc.driver"));
            String url = properties.getProperty("jdbc.url");
            String username = properties.getProperty("jdbc.username");
            String password = properties.getProperty("jdbc.password");
            connection = DriverManager.getConnection(url, username, password);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                preparedStatement.setObject(i+1,objects[i]);
            }
            resultSet = preparedStatement.executeQuery();
            //获取元注解，以便从中取出列名
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object cellValue = resultSet.getObject(columnName);
                cellName2ValueMap.put(columnName,cellValue);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
                try {
                    if (resultSet!=null){
                        resultSet.close();
                    }
                    if (preparedStatement!=null){
                        preparedStatement.close();
                    }
                    if (connection!=null){
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }

        return cellName2ValueMap;
    }
}
