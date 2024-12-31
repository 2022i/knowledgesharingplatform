package com.back.index;

import com.back.dto.Message;
import com.back.dto.ViewArticle;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(indexName = "user_data")
public class UserData {
    @Id
    private int id;
    @Field(type = FieldType.Text, analyzer = "smartcn", searchAnalyzer = "smartcn")
    private String username;
    private List<Integer> writeArticleIds =new ArrayList<>();
    private List<Integer> supportArticleIds =new ArrayList<>();
    private List<Integer> opposeArticleIds =new ArrayList<>();
    private List<Integer> collectArticleId=new ArrayList<>();
    private List<Integer> shareArticleId=new ArrayList<>();
    private List<ViewArticle> viewArticleList=new ArrayList<>();
    private List<Integer> fansId=new ArrayList<>();
    private List<Integer> subscriptionThemeId=new ArrayList<>();
    private List<Integer> followUserId=new ArrayList<>();
    private List<Integer> commentId=new ArrayList<>();
    private List<Message> Message=new ArrayList<>();
}
