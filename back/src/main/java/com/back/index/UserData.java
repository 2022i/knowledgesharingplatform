package com.back.index;

import com.back.dto.Message;
import com.back.dto.ViewArticle;
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
    private List<ViewArticle> viewArticleList=new ArrayList<>();
    private List<Integer> fansId=new ArrayList<>();
    private List<Integer> subscriptionThemeId=new ArrayList<>();
    private List<Integer> followUserId=new ArrayList<>();
    private List<Message> Message=new ArrayList<>();
}
