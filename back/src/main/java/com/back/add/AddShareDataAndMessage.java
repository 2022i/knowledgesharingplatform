package com.back.add;

import com.back.SentSystemMessage.SentShareMessage;
import com.back.get.ArticleIdsList.ShareArticleIdsList;
import com.back.get.UserIdsList.ShareUserIdsList;
import com.back.index.Article;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddShareDataAndMessage extends AddDataAndMessage {
    @Resource
    private SentShareMessage sentMessage;
    @Resource
    private ShareUserIdsList shareUserIdsList;
    @Resource
    private ShareArticleIdsList shareArticleIdsList;

    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> shareUserIds = shareUserIdsList.getIdsList(articleId);
        shareUserIds.add(userId);
        article.setSupportUserIds(shareUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> shareArticleId = shareArticleIdsList.getIdsList(userId);
        shareArticleId.add(articleId);
        userData.setSupportArticleIds(shareArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentMessage.sentMessage(articleId,messageGenerator);
    }
}
