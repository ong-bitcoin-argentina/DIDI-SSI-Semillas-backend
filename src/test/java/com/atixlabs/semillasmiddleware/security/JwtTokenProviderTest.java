package com.atixlabs.semillasmiddleware.security;

import com.atixlabs.semillasmiddleware.security.util.JwtTokenControlUtil;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class JwtTokenProviderTest {

    @InjectMocks
    JwtTokenProvider jwtTokenProvider;

    @Mock
    private DateUtil dateUtil;

    @Mock
    private JwtTokenControlUtil jwtTokenControlUtil;

    @Before
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);

        /*
            I had to assign these attributes by reflection because InjectMock does not allow to assign it by
            @SpringBootTest(properties = { "security.jwt.secret: SuperSecretTestKey", "security.jwtExpirationInMs: 100" })
         */
        ReflectionTestUtils.setField(jwtTokenProvider, "secret", "SuperSecretTestKey");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 604800000);
    }

    @Test
    public void testGenerateTokenOk() throws Exception {
        UserDetails userDetails = new User("user", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                new ArrayList<>());
;
        when(jwtTokenControlUtil.isTokenValid(any())).thenReturn(true);
        Mockito.doReturn(true).when(jwtTokenControlUtil).revoqueToken(anyString());

        String token = jwtTokenProvider.generateToken(userDetails);
        Assert.assertNotNull(token);
        verify(jwtTokenControlUtil,times(1)).setToken(any(),any());
    }

    @Test
    public void testValidateTokenExpired() throws Exception {
        UserDetails userDetails = new User("user", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                new ArrayList<>());

        Boolean result = jwtTokenProvider.validateToken(getTokenExpected(), userDetails);

        Assert.assertFalse(result);
    }

    @Test
    public void testValidateTokenOk() throws Exception {
        UserDetails userDetails = new User("user", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
                new ArrayList<>());

        when(jwtTokenControlUtil.isTokenValid(any())).thenReturn(true);
        Mockito.doReturn(true).when(jwtTokenControlUtil).revoqueToken(anyString());

        String token = jwtTokenProvider.generateToken(userDetails);
        Boolean result = jwtTokenProvider.validateToken(token, userDetails);

        Assert.assertTrue(result);
    }

    /*
        Generate by https://jwt.io/
        header {  "alg": "HS512" }
        payload {    "sub": "user",  "iat": 1580310,  "exp": 2185110 }
        Sig: HMACSHA512(  base64UrlEncode(header) + "." +  base64UrlEncode(payload), SuperSecretTestKey ) secret base64 encoded
        Link (you must set secret in Sig)
        https://jwt.io/#debugger-io?token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTgwMzEwLCJleHAiOjIxODUxMTB9.pn2WZfoV9WItr_DoDDLL6hQEGzqa3sQWApwR-udxpbVRuMyVGdml6VFX4iSNLHnm7bhjkBEYnnzrTH4yXnaf9w
    */
    private  String getTokenExpected(){
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNTgwMzEwLCJleHAiOjIxODUxMTB9.pn2WZfoV9WItr_DoDDLL6hQEGzqa3sQWApwR-udxpbVRuMyVGdml6VFX4iSNLHnm7bhjkBEYnnzrTH4yXnaf9w";

    }
}
