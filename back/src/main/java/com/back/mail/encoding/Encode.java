package com.back.mail.encoding;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class Encode {
    @Resource
    private EncodingSelectionServiceImpl encodingSelectionService;
    public String encrypt(String password) {
        return encodingSelectionService.getEncoder().encode(password);
    }
    public boolean matches(String password, String encodedPassword) {
        return encodingSelectionService.getEncoder().matches(password, encodedPassword);
    }
}
