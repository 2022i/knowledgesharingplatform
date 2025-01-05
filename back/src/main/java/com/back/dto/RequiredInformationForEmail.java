package com.back.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RequiredInformationForEmail {
    private final String FROM = "2383195232@qq.com";
    private String to;
    private String subject;
    private String code;
}
