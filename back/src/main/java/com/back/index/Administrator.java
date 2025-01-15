package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
@Data
@Document(indexName = "administrators")
public class Administrator {
    @Id
    private int id;
    private String username;
    private String password;
}
