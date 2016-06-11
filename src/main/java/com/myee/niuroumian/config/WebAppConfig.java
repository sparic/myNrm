package com.myee.niuroumian.config;

import com.myee.niuroumian.service.OrderService;
import com.myee.niuroumian.service.impl.OrderServiceImpl;
import me.chanjar.weixin.mp.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableJpaRepositories(basePackages = "com.myee.*.dao")
@EnableTransactionManagement
@PropertySources({@PropertySource("classpath:/redis.properties")})
@ComponentScan(basePackages = {"com.myee.*.service", "com.myee.*.controller","com.myee.niuroumian.websocket"})
@EnableWebMvc
//@EnableRedisHttpSession
public class WebAppConfig extends WebMvcConfigurerAdapter {


//    @Bean
//    @Scope("singleton")
//    public OrderService orderService() {
//        OrderService orderService = new OrderServiceImpl(redisTemplate);
//        return orderService;
//    }

    @Bean
    public JpaTransactionManager transactionManager(DataSource dataSource) throws SQLException {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }

    @Bean
    public PersistenceExceptionTranslator persistenceExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    DataSource dataSource() {
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource("java:comp/env/jdbc/freightDS");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/");
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1024000000L);
        return multipartResolver;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setGenerateDdl(false);
        adapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("com.myee.niuroumian.domain", "com.myee.niuroumian");
//        factoryBean.setMappingResources("META-INF/adempiere.xml");
        factoryBean.setJpaVendorAdapter(adapter);
        factoryBean.setDataSource(dataSource);
//        factoryBean.setPersistenceUnitName("nrm");
        return factoryBean;
    }

//    @Bean
//    public JdbcTemplate initJdbcTemplate(){
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
//        return jdbcTemplate;
//    }

    @Bean
    @Scope("singleton")
    public WxMpInMemoryConfigStorage wxMpConfigStorage() {
        WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
        wxMpConfigStorage.setAppId("wxe67244505b4041b6"); // 设置微信公众号的appid
        wxMpConfigStorage.setSecret("ae3b4cd8a550fab663c90ab16d548579"); // 设置微信公众号的app corpSecret
        wxMpConfigStorage.setToken("clever"); // 设置微信公众号的token
        wxMpConfigStorage.setAesKey("DKigNihAx9rcVHKJvc9D6xBnXCFHTUe4MnTBw01bLM7"); // 设置微信公众号的EncodingAESKey
        return wxMpConfigStorage;
    }

    @Bean
    JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMaxWaitMillis(5000);
        config.setTestOnBorrow(true);
        return config;
    }

    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Scope("singleton")
    JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig poolConfig) {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(redisHost);
        connectionFactory.setPort(redisPort);
        connectionFactory.setUsePool(true);
        connectionFactory.setPoolConfig(poolConfig);
        return connectionFactory;
    }

    @Bean
    RedisTemplate redisTemplate(JedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    CacheManager cacheManager(RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }

    @Bean
    public WxMpService wxMpService(WxMpConfigStorage wxMpConfigStorage) {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    @Bean
    @Scope("singleton")
    WxMpMessageRouter wxMpMessageRouter(WxMpService wxMpService) {
        WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
        return wxMpMessageRouter;
    }

//    @Bean
//    public OrderService orderService() {
//        // ...
//    }
//
//    @Bean
//    public OrderSender orderSender() {
////        return new OrderSender(new OrderService());
//    }
}
