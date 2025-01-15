package com.back.add;

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
    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> supportUserIds = supportUserIdsList.getIdsList(articleId);
        if(!supportUserIds.contains(userId)){
            supportUserIds.add(userId);
            article.setSupportUserIds(supportUserIds);
            articleRepository.save(article);
        }
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> supportArticleId = supportArticleIdsList.getIdsList(userId);
        if (!supportArticleId.contains(articleId)){
            supportArticleId.add(articleId);
            userData.setSupportArticleIds(supportArticleId);
            userDataRepository.save(userData);
        }
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentSupportMessage.sentMessage(articleId,messageGenerator);
    }
}