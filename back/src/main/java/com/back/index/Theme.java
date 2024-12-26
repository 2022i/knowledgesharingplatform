package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "theme")
public class Theme {
    @Id
    private int id;
    private int FatherId;
    private String name;
}
