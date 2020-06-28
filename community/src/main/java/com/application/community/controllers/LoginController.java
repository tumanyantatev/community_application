package com.application.community.controllers;

import com.application.community.authentication.AuthenticationResponse;
import com.application.community.authentication.LoginRequest;
import com.application.community.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    //@CrossOrigin("*")
    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = loginService.login(loginRequest.getUsername(),loginRequest.getPassword());
        HttpHeaders headers = new HttpHeaders();
        List<String> headerlist = new ArrayList<>();
        List<String> exposeList = new ArrayList<>();
        headerlist.add("Content-Type");
        headerlist.add(" Accept");
        headerlist.add("X-Requested-With");
        headerlist.add("Authorization");
        headers.setAccessControlAllowHeaders(headerlist);
        exposeList.add("Authorization");
        headers.setAccessControlExposeHeaders(exposeList);
        headers.set("Authorization", token);
        return new ResponseEntity<>(new AuthenticationResponse(token), headers, HttpStatus.CREATED);
    }

    //@CrossOrigin("*")
    @PostMapping("/signout")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> logout (@RequestHeader(value="Authorization") String token) {
        HttpHeaders headers = new HttpHeaders();
        if (loginService.logout(token)) {
            headers.remove("Authorization");
            return new ResponseEntity<>(new AuthenticationResponse("logged out"), headers, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new AuthenticationResponse("Logout Failed"), headers, HttpStatus.NOT_MODIFIED);
    }

}
