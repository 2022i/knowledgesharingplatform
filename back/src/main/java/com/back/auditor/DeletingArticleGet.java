package com.back.auditor;

import com.back.dto.article.AuditorArticle;
import com.back.get.AuditorArticlePreparation;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeletingArticleGet {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private AuditorArticlePreparation auditorArticlePreparation;
    public List<AuditorArticle> getDeletingArticles() {
        return auditorArticlePreparation.getUnCheckArticles(articleRepository.findArticlesByDelete(true));
    }
}
