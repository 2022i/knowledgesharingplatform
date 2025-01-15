package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDateTime;

@Data
@Document(indexName = "message")
public class Message {
    @Id
    private int id;
    @Field(type = FieldType.Keyword)
    private String title;
    @Field(type= FieldType.Date, format = {},pattern = "uuuu-MM-dd'T'HH:mm:ss")
    private LocalDateTime createTime;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String content;
    private int messageGeneratorId;
    private String messageGeneratorName;
    private int messageRecipientId;
    private int articleId;
    private String articleTitle;
    private boolean read =false;
}
