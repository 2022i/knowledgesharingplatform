package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "comment")
public class Comment {
    //CommentId=String(UserId)+String(CommentId)
    @Id
    private String id;
    private int fatherId;
    private String content;
}
