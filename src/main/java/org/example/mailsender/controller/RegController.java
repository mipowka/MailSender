package org.example.mailsender.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.mailsender.model.dto.UserRegDto;
import org.example.mailsender.model.entity.User;
import org.example.mailsender.service.RegService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reg")
@RequiredArgsConstructor
@Slf4j
public class RegController {

    private final RegService regService;

    @PostMapping("/reg")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegDto userRegDto){


        if(!regService.isNotActive(userRegDto)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Пользователь с такими данными уже есть");
        }
        log.info(userRegDto.toString());

        regService.sendCodeToEmail(userRegDto);
        return ResponseEntity.ok("Проверяйте почту!");
    }

    @PostMapping("/confirm")
    public ResponseEntity<User> confirm(@RequestParam String code, @RequestParam String email){
        User user = regService.confirmEmail(code, email);
        return ResponseEntity.ok(user);
    }

}
