package com.back.add;

import com.back.get.ArticleIdsList.OpposeArticleIdsList;
import com.back.get.UserIdsList.OpposeUserIdsList;
import com.back.sentSystemMessage.SentSupportMessage;
import com.back.get.ArticleIdsList.SupportArticleIdsList;
import com.back.get.UserIdsList.SupportUserIdsList;
import com.back.index.Article;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddSupportDataAndMessage extends AddDataAndMessage {
    @Resource
    private SentSupportMessage sentSupportMessage;
    @Resource
    private SupportUserIdsList supportUserIdsList;
    @Resource
    private SupportArticleIdsList supportArticleIdsList;
    @Resource
    private OpposeUserIdsList opposeUserIdsList;
    @Resource
    private OpposeArticleIdsList opposeArticleIdsList;
    private List<Integer> supportUserIds;
    private List<Integer> supportArticleId;

    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        supportUserIds.add(userId);
        article.setSupportUserIds(supportUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        supportArticleId.add(articleId);
        userData.setSupportArticleIds(supportArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentSupportMessage.sentMessage(articleId,messageGenerator);
    }
    @Override
    protected boolean isAvailable(int articleId, int userId){
        List<Integer> oppositionUserIds = opposeUserIdsList.getIdsList(articleId);
        List<Integer> oppositionArticleId = opposeArticleIdsList.getIdsList(userId);
        supportArticleId = supportArticleIdsList.getIdsList(userId);
        supportUserIds = supportUserIdsList.getIdsList(articleId);
        return !oppositionUserIds.contains(userId)&&
                !oppositionArticleId.contains(articleId)&&
                !supportArticleId.contains(articleId)&&
                !supportUserIds.contains(userId);
    }
}