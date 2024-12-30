package com.back.add;

import com.back.index.Article;
import com.back.index.UserData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddOppositionData extends AddData {
    @Resource
    private SentOpposeMessage sentMessage;
    @Override
    protected void addArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> oppositionUserIds = article.getOpposeUserIds();
        oppositionUserIds.add(userId);
        article.setOpposeUserIds(oppositionUserIds);
        articleRepository.save(article);
    }
    @Override
    protected void addUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> oppositionArticleId = userData.getOpposeArticleId();
        oppositionArticleId.add(articleId);
        userData.setOpposeArticleId(oppositionArticleId);
        userDataRepository.save(userData);
    }
    @Override
    protected void sentMessage(int articleId, int messageGenerator){
        sentMessage.sentMessage(articleId,messageGenerator);
    }
}
