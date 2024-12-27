package com.back.add;

import com.back.index.Article;
import com.back.index.UserData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddShareData extends AddData {
    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> shareUserIds = article.getShareUserIds();
        shareUserIds.add(userId);
        article.setSupportUserIds(shareUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> shareArticleId = userData.getShareArticleId();
        shareArticleId.add(articleId);
        userData.setSupportArticleId(shareArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentMessage=new SentShareMessage();
        sentMessage.sentMessage(articleId,messageGenerator);
    }
}
