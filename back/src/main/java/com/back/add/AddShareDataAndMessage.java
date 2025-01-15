package com.back.add;

import com.back.sentSystemMessage.SentShareMessage;
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
    private List<Integer> shareUserIds;
    private List<Integer> shareArticleId;

    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        shareUserIds.add(userId);
        article.setShareUserIds(shareUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        shareArticleId.add(articleId);
        userData.setShareArticleId(shareArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentMessage.sentMessage(articleId,messageGenerator);
    }
    @Override
    protected boolean isAvailable(int articleId, int userId){
        shareUserIds = shareUserIdsList.getIdsList(articleId);
        shareArticleId = shareArticleIdsList.getIdsList(userId);
        return true;
    }
}
