//package kr.co.upchain.module_exception_monitoring.config;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.*;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//@Slf4j
//@Configuration
//@PropertySource("classpath:application.properties")
//@EnableTransactionManagement
//public class DatabaseConfiguration {
//
////	@PostConstruct
//	@Primary
//	@Bean
//	@ConfigurationProperties(prefix = "spring.datasource.hikari")
//	public HikariConfig hikariConfig() {
//		HikariConfig hikariConfig = new HikariConfig();
//		return hikariConfig;
//	}
//
//	@Primary
//	@Bean
//	public DataSource dataSource() {
//		HikariConfig hikariConfig = hikariConfig();
//		try {
//			hikariConfig.setJdbcUrl(hikariConfig.getJdbcUrl());
//			hikariConfig.setUsername(hikariConfig.getUsername());
//			hikariConfig.setPassword(hikariConfig.getPassword());
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		DataSource dataSource = new HikariDataSource(hikariConfig());
//		log.info("datasource : {}", dataSource);
//		return dataSource;
//	}
//
//	@Autowired
//	private ApplicationContext applicationContext;
//
//	@Primary
//	@Bean
//	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//		sqlSessionFactoryBean.setDataSource(dataSource);
//		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath*:sql/*_SQL.xml"));
//		sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("config/mybatis-config.xml"));
//		return sqlSessionFactoryBean.getObject();
//	}
//
//	@Primary
//	@Bean
//	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}
//
//	@Primary
//	@Bean
//	public PlatformTransactionManager txManager() {
//		return new DataSourceTransactionManager(dataSource());
//	}
//}
