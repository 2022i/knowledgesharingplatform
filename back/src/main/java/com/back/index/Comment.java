package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;

@Data
@Document(indexName = "comment")
public class Comment {
    @Id
    private int id;
    private int fatherId;
    private int userId;
    private int articleId;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String content;
    @Field(type= FieldType.Date, format = {},pattern = "uuuu-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
}
