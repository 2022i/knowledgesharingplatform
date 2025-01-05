package com.back.mail.encoding;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

public interface EncodingSelectionService {
    Map<String, PasswordEncoder> setEncryptedMap();
    PasswordEncoder getEncoder();
}
