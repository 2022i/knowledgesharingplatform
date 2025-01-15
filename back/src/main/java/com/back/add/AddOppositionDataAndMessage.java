package com.back.add;

import com.back.get.ArticleIdsList.SupportArticleIdsList;
import com.back.get.UserIdsList.SupportUserIdsList;
import com.back.sentSystemMessage.SentOpposeMessage;
import com.back.get.ArticleIdsList.OpposeArticleIdsList;
import com.back.get.UserIdsList.OpposeUserIdsList;
import com.back.index.Article;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddOppositionDataAndMessage extends AddDataAndMessage {
    @Resource
    private SentOpposeMessage sentMessage;
    @Resource
    private OpposeUserIdsList opposeUserIdsList;
    @Resource
    private OpposeArticleIdsList opposeArticleIdsList;
    @Resource
    private SupportUserIdsList supportUserIdsList;
    @Resource
    private SupportArticleIdsList supportArticleIdsList;
    private List<Integer> oppositionUserIds;
    private List<Integer> oppositionArticleId;

    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        oppositionUserIds.add(userId);
        article.setOpposeUserIds(oppositionUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        oppositionArticleId.add(articleId);
        userData.setOpposeArticleIds(oppositionArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentMessage.sentMessage(articleId,messageGenerator);
    }
    @Override
    protected boolean isAvailable(int articleId, int userId){
        oppositionUserIds = opposeUserIdsList.getIdsList(articleId);
        oppositionArticleId = opposeArticleIdsList.getIdsList(userId);
        List<Integer> supportArticleId = supportArticleIdsList.getIdsList(userId);
        List<Integer> supportUserIds = supportUserIdsList.getIdsList(articleId);
        return !oppositionUserIds.contains(userId)&&
                !oppositionArticleId.contains(articleId)&&
                !supportArticleId.contains(articleId)&&
                !supportUserIds.contains(userId);
    }
}
