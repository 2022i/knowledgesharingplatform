package com.back.add;

import com.back.get.LastIdOperation;
import com.back.dto.Response;
import com.back.index.ViewArticle;
import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import com.back.repository.ViewArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddViewData {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private UserDataRepository userDataRepository;
    @Resource
    private ViewArticleRepository viewArticleRepository;
    @Resource
    private LastIdOperation lastIdOperation;
    private ViewArticle viewArticle=new ViewArticle();
    public Response addViewData(int articleId, int userId) {
        addArticleData(articleId, userId);
        addUserData(articleId, userId);
        return Response.successAddData();
    }
    private void addArticleData(int articleId, int userId){
        Article article= articleRepository.findArticleById(articleId);
        List<Integer> viewUserIds= article.getViewUserIds();
        viewUserIds.add(userId);
        article.setViewUserIds(viewUserIds);
        articleRepository.save(article);
    }
    private void addUserData(int articleId, int userId){
        List<Integer> viewArticleIds =userDataRepository.findUserDataById(userId).getViewArticleIds();
        if(viewArticleRepository.findViewArticleByArticleId(articleId)!=null){
            viewArticle=viewArticleRepository.findViewArticleByArticleId(articleId);
        }else{
            viewArticle.setId(lastIdOperation.getViewArticleId());
            viewArticle.setArticleId(articleId);
            viewArticleIds.add(viewArticle.getId());
        }
        viewArticle.setViewTime(LocalDateTime.now());
        viewArticleRepository.save(viewArticle);
        UserData userData=userDataRepository.findUserDataById(userId);
        userData.setViewArticleIds(viewArticleIds);
        userDataRepository.save(userData);
    }
}
