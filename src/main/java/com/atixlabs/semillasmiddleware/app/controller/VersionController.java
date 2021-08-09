package com.atixlabs.semillasmiddleware.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class VersionController {

    @Value("${app.version}")
    private String version;

    @GetMapping("/appversion")
    @ResponseStatus(HttpStatus.OK)
    public String version() {
           return version;
    }
}
