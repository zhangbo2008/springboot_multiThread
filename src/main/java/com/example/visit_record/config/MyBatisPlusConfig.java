package com.example.visit_record.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.ResourceServlet;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration //标注它是一个配置类，类似于新建一个spring的xml配置文件
public class MyBatisPlusConfig {

    /**
     * Druid数据源配置
     * @return
     */
    @Bean("DataSource") //标注这是一个Bean对象,并取一个特有的名字，避免冲突
    @Primary //如果在IOC容器中找到相同类型的Bean对象，则优先使用这个
    @ConfigurationProperties(prefix = "spring.datasource.druid") //读取核心配置文件application.properties中前缀是spring.datasource.druid的数据注入到当前Bean中
    //注意配置文件中的key值需要和DataSource中对应的set方法名称一致，注意不是属性值，是set方法
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }
//druid 是阿里数据源控制
    /**
     * 注册一个Servlet ,把Druid提供的监控Servlet注册进去，并提供一个访问路径,用户名和密码
     * 当前自定义Servlet的注册方式一致，你也可以在web.xml中配置，只是SpringBoot项目中不建议这么做
     * @return
     */




    @Bean
    public ServletRegistrationBean druidStatViewServlet(){
        //监控界面Servlet的访问设置，访问路劲为根目录下的/druid/**,Druid数据源提供了一套显示页面，StatViewServlet，只需要注入即可，
        ServletRegistrationBean servletRegistration = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        //添加Servlet的初始值，访问这个监控界面的用户名和密码，如果不配置，则默认不需要密码，不显示登录界面
        servletRegistration.addInitParameter(ResourceServlet.PARAM_NAME_USERNAME,"admin");
        servletRegistration.addInitParameter(ResourceServlet.PARAM_NAME_PASSWORD,"admin");
        return servletRegistration;
    }

    /**
     * 过滤器注册，需要配置Druid监控器需要监控的请求和操作
     * 配置一下过滤规则，让静态资源和它自己的视图界面不拦截
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter(){
        //那些信息要监控，需要定义该过滤器来进行拦截，Druid是数据源，当然只拦截请求操作了，静态资源需要放行
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean(new WebStatFilter());
        //过滤器拦截路径
        filterRegistration.addUrlPatterns("/*");
        //不拦截的请求
        filterRegistration.addInitParameter(WebStatFilter.PARAM_NAME_EXCLUSIONS,"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistration;
    }

    /**
     * MyBatisPlus分页插件启用，比较简单，只需要实例化即可
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

}
