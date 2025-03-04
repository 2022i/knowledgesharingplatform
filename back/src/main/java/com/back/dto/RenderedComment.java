package com.back.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;

@Data
public class RenderedComment {
    private int id;
    private int fatherId;
    private int userId;
    private String username;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String content;
    private int replyCount;
    private LocalDateTime createTime;
}
