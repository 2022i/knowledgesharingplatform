package com.back.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class ArticleAuthor {
    @Field(type= FieldType.Keyword)
    private int id;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String username;
}
