package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@Document(indexName = "related_knowledge")
public class RelatedKnowledge {
    @Field(type= FieldType.Keyword)
    private int id;
    private List<String> relatedKnowledge;
}
