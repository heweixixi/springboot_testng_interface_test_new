package com.example.springboot_quartz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.w3c.dom.DocumentType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ${user} on 2019/7/18
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {
    private final String title = "springboot_quartz";
    private final String description = "quartz使用文档";
    private final String version = "1.0";


    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title(title).description(description).version(version).build())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET,gloableResponseMessages())
                .globalResponseMessage(RequestMethod.POST,gloableResponseMessages())
                .globalResponseMessage(RequestMethod.DELETE,gloableResponseMessages())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.springboot_quartz.controller"))
                .paths(PathSelectors.any())
                .build();
    }



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 自定义每个接口通用的返回code说明
     *
     * @return
     */

    private List<ResponseMessage> gloableResponseMessages() {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder().code(1).message("成功").build());
        responseMessages.add(new ResponseMessageBuilder().code(400).message("请求参数错误").build());
        responseMessages.add(new ResponseMessageBuilder().code(401).message("用户未授权").build());
        responseMessages.add(new ResponseMessageBuilder().code(403).message("拒绝访问").build());
        responseMessages.add(new ResponseMessageBuilder().code(404).message("不存在").build());
        responseMessages.add(new ResponseMessageBuilder().code(500).message("服务器异常").build());
        responseMessages.add(new ResponseMessageBuilder().code(600).message("业务逻辑错误").build());
        return responseMessages;
    }
}
