package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "articles")
@Data
public class Article {
    @Id
    private int id;
    //userId
    private int authorId;
    private int themeId;
    @Field(type=FieldType.Date, format={}, pattern="dd.MM.uuuu")
    private String createTime;
    private int relatedKnowledgeId;
    private List<Integer> collectionUserIds=new ArrayList<>();
    private List<Integer> shareUserIds=new ArrayList<>();
    private List<Integer> supportUserIds=new ArrayList<>();
    private List<Integer> opposeUserIds=new ArrayList<>();
    private List<Integer> viewUserIds=new ArrayList<>();
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String title;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String content;
}
