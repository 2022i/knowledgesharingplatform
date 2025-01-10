package com.back.get.HomePageArticle;

import com.back.index.Article;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowArticle {
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    private ArticleRepository articleRepository;

    public List<Article> getFollowArticles(int userId){
        //获取用户关注的用户列表
        List<Integer> followList = userDataRepository.findUserDataById(userId).getFollowUserId();
        List<Integer> followerArticleId = new ArrayList<>();
        List<Article> followArticles = new ArrayList<>();
        for(int id :followList){
            followerArticleId.addAll(userDataRepository.findUserDataById(id).getWriteArticleIds());
        }
        for(int id :followerArticleId){
            Article article = articleRepository.findArticleById(id);
            if (!followArticles.contains(article)) {
                followArticles.add(article);
            }
        }
        //获取用户关注的主题列表
        List<Integer> subscriptionThemeId = userDataRepository.findUserDataById(userId).getSubscriptionThemeId();
        for (int id : subscriptionThemeId) {
            List<Article> articles = articleRepository.findArticleByThemeId(id);
            for (Article article : articles) {
                if (!followArticles.contains(article)) {
                    followArticles.add(article);
                }
            }
        }
        return followArticles;
    }
}
