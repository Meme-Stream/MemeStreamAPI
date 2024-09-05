package com.amirali.memestream.controller;

import com.amirali.memestream.model.ChefRoleRequest;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.service.commandhandlers.AddRoleCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AddRoleCommandHandler addRoleCommandHandler;

    @PostMapping("/add-role")
    public ResponseEntity<SimpleResponse> addRoleToChef(@RequestBody ChefRoleRequest roleRequest) {
        return addRoleCommandHandler.execute(roleRequest);
    }
}
