package kr.co.softcampus.config;

import javax.annotation.Resource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.softcampus.Interceptor.CheckLoginInterceptor;
import kr.co.softcampus.Interceptor.CheckWriterInterceptor;
import kr.co.softcampus.Interceptor.TopMenuInterceptor;
import kr.co.softcampus.beans.UserBean;
import kr.co.softcampus.mapper.BoardMapper;
import kr.co.softcampus.mapper.TopMenuMapper;
import kr.co.softcampus.mapper.UserMapper;
import kr.co.softcampus.service.BoardService;
import kr.co.softcampus.service.TopMenuService; 

//Spring MVC 프로젝트에 관련된 설정을 하는 클래스
@Configuration
// Controller 어노테이션이 셋팅되어 있는 클래스를 Controller로 등록한다
@EnableWebMvc
//스캔할 패키지를 지정한다
@ComponentScan("kr.co.softcampus.controller")
@ComponentScan("kr.co.softcampus.dao")
@ComponentScan("kr.co.softcampus.service")
@PropertySource("/WEB-INF/properties/db.properties")
public class ServletAppContext implements WebMvcConfigurer{
	
	@Value("${db.classname}")
	private String db_classname;

	@Value("${db.url}")
	private String db_url;
	
	@Value("${db.username}")
	private String db_username;
	
	@Value("${db.password}")
	private String db_password;
	
	//사용하는 이유는 인터셉터에서 @Autowired가 불가능하므로 
	//여기서 생성한뒤에 topMenuservice를 이용해서 밑에 인터셉터를 사용해주자
	@Autowired
	private TopMenuService topMenuservice;
	
	//세션스코프에 있는 로그인유저 주입
	@Resource(name = "loginUserBean")
	private UserBean loginUserBean;
	
	//인터셉터 (내 글 아닌것에 수정삭제를 url를 통해 접근시에 사용)
	@Autowired
	private BoardService boardService;
	
	// Contorller의 메서드가 반환하는 jsp의 이름 앞뒤에 경로와 확장자를 붙혀주도록 설정한다.
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	
	// 정적 파일의 경로를 매핑한다
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/**").addResourceLocations("/resources/");
	}
	
	//데이터베이스 접속 정보를 관리하는 Bean
	@Bean
	public BasicDataSource dataSource() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(db_classname);
		source.setUrl(db_url);
		source.setUsername(db_username);
		source.setPassword(db_password);
		return source;
	}
	
	//쿼리문과 접속 정보를 관리하는 객체
	@Bean
	public SqlSessionFactory factory(BasicDataSource source) throws Exception{
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(source);
		SqlSessionFactory factory = factoryBean.getObject();
		return factory;
	}
	
	//쿼리문 실행을 위한 객체(Mapper 관리)
	@Bean
	public MapperFactoryBean<BoardMapper> getBoardMapper(SqlSessionFactory factory) throws Exception{
		MapperFactoryBean<BoardMapper> factoryBean = new MapperFactoryBean<BoardMapper>(BoardMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}
	
	//이것은 바로 위의 Bean과 동일하게 작성해준다
	//바뀌는 것은 <>안에 있는 Mapper의 타입만 바꿔서 작성해주자
	@Bean
	public MapperFactoryBean<TopMenuMapper> getTopMenuMapper(SqlSessionFactory factory) throws Exception{
		MapperFactoryBean<TopMenuMapper> factoryBean = new MapperFactoryBean<TopMenuMapper>(TopMenuMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}
	
	//이것은 바로 위의 Bean과 동일하게 작성해준다
	//바뀌는 것은 <>안에 있는 Mapper의 타입만 바꿔서 작성해주자
	@Bean
	public MapperFactoryBean<UserMapper> getUserMapper(SqlSessionFactory factory) throws Exception{
		MapperFactoryBean<UserMapper> factoryBean = new MapperFactoryBean<UserMapper>(UserMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 바로밑에줄은 addInterceptors(스프링메소드)를 생성하면 자동으로 생기는것
		WebMvcConfigurer.super.addInterceptors(registry);
		
		//여기서부터 우리가 작성해야 하는 코드
		//바로밑에줄 new의 괄호안에는 위에서 @Autowired받은것으로 인터셉터 @Autowired를 생성자로 해야하는 것에서부터 시작함
		TopMenuInterceptor topMenuInterceptor = new TopMenuInterceptor(topMenuservice, loginUserBean);
		InterceptorRegistration reg1 = registry.addInterceptor(topMenuInterceptor);
		//모든 요청주소에 반응하도록 /** 으로 경로를 쳐준다
		reg1.addPathPatterns("/**");
		
		//로그인이 안된 상태에서 접근하면 로그인관련것들 접근하지 못하게 처리
		CheckLoginInterceptor checkLoginInterceptor = new CheckLoginInterceptor(loginUserBean);
		InterceptorRegistration reg2 = registry.addInterceptor(checkLoginInterceptor);
		reg2.addPathPatterns("/user/modify","/user/logout", "/board/*");
		reg2.excludePathPatterns("/board/main");
 
		//내글 아닌글에 주소창으로 삭제 수정에 접근시에 발동함
		CheckWriterInterceptor checkWriterInterceptor = new CheckWriterInterceptor(loginUserBean, boardService);
		InterceptorRegistration reg3 = registry.addInterceptor(checkWriterInterceptor);
		reg3.addPathPatterns("/board/modify", "/board/delete");
	}
	
	//밑의 @Beann ReloadableResourceBundleMessageSource를 위해서 설정하는것
	//위에 이미 db를 위한 properties파일을 어노테이션 설정했는데 또 밑에처럼 properties파일을 설정하면 에러 발생함
	//이렇게 2개 이상의 어노테이션을 사용할때는 지금의 @Bean을 선언해주면 2개를 사용할 수 있다.
	@Bean
	public static PropertySourcesPlaceholderConfigurer PropertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	//에러 어노테이션을 위한 설정
	//@Vaild 등을 사용하려면 밑에 메소드를 선언해야 한다.
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
		res.setBasenames("/WEB-INF/properties/error_message");
		return res;
	}
	
	//jsp의 enctype="multipart/form-data" 파일 제출을 위해 정의하는 Bean
	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	
}
