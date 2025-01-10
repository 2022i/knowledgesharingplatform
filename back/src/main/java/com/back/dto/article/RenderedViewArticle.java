package com.back.dto.article;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RenderedViewArticle {
    private RenderedArticle article;
    private LocalDateTime viewTime;
}
