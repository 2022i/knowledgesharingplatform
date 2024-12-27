package com.back.update;

import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;

public abstract class AddData {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDataRepository;
    public void updateData(int articleId, int userId){
        updateArticleData( articleId,  userId);
        updateUserData( articleId,  userId);
    }
    abstract void updateArticleData(int articleId, int userId);
    abstract void updateUserData(int articleId, int userId);
}
