package com.example.springboot_quartz.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;


/**
 * created by ${user} on 2019/9/11
 */
@Configuration
@Slf4j
public class HttpClientConfig {

    @Autowired
    HttpClientPoolConfig httpClientPoolConfig;

    /**
     * 获取普通的RestTemplate
     * @return
     */
    @Bean(name = "restTemplate")
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }


    /**
     * 普通的httpclient
     * @return
     */
    @Bean
    public HttpClient getHttpClient(){
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        //另一种方式获取 httpClientBuilder
//        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        try {
            /**
             * 1、Registry注册http和https请求
             */
            //设置信任ssl访问
            //第一种方式
//            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,(arg0,arg1)->true).build();
            //第二种方式
            SSLContext sslContext = createIgnoreVerifySSL();
            httpClientBuilder.setSSLContext(sslContext);
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    // 注册http和https请求
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https",sslConnectionSocketFactory)
                    .build();
            /**
             * 2、设置httpclient连接池，可以使用Registry构建poolingHttpClientConnectionManager
             */
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry);
            //设置最大连接数
            poolingHttpClientConnectionManager.setMaxTotal(httpClientPoolConfig.getMaxTotalConnect());
            //设置路由并发数
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(httpClientPoolConfig.getMaxConnectPerRout());

            //设置连接池
            httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
            //设置重试次数
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(httpClientPoolConfig.getRetryTimes(),true));

            return httpClientBuilder.build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * restTemplate集成httpclient
     * @param factory
     * @return
     */
    @Bean(name = "httpClientTemplate")
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return createRestTemplate(factory);
    }

    private RestTemplate createRestTemplate(ClientHttpRequestFactory factory){
        RestTemplate restTemplate = new RestTemplate(factory);
        //设置错误处理器
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    @Bean(name = "clientHttpRequestFactory ")
    public ClientHttpRequestFactory clientHttpRequestFactory(){
        /**
         *  maxTotalConnection 和 maxConnectionPerRoute 必须要配
         */
        if (httpClientPoolConfig.getMaxTotalConnect() <= 0) {
            throw new IllegalArgumentException("invalid maxTotalConnection: " + httpClientPoolConfig.getMaxTotalConnect());
        }
        if (httpClientPoolConfig.getMaxConnectPerRout() <= 0) {
            throw new IllegalArgumentException("invalid maxConnectionPerRoute: " + httpClientPoolConfig.getMaxConnectPerRout());
        }

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(getHttpClient());
        // 连接超时
        clientHttpRequestFactory.setConnectTimeout(httpClientPoolConfig.getConnectTimeout());
        // 数据读取超时时间，即SocketTimeout
        clientHttpRequestFactory.setReadTimeout(httpClientPoolConfig.getReadTimeout());
        // 从连接池获取请求连接的超时时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        clientHttpRequestFactory.setConnectionRequestTimeout(httpClientPoolConfig.getConnectionRequestTimout());
        return clientHttpRequestFactory;
    }

    /**
     * 绕过https证书认证实现访问
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[] { trustManager }, null);
        return sc;
    }
}
