package com.edu.car.config;

import com.edu.car.bo.AdminDetails;
import com.edu.car.component.JwtAuthenticationTokenFilter;
import com.edu.car.component.RestAuthenticationEntryPoint;
import com.edu.car.component.RestfulAccessDeniedHandler;
import com.edu.car.mapper.CustomerMapper;
import com.edu.car.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity配置
 *
 * @author Administrator
 * @date 2018/12/29 18:28
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomerMapper customerMapper;

    private final RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(CustomerMapper customerMapper, RestfulAccessDeniedHandler restfulAccessDeniedHandler, RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.customerMapper = customerMapper;
        this.restfulAccessDeniedHandler = restfulAccessDeniedHandler;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll();
        http.headers().cacheControl();
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedHandler(restfulAccessDeniedHandler)
                                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            Customer customer = customerMapper.findCustomer(username);
            if (customer == null) {
                customer = customerMapper.findCustomerByDriveNum(username);
            }
            if (customer != null) {
                return new AdminDetails(customer,customerMapper.findRoles(Long.valueOf(customer.getId())));
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }
}
