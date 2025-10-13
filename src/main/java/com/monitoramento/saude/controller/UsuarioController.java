package com.monitoramento.saude.controller;

import java.util.List;
import jakarta.transaction.Transactional;
import com.monitoramento.saude.dto.UserDTO;
import org.springframework.web.bind.annotation.*;
import com.monitoramento.saude.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/")
public class UsuarioController {

    @Autowired
    private UserService service;

    @GetMapping("/usuarios")
    public List<UserDTO> findAllUsers() {
        return service.getUsers();
    }

    @Transactional
    @PostMapping("/cadastros/usuarios/novo")
    public void registerUser(@RequestBody UserDTO dados) {
        service.registerUser(dados);
    }
}
