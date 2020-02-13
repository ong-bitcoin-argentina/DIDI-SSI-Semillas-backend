package com.atixlabs.semillasmiddleware.filemanager.controller;

import com.atixlabs.semillasmiddleware.exceptionhandler.dto.ApiError;
import com.atixlabs.semillasmiddleware.security.controller.BasicAuthIntegrationTest;
import com.atixlabs.semillasmiddleware.security.dto.JwtRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@Slf4j
public class FileManagerControllerIntegrationTest extends BasicAuthIntegrationTest {

    public final String URL_UPLOAD = "/api/file/upload";

    @Test
    public void fileEmptyException() throws IOException {

        String token = loginAndGetToken(JwtRequest.builder().username("admin").password("password").build());

        Response response  = given()
                .headers("Authorization", "Bearer " + token)
                .multiPart("file","newFile","".getBytes())
                .when().post(URL_UPLOAD);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.as(ApiError.class).getStatus());
        assertEquals("Empty file", response.as(ApiError.class).getMessage());

    }

    private Response uploadFile(String token) throws IOException {
        File newFile = new File("src/test/resources/files/exampleFile.xls");
        return given()
                .headers("Authorization", "Bearer " + token)
                .accept(ContentType.JSON)
                .multiPart(newFile)
                .when().post(URL_UPLOAD);
    }

    @Test
    public void fileUploadOk() throws IOException {

        File uploadFile = new File("/tmp/exampleFile.xls");
        if(uploadFile.exists())
            uploadFile.delete();

        String token = loginAndGetToken(JwtRequest.builder().username("admin").password("password").build());

        Response response  =  this.uploadFile(token);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        uploadFile = new File("/tmp/exampleFile.xls");
        assertEquals(true, uploadFile.exists());

    }

}
