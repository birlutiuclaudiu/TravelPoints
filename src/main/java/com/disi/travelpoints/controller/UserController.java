package com.disi.travelpoints.controller;

import com.disi.travelpoints.dto.EmailDTO;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.service.UserService;
import com.disi.travelpoints.service.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }


    @GetMapping(path = "/client")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllClients(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(name = "pageSize", defaultValue = "3") int pageSize) {
        Map<String, Object> response = userService.fetchAllClients(pageSize, pageNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/send")
    public void sendEmailToAdmin(@RequestBody EmailDTO emailDTO, Authentication authentication) {
        TravelUser travelUser = (TravelUser) authentication.getPrincipal();
        emailService.sendMessageToAdmin(travelUser.getEmail(), emailDTO.getSubject(), emailDTO.getText());
    }
}
