package com.back.dto.article;

import com.back.dto.ArticleAuthor;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;
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
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String content;
    private String summary;
    private List<String> relatedKnowledge=new ArrayList<>();
    @Field(type= FieldType.Date, format = {},pattern = "uuuu-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
    @Field(type=FieldType.Keyword)
    private int viewUserCount;
    private int supportUserCount;
    private int opposeUserCount;
    private int commentCount;
    private int collectionUserCount ;
    private int shareUserCount;
}
