package com.amirali.memestream.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/secret")
    public ResponseEntity<String> secret() {
        return ResponseEntity.ok("Very secret endpoint, only admins can access");
    }
}
