package com.joan.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/hello")
public class HelloWorldController {

    @GetMapping
    public ResponseEntity<String> sayHello() { // no preauthorize, no request matcher => any authenticated person can access this
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/john")
    @PreAuthorize("hasRole('ROLE_EDITOR')")
    public ResponseEntity<String> adminHello() { // use of PreAuthorize => only users with role editor can access this resource
        return ResponseEntity.ok("Hello editor");
    }
}
