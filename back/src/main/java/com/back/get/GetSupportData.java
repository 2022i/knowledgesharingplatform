package com.back.get;

import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetSupportData extends GetList {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDataRepository;
    @Override
    //获取用户点赞文章ID列表
    public List<Integer> getArticleIDList(int userId){
        UserData userData = userDataRepository.findUserDataById(userId);
        return userData.getSupportArticleId();
    }

    //获取点赞文章列表
    public List<Article> getArticleList(int userId){
        UserData userData= userDataRepository.findUserDataById(userId);
        List<Integer> writeArticleId = userData.getWriteArticleId();
        List<Article> articleData=new ArrayList<>();
        for (Integer integer : writeArticleId) {
            articleData.add(articleRepository.findArticleById(integer));
        }
        return articleData;
    }

}
