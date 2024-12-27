package com.back.index;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(indexName = "user_data")
public class UserData {
    @Id
    private int id;
    private List<Integer> supportArticleId=new ArrayList<>();
    private List<Integer> opposeArticleId=new ArrayList<>();
    private List<Integer> collectArticleId=new ArrayList<>();
    private List<Integer> shareArticleId=new ArrayList<>();
    private List<Integer> writeArticleId=new ArrayList<>();
    private List<Integer> viewArticleId=new ArrayList<>();
}
