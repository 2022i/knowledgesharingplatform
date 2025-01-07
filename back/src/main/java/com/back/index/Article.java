package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(indexName = "articles")
@Data
public class Article {
    @Id
    private int id;
    //userId
    @Field(type=FieldType.Keyword)
    private int authorId;
    @Field(type=FieldType.Keyword)
    private int themeId;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String title;
    private String summary;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String content;
    @Field(type= FieldType.Date, format = {},pattern = "uuuu-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
    private List<String> relatedKnowledge =new ArrayList<>();
    private List<Integer> collectUserIds =new ArrayList<>();
    private List<Integer> shareUserIds=new ArrayList<>();
    private List<Integer> supportUserIds=new ArrayList<>();
    private List<Integer> opposeUserIds=new ArrayList<>();
    private List<Integer> viewUserIds=new ArrayList<>();
    private boolean check=false;
    private boolean delete=false;
    private boolean draft=false;
    private boolean reject=false;
}
