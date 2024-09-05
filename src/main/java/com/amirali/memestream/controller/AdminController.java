package com.amirali.memestream.controller;

import com.amirali.memestream.model.ChefRoleRequest;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.service.commandhandlers.AddRoleCommandHandler;
import com.amirali.memestream.service.commandhandlers.DeleteRoleCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AddRoleCommandHandler addRoleCommandHandler;

    private final DeleteRoleCommandHandler deleteRoleCommandHandler;

    @PostMapping("/add-role")
    public ResponseEntity<SimpleResponse> addRoleToChef(@RequestBody ChefRoleRequest roleRequest) {
        return addRoleCommandHandler.execute(roleRequest);
    }

    @DeleteMapping("delete-role")
    public ResponseEntity<SimpleResponse> deleteChefRole(@RequestBody ChefRoleRequest roleRequest) {
        return deleteRoleCommandHandler.execute(roleRequest);
    }
}
