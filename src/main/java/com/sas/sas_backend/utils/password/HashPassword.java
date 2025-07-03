package com.sas.sas_backend.utils.password;

import org.springframework.security.crypto.bcrypt.BCrypt; // Certifique-se que esta importação está correta.

public class HashPassword {
    public static PasswordHashResponse hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(); // Gera um salt aleatório
        String hash = BCrypt.hashpw(plainPassword, salt); // Hash da senha com o salt
        return new PasswordHashResponse(hash, salt);
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword, String salt) {
        // BCrypt.hashpw com o salt original irá gerar o mesmo hash se a plainPassword for a mesma.
        // É importante que o salt armazenado seja usado aqui.
        return BCrypt.checkpw(plainPassword, hashedPassword); // BCrypt.checkpw já lida com o salt interno ao hashedPassword
    }
}