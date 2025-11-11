package com.monitoramento.saude.security;

import com.monitoramento.saude.dto.AuthenticationDTO;
import com.monitoramento.saude.dto.LoginResponseDTO;
import com.monitoramento.saude.dto.RegisterDTO;
import com.monitoramento.saude.model.Usuario;
import com.monitoramento.saude.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        if (this.usuarioRepository.findByEmail(data.login()) == null) {
            return ResponseEntity.status(404).body("Acesso negado, usuário ou senha incorretos!");
        }

        try {
            var user = usuarioRepository.findByEmail(data.login());
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
            var userLogado = usuarioRepository.findByEmailIgnoreCase(data.login());
            var auth = authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(user.getUsername(), token, userLogado.getUsuario_id()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Acesso negado, usuário ou senha incorretos!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (this.usuarioRepository.findByEmail(data.login()) != null) {
            return ResponseEntity.badRequest().body("Esse e-mail já está cadastrado. Tente logar!");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());

        Usuario newUser = new Usuario(data.nomeCompleto(), data.login(), encryptedPassword, data.regra(), data.dataCriacao());
        this.usuarioRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}