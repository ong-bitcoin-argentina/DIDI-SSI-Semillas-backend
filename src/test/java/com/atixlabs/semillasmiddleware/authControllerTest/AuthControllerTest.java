package com.atixlabs.semillasmiddleware.authControllerTest;


import com.atixlabs.semillasmiddleware.security.controller.AuthController;
import com.atixlabs.semillasmiddleware.security.dto.JwtRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@ContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class AuthControllerTest {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-docs");

    private MockMvc mockMvc;

    @InjectMocks
    private AuthController usersController;

    @Autowired
    private WebApplicationContext context;


    @Before
    public void setUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }


    protected JwtRequest adminLogin() {
        return JwtRequest.builder().username("admin@atixlabs.com").password("password").build();
    }


    @Test
    public void login() throws Exception {

        //ConstrainedFields fields = new ConstrainedFields(BeerDto.class);
       // String token =
           /*     given().contentType(ContentType.JSON)
                        .body(adminLogin())
                        .when().post("/auth/login")
                        .andReturn().jsonPath().getString("accessToken");
        log.debug("Got token:" + token);
*/

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login").accept(MediaType.APPLICATION_JSON).content(adminLogin().toString()))
                .andDo(document("{class-name}/{method-name}"));



    }
}
