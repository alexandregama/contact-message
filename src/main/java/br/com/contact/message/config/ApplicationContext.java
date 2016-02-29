package br.com.contact.message.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
@ComponentScan(basePackages = { "br.com.contact.message.controller", "br.com.contact.message.service" })
@EnableJpaRepositories
@EnableTransactionManagement
@EnableWebMvc
@PropertySource("classpath:application.properties")
public class ApplicationContext extends WebMvcConfigurerAdapter {

	@Resource
	private Environment env;

	@Bean
	public DataSource dataSource() {
		BoneCPDataSource ds = new BoneCPDataSource();

		ds.setDriverClass(env.getRequiredProperty("db.driver"));
		ds.setJdbcUrl(env.getRequiredProperty("db.url"));
		ds.setUsername(env.getRequiredProperty("db.username"));
		ds.setPassword(env.getRequiredProperty("db.password"));

		return ds;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

	    em.setDataSource(dataSource());
	    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
	    em.setPackagesToScan(env.getRequiredProperty("entitymanager.packages.to.scan"));

	    Properties p = new Properties();
	    p.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
	    p.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
	    p.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
	    p.put("hibernate.ejb.naming_strategy", env.getRequiredProperty("hibernate.ejb.naming_strategy"));
	    p.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
	    em.setJpaProperties(p);

	    return em;
	}
	
	@Bean
	public JpaTransactionManager transactionManager() {
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
	    return transactionManager;
	}
	
}
