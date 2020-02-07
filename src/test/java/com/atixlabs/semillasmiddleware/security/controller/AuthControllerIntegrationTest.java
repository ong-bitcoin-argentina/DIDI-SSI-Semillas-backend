package com.atixlabs.semillasmiddleware.security.controller;

import com.atixlabs.semillasmiddleware.security.JwtTokenProvider;
import com.atixlabs.semillasmiddleware.security.dto.JwtRequest;
import com.atixlabs.semillasmiddleware.security.model.Role;
import com.atixlabs.semillasmiddleware.security.model.User;
import com.atixlabs.semillasmiddleware.security.repository.RoleRepository;
import com.atixlabs.semillasmiddleware.security.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public class AuthControllerIntegrationTest {

    public final String URL_AUTH = "/auth/login";
    public final String URL_ISAUTH = "/auth/isauth";
    public final String URL_LOGOUT = "/auth/logout";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @LocalServerPort
    protected int port;

    @Before
    public void prepareUsers() {

//        roleRepository.deleteAll();
        Role roleAdmin = new Role();
        roleAdmin.setCode("ROLE_ADMIN");
        roleAdmin.setDescription("Role Admin");

        roleRepository.save(roleAdmin);

  //      userRepository.deleteAll();
        User admin = new User();
        admin.setActive(true);
        admin.setEmail("admin@semillas.com");
        admin.setName("Admin Name");
        admin.setLastName("Admin Lastname");
        admin.setUsername("admin");
        admin.setPassword("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6");
        admin.setRole(roleAdmin);

        userRepository.save(admin);

    }


    protected String loginAndGetToken(JwtRequest logging) {
        Response response = login(logging);
        return response.andReturn().jsonPath().getString("accessToken");
    }

    protected Response login(JwtRequest logging) {
        Response response  = given()
                .contentType(ContentType.JSON)
                .body(logging)
                .when().post(URL_AUTH);

        return response;
    }

    protected boolean logout(String token) {
        Response response = given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .when().get(URL_LOGOUT);
        return response
                .getStatusCode() == HttpStatus.SC_OK;
    }

    @Test
    public void loginSuccessful() {
        RestAssured.port = this.port;
        Response response = login(JwtRequest.builder().username("admin").password("password").build());

        response.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void loginUnsuccessful() {
        RestAssured.port = this.port;
        login(JwtRequest.builder().username("invalid user").password("no pass").build())
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }


    private Response isAuthCheck(String token) {
        return  given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .when().get(URL_ISAUTH);

    }

    @Test
    public void logoutSuccesfull() {
        RestAssured.port = this.port;
        String token = loginAndGetToken(JwtRequest.builder().username("admin").password("password").build());

        Response response = isAuthCheck(token);
        response.then().assertThat().statusCode(HttpStatus.SC_OK);
        Assert.assertTrue(response.getBody().print().equals("true"));

        Assert.assertTrue(logout(token));

        Response responseOut = isAuthCheck(token);
        responseOut.then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
        Assert.assertFalse(response.getBody().equals(true));


    }
}
