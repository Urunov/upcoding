package com.urunov.configuration;

import com.urunov.authentication.JsonUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;

/**
 * User: hamdamboy
 * Project: TinyURL
 * Github: @urunov
 */
@Configuration
@EnableWebSecurity
@PropertySource({"classpath:/configurations/cassandra.properties"})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    OAuth2ClientContext oauth2ClientContext;
    @Autowired
    AuthorizationCodeResourceDetails authorizationCodeResourceDetails;

    @Autowired
    ResourceServerProperties resourceServerProperties;

    @Autowired
    OAuth2ClientAuthenticationProcessingFilter oAuth2Filter;

    @Bean
    public PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean customApiAuthenticationFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(customApiAuthenticationFilter());
        registration.setEnabled(true);
        return registration;
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter customApiAuthenticationFilter() {
        return new JsonUsernamePasswordAuthenticationFilter();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/api/**").permitAll()
                .antMatchers("/js/**","/css/**").permitAll()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(
                        customApiAuthenticationFilter() ,  BasicAuthenticationFilter.class)
                .formLogin()
                .loginPage("/api/login")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/api/")
                .permitAll();


		/*http.csrf().disable()
		.authorizeRequests().antMatchers("/api/**", "/css/**","/js/**","/views/**").permitAll()
		.anyRequest().fullyAuthenticated()
		.and()
		.formLogin()
    	.loginPage("/api/login").defaultSuccessUrl("/user/home",true)
    		.permitAll()
		.and()
		.logout()
		.logoutSuccessUrl("/api/")//
			.permitAll()
		.and()
			.antMatcher("/api/login").addFilterBefore(
				customApiAuthenticationFilter() ,  BasicAuthenticationFilter.class);
		http.httpBasic();*/

		/*.and()//
			// Setting the filter for the URL "/google/login"
			.addFilterAt(oAuth2Filter, BasicAuthenticationFilter.class)
				.csrf()//
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.and().addFilterAfter(new CustomCorsFilter(), ChannelProcessingFilter.class);*/
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        AuthenticationProvider authProvider = null;
        auth.authenticationProvider(authProvider);
        auth.build();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        AuthenticationProvider authProvider = null;
        return new ProviderManager(Arrays.asList(authProvider));
    }

    @Bean
    public OAuth2ClientAuthenticationProcessingFilter filter() {
        OAuth2ClientAuthenticationProcessingFilter oAuth2Filter = new OAuth2ClientAuthenticationProcessingFilter(
                "/api/google/login");
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(authorizationCodeResourceDetails,
                oauth2ClientContext);
        oAuth2Filter.setRestTemplate(oAuth2RestTemplate);
        oAuth2Filter.setTokenServices(new UserInfoTokenServices(resourceServerProperties.getUserInfoUri(),
                resourceServerProperties.getClientId()));
        return oAuth2Filter;
    }

    @Bean
    public FilterRegistrationBean loggingFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean(oAuth2Filter);
        registration.addUrlPatterns("/api/google/login");
        return registration;
    }

}
