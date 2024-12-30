package com.back.dto;

import com.back.index.UserData;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Data
public class RenderedArticle {
    @Field(type=FieldType.Keyword)
    private int id;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String title;
    @Field(type=FieldType.Keyword)
    private ArticleAuthor Author;
    @Field(type=FieldType.Keyword)
    private String theme;
    @Field(type= FieldType.Date,format= DateFormat.basic_date)
    private String createTime;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String content;
    @Field(type=FieldType.Keyword)
    private int viewUserCount;
    private int supportUserCount;
    private int opposeUserCount;
    private int collectionUserCount ;
    private int shareUserCount;
    private List<String> relatedKnowledge=new ArrayList<>();
}
