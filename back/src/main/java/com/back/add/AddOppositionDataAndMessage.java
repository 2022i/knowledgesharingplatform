package com.back.add;

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

    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> oppositionUserIds = opposeUserIdsList.getIdsList(articleId);
        oppositionUserIds.add(userId);
        article.setOpposeUserIds(oppositionUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> oppositionArticleId = opposeArticleIdsList.getIdsList(userId);
        oppositionArticleId.add(articleId);
        userData.setOpposeArticleIds(oppositionArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentMessage.sentMessage(articleId,messageGenerator);
    }
}
