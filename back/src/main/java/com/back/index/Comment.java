package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(indexName = "comment")
public class Comment {
    @Id
    private int id;
    private boolean isRoot;
    private int userId;
    private int articleId;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String content;
    private LocalDateTime timestamp;
    private List<Comment> replies=new ArrayList<>();
}
