package com.back.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;

@Data
public class RenderingComment {
    private int id;
    private int fatherId;
    private int userId;
    private String username;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String content;
    @Field(type= FieldType.Date, format = {},pattern = "uuuu-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
}
