package com.back.update;

import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;

public abstract class UpdateData {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDataRepository;
    abstract void updateArticleData(int articleId, int userId);
    abstract void updateUserData(int articleId, int userId);
}
