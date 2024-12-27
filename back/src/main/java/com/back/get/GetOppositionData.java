package com.back.get;

import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetOppositionData extends GetList{
    @Resource
    protected UserDataRepository userDataRepository;
    @Resource
    protected ArticleRepository articleRepository;

    //获取用户反对文章列表
    public List<Article> getOppositionList(int userId){
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> oppositionArticleId = userData.getOpposeArticleId();
        List<Article> articles = new java.util.ArrayList<>();
        for(Integer id : oppositionArticleId){
            articles.add(articleRepository.findArticleById(id));
        }
        return articles;
        }

        //获取用户反对文章ID列表
    @Override
    public List<Integer> getArticleIDList(int userId){
        return userDataRepository.findUserDataById(userId).getOpposeArticleId();
    }
}
