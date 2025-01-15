package com.back.get.HomePageArticle;

import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public abstract class RecommendedArticle {
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    protected ArticleRepository articleRepository;
    protected UserData userData;
    private List<Integer> subscribedThemes;
    protected Set<Integer> followedAuthors;

    public List<Article> getRecommendArticles(int userId){
        init(userId);
        return generateArticles();
    }
    protected List<Article> generateArticles(){
        List<Article> alternativeArticles = new ArrayList<>();
        for(Integer themeId:subscribedThemes){
            alternativeArticles.addAll(articleRepository.findArticlesByThemeIdAndCheckAndRejectAndDelete(themeId, true, false, false));
        }
        for(Integer authorId:followedAuthors){
            alternativeArticles.addAll(articleRepository.findArticlesByAuthorIdAndCheckAndRejectAndDelete(authorId, true, false, false));
        }
        int randomNum = new Random().nextInt(alternativeArticles.size())+1;
        int recommendArticlesCount=8;
        List<Article> recommendedArticles = new ArrayList<>();
        while (recommendArticlesCount>0){
            if(!recommendedArticles.contains(alternativeArticles.get(randomNum))){
                recommendedArticles.add(alternativeArticles.get(randomNum));
                randomNum = new Random().nextInt(alternativeArticles.size())+1;
                recommendArticlesCount--;
            }
        }
        return recommendedArticles;
    }
    abstract void init(int userId);
    abstract void setFollowedAuthors();
    protected void setUserData(int userId){
        userData = userDataRepository.findUserDataById(userId);
    }
    protected void setSubscribedThemes(){
        subscribedThemes = new ArrayList<>();
        subscribedThemes.addAll(userData.getSubscriptionThemeId());
    }
}
