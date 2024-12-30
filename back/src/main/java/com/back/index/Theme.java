package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "theme")
public class Theme {
    @Field(type= FieldType.Keyword)
    private int id;
    @Field(type=FieldType.Keyword)
    private int FatherId;
    @Field(type=FieldType.Keyword)
    private String name;
}
