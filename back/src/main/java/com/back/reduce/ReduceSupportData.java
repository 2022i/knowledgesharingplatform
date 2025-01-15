package com.back.reduce;

import com.back.index.Article;
import com.back.index.UserData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReduceSupportData extends ReduceData {
    @Override
    void reduceArticleData(int articleId, int userId) {
        Article article=articleRepository.findArticleById(articleId);
        List<Integer> supportUserIds = article.getSupportUserIds();
        if(supportUserIds.contains(userId)){
            supportUserIds.remove((Integer) userId);
            article.setSupportUserIds(supportUserIds);
            articleRepository.save(article);
        }
    }

    @Override
    void reduceUserData(int articleId, int userId) {
        UserData userData = userDataRepository.findUserDataById(userId);
        List<Integer> supportArticleIds = userData.getSupportArticleIds();
        if(supportArticleIds.contains(articleId)){
            supportArticleIds.remove((Integer) articleId);
            userData.setSupportArticleIds(supportArticleIds);
            userDataRepository.save(userData);
        }
    }
}
