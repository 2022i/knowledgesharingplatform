package com.back.get;

import com.back.dto.article.RenderedViewArticle;
import com.back.get.preparation.ViewArticlePreparation;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ViewHistory {
    @Resource
    private ViewArticlePreparation viewArticlePreparation;
    public List<RenderedViewArticle> getViewArticle(int userId){
        return viewArticlePreparation.getRenderedViewArticles(userId);
    }
}
