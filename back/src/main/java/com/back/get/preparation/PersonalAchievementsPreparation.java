package com.back.get.preparation;

import com.back.dto.PersonalAchievements;
import com.back.get.ArtilcesList.WriteArticlesList;
import com.back.get.UserIdsList.FansIdsList;
import com.back.index.Article;
import com.back.index.Message;
import com.back.repository.ArticleRepository;
import com.back.repository.MessageRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonalAchievementsPreparation {
    @Resource
    private WriteArticlesList writeArticlesList;
    @Resource
    private FansIdsList fansIdsList;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    private ArticleRepository articleRepository;
    public PersonalAchievements getPersonalAchievements(int userId) {
        PersonalAchievements personalAchievements = new PersonalAchievements();
        personalAchievements.setUserName(getUserName(userId));
        personalAchievements.setWriteArticleCount(getWriteArticleCount(userId));
        personalAchievements.setSupportedCount(getSupportedCount(userId));
        personalAchievements.setOpposedCount(getOpposedCount(userId));
        personalAchievements.setSharedCount(getSharedCount(userId));
        personalAchievements.setCollectedCount(getCollectedCount(userId));
        personalAchievements.setFansCount(getFansCount(userId));
        personalAchievements.setHaveMessage(haveMessage(userId));
        return personalAchievements;
    }
    private List<Article> getPublishedArticles(int userId) {
        return articleRepository.findArticlesByAuthorIdAndCheckAndRejectAndDeleteAndDraft(userId, true, false, false, false);
    }
    private String getUserName(int userId) {
        return userDataRepository.findUserDataById(userId).getUsername();
    }
    private boolean haveMessage(int userId) {
        return !messageRepository.findMessagesByMessageRecipientIdAndRead(userId, false).isEmpty();
    }
    private int getWriteArticleCount(int userId) {
        return articleRepository.findArticlesByAuthorIdAndCheckAndRejectAndDeleteAndDraft(userId, true, false, false, false).size();
    }
    private int getSupportedCount(int userId) {
        List<Article> articles = getPublishedArticles(userId);
        int count = 0;
        if (!articles.isEmpty()) {
            for (Article article : articles) {
                if(article!=null){
                    count +=article.getSupportUserIds().size();
                }
            }
        }
        return count;
    }
    private int getOpposedCount(int userId) {
        List<Article> articles = getPublishedArticles(userId);
        int count = 0;
        if(!articles.isEmpty()){
            for (Article article : articles) {
                count += article.getOpposeUserIds().size();
            }
        }
        return count;
    }
    private int getSharedCount(int userId) {
        List<Article> articles = getPublishedArticles(userId);
        int count = 0;
        if(!articles.isEmpty()) {
            for (Article article : articles) {
                count += article.getShareUserIds().size();
            }
        }
        return count;
    }
    private int getCollectedCount(int userId) {
        List<Article> articles = getPublishedArticles(userId);
        int count = 0;
        if(!articles.isEmpty()) {
            for (Article article : articles) {
                count += article.getCollectUserIds().size();
            }
        }
        return count;
    }
    private int getFansCount(int userId) {
        return fansIdsList.getIdsList(userId).size();
    }
}
