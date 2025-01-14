package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import java.util.List;

@Data
@Document(indexName = "related_knowledge")
public class RelatedKnowledge {
    @Id
    private int id;
    private List<String> relatedKnowledge;
}
