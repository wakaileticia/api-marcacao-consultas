package com.fiap.eca.api_marcacao_consultas;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteEncoder {

    @Test
    void testarEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha = "1234";
        String hash = "$2a$10$tAjNEIIGSz1hMW41dIX/j.ZgJ0VpOqYGqmnYUBPcPj2sk4UuAPMj.";

        System.out.println("Senha confere? " + encoder.matches(senha, hash));
    }
}
