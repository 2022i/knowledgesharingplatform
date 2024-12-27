package com.back.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;


@Data
public class ViewArticle {
    private int articleId;
    @Field(type=FieldType.Date,format=DateFormat.basic_date)
    private LocalDate viewTime;
}

