package com.back.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
public class Message {
    //support, opposition, collection, share, comment
    private String title;
    @Field(type= FieldType.Date,format= DateFormat.basic_date_time)
    private LocalDate generationTime;
    private int messageGeneratorId;
    private int articleId;
    private boolean isRead;
}
