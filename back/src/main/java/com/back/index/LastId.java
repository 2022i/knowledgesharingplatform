package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "last_id")
public class LastId {
    @Id
    private int id;
    private int userId ;
    private int commentId ;
    private int articleId ;
    private int messageId ;
    private int viewArticleId ;
}
