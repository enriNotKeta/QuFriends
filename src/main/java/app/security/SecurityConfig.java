package app.security;

import app.model.User;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:data.properties")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CustomLoginSuccessHandler sucessHandler;


    @Autowired
    private DataSource dataSource;

    @Value("${usersQuery}")
    private String usersQuery;

    @Value("${rolesQuery}")
    private String rolesQuery;



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.eraseCredentials(false);
        auth.userDetailsService(userService);
        auth.jdbcAuthentication().usersByUsernameQuery(usersQuery).
                authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/comment").permitAll()
                .antMatchers("/home").permitAll()
                .antMatchers("/matches").hasAnyAuthority("USER")
                .antMatchers("/api/comment/**").permitAll()
                .antMatchers("/api/story/**").permitAll()
                .antMatchers("/forgotPassword").permitAll()
                .antMatchers("/confirm-reset").permitAll()
                .antMatchers("/reset").permitAll()
                .antMatchers("/resetPassword").permitAll()
                .antMatchers("/vote").hasAnyAuthority("USER")
                .antMatchers("/teams").hasAnyAuthority("USER")
                .antMatchers("/team/").hasAnyAuthority("USER")
                .antMatchers("/adminHome").hasAnyAuthority("ADMIN")
                .antMatchers("/addAdmin").hasAnyAuthority("ADMIN")
                .antMatchers("/register").permitAll()
                .antMatchers("/edit/**").hasAnyAuthority("ADMIN")
                .antMatchers("/home/**").hasAnyAuthority("USER","ADMIN")
                .antMatchers("/settings/**").hasAnyAuthority("ADMIN")
                .antMatchers("/settings/changePassword/**").hasAnyAuthority("ADMIN")
                .antMatchers("/changePassword/**").hasAnyAuthority("ADMIN")
                .antMatchers("/user-settings/**").hasAnyAuthority("USER")
                .antMatchers("/user-settings/changePassword/**").hasAnyAuthority("USER")
                .anyRequest().authenticated()
                .and()
                // form login
                .csrf().disable().formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .successHandler(sucessHandler)
                .usernameParameter("email")
                .passwordParameter("password")
                .and()
                // logout
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

}