package naucna_centrala.nc;

import naucna_centrala.nc.model.CustomUser;
import naucna_centrala.nc.service.UserService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.authorization.*;
import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;

@Configuration
@SpringBootApplication
public class NcApplication {

	@Autowired
	private IdentityService identityService;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(NcApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init(){
		Group g0 = identityService.newGroup("author");
		g0.setType(Groups.GROUP_TYPE_WORKFLOW);
		identityService.saveGroup(g0);
		Group g1 = identityService.newGroup("reviewer");
		g1.setType(Groups.GROUP_TYPE_WORKFLOW);
		identityService.saveGroup(g1);
		Group g2 = identityService.newGroup("editor");
		g2.setType(Groups.GROUP_TYPE_WORKFLOW);
		identityService.saveGroup(g2);

		User userEditor = identityService.newUser("editor");
		userEditor.setPassword("editor");
		identityService.saveUser(userEditor);

		User userEditor1 = identityService.newUser("editor1");
		userEditor1.setPassword("editor1");
		identityService.saveUser(userEditor1);

		User userEditor2 = identityService.newUser("editor2");
		userEditor2.setPassword("editor2");
		identityService.saveUser(userEditor2);

		User userReviewer1 = identityService.newUser("reviewer1");
		userReviewer1.setPassword("reviewer1");
		identityService.saveUser(userReviewer1);

		User userReviewer2 = identityService.newUser("reviewer2");
		userReviewer2.setPassword("reviewer2");
		identityService.saveUser(userReviewer2);

		User userReviewer3 = identityService.newUser("reviewer3");
		userReviewer3.setPassword("reviewer3");
		identityService.saveUser(userReviewer3);

		identityService.createMembership("editor","editor");
		identityService.createMembership("editor1","editor");
		identityService.createMembership("editor2","editor");
		identityService.createMembership("reviewer1","reviewer");
		identityService.createMembership("reviewer2","reviewer");
		identityService.createMembership("reviewer3","reviewer");

		CustomUser customUser = new CustomUser();
		customUser.setEnabled(true);
		customUser.setIme("Editor");
		customUser.setPrezime("Editor");
		customUser.setKorisnicko_ime("editor");
		userService.save(customUser);

		CustomUser customUser1 = new CustomUser();
		customUser1.setEnabled(true);
		customUser1.setIme("Editor1");
		customUser1.setPrezime("Editor1");
		customUser1.setKorisnicko_ime("editor1");
		userService.save(customUser1);

		CustomUser customUser2 = new CustomUser();
		customUser2.setEnabled(true);
		customUser2.setIme("Editor2");
		customUser2.setPrezime("Editor2");
		customUser2.setKorisnicko_ime("editor2");
		userService.save(customUser2);

		CustomUser reviewer1 = new CustomUser();
		reviewer1.setEnabled(true);
		reviewer1.setIme("Reviewer1");
		reviewer1.setPrezime("Reviewer1");
		reviewer1.setKorisnicko_ime("reviewer1");
		userService.save(reviewer1);

		CustomUser reviewer2 = new CustomUser();
		reviewer2.setEnabled(true);
		reviewer2.setIme("Reviewer2");
		reviewer2.setPrezime("Reviewer2");
		reviewer2.setKorisnicko_ime("reviewer2");
		userService.save(reviewer2);

		CustomUser reviewer3 = new CustomUser();
		reviewer3.setEnabled(true);
		reviewer3.setIme("Reviewer3");
		reviewer3.setPrezime("Reviewer3");
		reviewer3.setKorisnicko_ime("reviewer3");
		userService.save(reviewer3);


		Authorization authorization1 = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
		authorization1.setGroupId("author");
		authorization1.addPermission(Permissions.CREATE_INSTANCE);
		authorization1.setResource(Resources.PROCESS_DEFINITION);
		authorization1.setResourceId("*");
		authorizationService.saveAuthorization(authorization1);

		Authorization authorization1Process = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
		authorization1Process.setGroupId("author");
		authorization1Process.addPermission(Permissions.CREATE);
		authorization1Process.addPermission(Permissions.READ);
		authorization1Process.addPermission(Permissions.UPDATE);
		authorization1Process.setResource(Resources.PROCESS_INSTANCE);
		authorizationService.saveAuthorization(authorization1Process);

		Authorization authorization2 = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
		authorization2.setGroupId("reviewer");
		authorization2.addPermission(Permissions.CREATE_INSTANCE);
		authorization2.setResource(Resources.PROCESS_DEFINITION);
		authorization2.setResourceId("*");
		authorizationService.saveAuthorization(authorization2);

		Authorization authorization2Process = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
		authorization2Process.setGroupId("reviewer");
		authorization2Process.addPermission(Permissions.CREATE);
		authorization2Process.addPermission(Permissions.READ);
		authorization2Process.addPermission(Permissions.UPDATE);
		authorization2Process.setResource(Resources.PROCESS_INSTANCE);
		authorizationService.saveAuthorization(authorization2Process);

		AuthorizationEntity userEditorAuth = new AuthorizationEntity(Authorization.AUTH_TYPE_GRANT);
		userEditorAuth.setGroupId("editor");
		userEditorAuth.setResource(Resources.PROCESS_DEFINITION);
		userEditorAuth.setResourceId("*");
		userEditorAuth.addPermission(Permissions.ALL);
		authorizationService.saveAuthorization(userEditorAuth);

		AuthorizationEntity userEditorAuth1 = new AuthorizationEntity(Authorization.AUTH_TYPE_GRANT);
		userEditorAuth1.setGroupId("editor");
		userEditorAuth1.setResource(Resources.PROCESS_INSTANCE);
		userEditorAuth1.setResourceId("*");
		userEditorAuth1.addPermission(Permissions.ALL);
		authorizationService.saveAuthorization(userEditorAuth1);
	}

	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	@Bean
	public FilterRegistrationBean processEngineAuthenticationFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setName("camunda-auth");
		registration.setFilter(getProcessEngineAuthenticationFilter());
		registration.addInitParameter("authentication-provider", "org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider");
		registration.addUrlPatterns("/process/*");
		return registration;
	}

	@Bean
	public Filter getProcessEngineAuthenticationFilter() { return new ProcessEngineAuthenticationFilter();}
}
