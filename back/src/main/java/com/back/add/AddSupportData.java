package com.back.add;

import com.back.index.Article;
import com.back.index.UserData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddSupportData extends AddData {
    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> supportUserIds = article.getSupportUserIds();
        supportUserIds.add(userId);
        article.setSupportUserIds(supportUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> supportArticleId = userData.getSupportArticleId();
        supportArticleId.add(articleId);
        userData.setSupportArticleId(supportArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentMessage=new SentSupportMessage();
        sentMessage.sentMessage(articleId,messageGenerator);
    }
}
