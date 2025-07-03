package com.sas.sas_backend.utils.password;

public record PasswordHashResponse(String hash, String salt) {}