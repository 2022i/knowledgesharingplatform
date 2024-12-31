package com.back.add;

import com.back.get.ArticleIdsList.ShareArticleIdsList;
import com.back.get.UserIdsList.ShareUserIdsList;
import com.back.index.Article;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddSupportDataAndMessage extends AddDataAndMessage {
    @Resource
    private SentShareMessage sentMessage;
    @Resource
    private ShareUserIdsList shareUserIdsList;
    @Resource
    private ShareArticleIdsList shareArticleIdsList;
    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> supportUserIds = shareUserIdsList.getIdsList(articleId);
        supportUserIds.add(userId);
        article.setSupportUserIds(supportUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> supportArticleId = shareArticleIdsList.getIdsList(userId);
        supportArticleId.add(articleId);
        userData.setSupportArticleIds(supportArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentMessage.sentMessage(articleId,messageGenerator);
    }
}