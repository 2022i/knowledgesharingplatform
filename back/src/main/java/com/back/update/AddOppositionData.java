package com.back.update;

import com.back.index.Article;
import com.back.index.UserData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddOppositionData extends AddData {
    @Override
    public void updateArticleData(int articleId, int userId) {
        Article article = articleRepository.findArticleById(articleId);
        List<Integer> oppositionUserIds = article.getOpposeUserIds();
        oppositionUserIds.add(userId);
        article.setOpposeUserIds(oppositionUserIds);
        articleRepository.save(article);
    }
    public void updateUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> oppositionArticleId = userData.getOpposeArticleId();
        oppositionArticleId.add(articleId);
        userData.setOpposeArticleId(oppositionArticleId);
        userDataRepository.save(userData);
    }
}
