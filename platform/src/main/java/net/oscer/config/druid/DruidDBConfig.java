package net.oscer.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DruidDBConfig implements EnvironmentAware {

    private Environment env;

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(env.getProperty("spring.datasource.url"));
        datasource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
        datasource.setUsername(env.getProperty("spring.datasource.username"));
        datasource.setPassword(env.getProperty("spring.datasource.password"));
        datasource.setInitialSize(Integer.valueOf(env.getProperty("spring.datasource.initialSize")));
        datasource.setMinIdle(Integer.valueOf(env.getProperty("spring.datasource.minIdle")));
        datasource.setMaxWait(Long.valueOf(env.getProperty("spring.datasource.maxWait")));
        datasource.setMaxActive(Integer.valueOf(env.getProperty("spring.datasource.maxActive")));
        datasource.setMinEvictableIdleTimeMillis(Long.valueOf(env.getProperty("spring.datasource.minEvictableIdleTimeMillis")));
        try {
            datasource.setFilters("stat, wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }

    /**
     * 配置监控服务器
     *
     * @return 返回监控注册的servlet对象
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        servletRegistrationBean.addInitParameter("allow", env.getProperty("spring.druid.allow"));
        servletRegistrationBean.addInitParameter("deny", env.getProperty("spring.druid.deny"));
        servletRegistrationBean.addInitParameter("username", env.getProperty("spring.druid.username"));
        servletRegistrationBean.addInitParameter("password", env.getProperty("spring.druid.password"));
        servletRegistrationBean.addInitParameter("resetEnable", env.getProperty("spring.druid.resetEnable"));
        return servletRegistrationBean;
    }

    /**
     * 配置服务过滤器
     *
     * @return 返回过滤器配置对象
     */
    @Bean
    public FilterRegistrationBean statFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略过滤格式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
        return filterRegistrationBean;
    }
}