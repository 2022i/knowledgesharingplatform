package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "articles")
@Data
public class Article {
    @Id
    private int id;
    private User author;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String theme;
    private List<Integer> relatedKnowledge;
    private String createTime;
    private int viewCount;
    private int supportCount;
    private int opposeCount;
    private int commentCount;
    private int collectCount;
    private int shareCount;
    private double supportRate;
}
