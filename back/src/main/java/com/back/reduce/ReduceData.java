package com.back.reduce;

import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public abstract class ReduceData {
    @Resource
    protected ArticleRepository articleRepository;
    @Resource
    protected UserDataRepository userDataRepository;
    public void reduceData(int articleId, int userId){
        reduceArticleData(articleId,userId);
        reduceUserData(articleId,userId);
    }
    abstract void reduceArticleData(int articleId, int userId);
    abstract void reduceUserData(int articleId, int userId);
}
