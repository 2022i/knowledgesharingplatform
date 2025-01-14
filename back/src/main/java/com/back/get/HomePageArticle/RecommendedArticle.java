package com.back.get.HomePageArticle;

import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public abstract class RecommendedArticle {
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    private ValidArticle validArticle;
    protected UserData userData;
    private List<Integer> subscribedThemes;
    protected Set<Integer> followedAuthors;
    private List<Article> recommendedArticles;

    public List<Article> getRecommendArticles(int userId){
        init(userId);
        return generateArticles();
    }
    protected List<Article> generateArticles(){
        recommendedArticles = new ArrayList<>();
        int articleCount =articleRepository.findAll().size();
        int randomNum = new Random().nextInt(articleCount)+1;
        int recommendArticlesCount=5;
        while (recommendArticlesCount>0){
            Article article=articleRepository.findArticleById(randomNum);
            if(isInsert(article,userData)){
                recommendedArticles.add(article);
                recommendArticlesCount--;
            }
            randomNum = new Random().nextInt(articleCount)+1;
        }
        // 去重并随机打乱推荐结果
        return recommendedArticles.stream()
                .distinct()
                .limit(5)  // 推荐10篇文章
                .collect(Collectors.toList());
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
    protected boolean isInsert(Article article, UserData userData){
        return validArticle.isArticleValid(article,userData)&&!recommendedArticles.contains(article)&&isRecommend(article);
    }
    private boolean isRecommend(Article article){
        return subscribedThemes.contains(article.getThemeId())||followedAuthors.contains(article.getAuthorId());
    }
}
