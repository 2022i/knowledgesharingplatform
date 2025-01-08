package com.back.get.preparation;

import com.back.dto.PersonalAchievements;
import com.back.get.ArtilcesList.WriteArticlesList;
import com.back.get.UserIdsList.FansIdsList;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonalAchievementsPreparation {
    @Resource
    private WriteArticlesList writeArticlesList;
    @Resource
    private FansIdsList fansIdsList;
    public PersonalAchievements getPersonalAchievements(int userId) {
        PersonalAchievements personalAchievements = new PersonalAchievements();
        personalAchievements.setWriteArticleCount(getWriteArticleCount(userId));
        personalAchievements.setSupportedCount(getSupportedCount(userId));
        personalAchievements.setOpposedCount(getOpposedCount(userId));
        personalAchievements.setSharedCount(getSharedCount(userId));
        personalAchievements.setCollectedCount(getCollectedCount(userId));
        personalAchievements.setFansCount(getFansCount(userId));
        return personalAchievements;
    }
    private int getWriteArticleCount(int userId) {
        return writeArticlesList.getArticlesList(userId).size();
    }
    private int getSupportedCount(int userId) {
        List<Article> articles = writeArticlesList.getArticlesList(userId);
        int count = 0;
        for (Article article : articles) {
            count += article.getSupportUserIds().size();
        }
        return count;
    }
    private int getOpposedCount(int userId) {
        List<Article> articles = writeArticlesList.getArticlesList(userId);
        int count = 0;
        for (Article article : articles) {
            count += article.getOpposeUserIds().size();
        }
        return count;
    }
    private int getSharedCount(int userId) {
        List<Article> articles = writeArticlesList.getArticlesList(userId);
        int count = 0;
        for (Article article : articles) {
            count += article.getShareUserIds().size();
        }
        return count;
    }
    private int getCollectedCount(int userId) {
        List<Article> articles = writeArticlesList.getArticlesList(userId);
        int count = 0;
        for (Article article : articles) {
            count += article.getCollectUserIds().size();
        }
        return count;
    }
    private int getFansCount(int userId) {
        return fansIdsList.getIdsList(userId).size();
    }
}
