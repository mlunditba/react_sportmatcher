package ar.edu.itba.paw.webapp.config;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;


@EnableTransactionManagement
@EnableAsync
@ComponentScan({
	"ar.edu.itba.paw.webapp.controller",
	"ar.edu.itba.paw.webapp.dto",
	"ar.edu.itba.paw.service",
	"ar.edu.itba.paw.persistence"
})
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	
	private final int maxUploadSizeInMb = 20 * 1024 * 1024; // 20 MB
	private ApplicationContext applicationContext;
	
	@Value("classpath:schema.sql")
	private Resource schemaSql;
	
//	@Bean
//	public ViewResolver viewResolver() {
//		final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//		viewResolver.setViewClass(JstlView.class);
//		viewResolver.setPrefix("/WEB-INF/jsp/");
//		viewResolver.setSuffix(".jsp");
//		return viewResolver;
//	}
	
	@Bean
	public DataSource dataSource() {
		final SimpleDriverDataSource ds	= new SimpleDriverDataSource();
		ds.setDriverClass(org.postgresql.Driver.class);
		ds.setUrl("jdbc:postgresql://localhost/paw");
		ds.setUsername("postgres");
		ds.setPassword("root");
		return ds;
	}

	// JDBC configuration
	// @Bean
	// public DataSourceInitializer dataSourceInitializer(final DataSource ds) {
	// 	final DataSourceInitializer dsi = new DataSourceInitializer();
	// 	dsi.setDataSource(ds);
	// 	dsi.setDatabasePopulator(databasePopulator());
	// 	return dsi;
	// }

	// private DatabasePopulator databasePopulator()	{
	// 	final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();
	// 	dbp.addScript(schemaSql);
	// 	return dbp;
	// }

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPackagesToScan("ar.edu.itba.paw.model");
		factoryBean.setDataSource(dataSource());
		final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		factoryBean.setJpaVendorAdapter(vendorAdapter);
		final Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");
//		properties.setProperty("hibernate.show_sql", "true");
//		properties.setProperty("format_sql", "true");
		factoryBean.setJpaProperties(properties);
		return factoryBean;
	}

	@Bean
	public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
	
	@Primary
	@Bean
	public MessageSource messageSource() {
		final ReloadableResourceBundleMessageSource	messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
		messageSource.setCacheSeconds(5);
		return messageSource;
	}
	
	@Bean
	public MessageSource emailMessageSource() {
		final ReloadableResourceBundleMessageSource	messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:email/i18n/messages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
		messageSource.setCacheSeconds(5);
		return messageSource;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public Validator validator(final AutowireCapableBeanFactory autowireCapableBeanFactory) {
		ValidatorFactory vf = Validation.byProvider(HibernateValidator.class)
				.configure()
				.constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
				.buildValidatorFactory();
		return vf.getValidator();
	}

	/*@Bean
	public PlatformTransactionManager transactionManager(final DataSource ds) {
		return new DataSourceTransactionManager(ds);
	}*/
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/");
	}
	
	@Bean
    public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver cmr = new CommonsMultipartResolver();
        cmr.setMaxUploadSize(maxUploadSizeInMb * 2);
        cmr.setMaxUploadSizePerFile(maxUploadSizeInMb);
        return cmr;
    }

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("sportmatcher123@gmail.com");
		mailSender.setPassword("sm20191c");

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");

		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver(){
		final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(this.applicationContext);
		templateResolver.setPrefix("classpath:/email/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(true);
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(){
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}
	
//	@Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//            .allowedOrigins("*")
//            .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")
//            .allowedHeaders("Authorization", "Cache-Control", "Content-Type", "X-Auth-Token")
//            .exposedHeaders("Access-Control-Allow-Origin")
//            .allowCredentials(true);
//    }
	
}
