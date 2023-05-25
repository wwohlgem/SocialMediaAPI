package com.cooksys.assessment1.controllers;

import com.cooksys.assessment1.services.ValidateService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate")
@RequiredArgsConstructor
public class ValidateController {
    private final ValidateService validateService;


    @GetMapping("/username/exists/@{username}")
    public boolean checkUsernameExists(@PathVariable String username){
        return validateService.checkUsernameExists(username);
    }

}
