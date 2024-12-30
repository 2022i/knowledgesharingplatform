package com.back.index;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "comment")
public class Comment {
    @Field(type= FieldType.Keyword)
    private int id;
    @Field(type=FieldType.Keyword)
    private int fatherId;
    private String content;
}
