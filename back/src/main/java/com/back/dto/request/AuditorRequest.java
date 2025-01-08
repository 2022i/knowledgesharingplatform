package com.back.dto.request;

import lombok.Data;

@Data
public class AuditorRequest {
    private int articleId;
    private int auditorId;
    private String reason;
}
