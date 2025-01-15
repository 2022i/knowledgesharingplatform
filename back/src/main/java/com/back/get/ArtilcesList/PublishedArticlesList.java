package com.back.get.ArtilcesList;

import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PublishedArticlesList {
    @Resource
    private ArticleRepository articleRepository;
    public List<Article> getPublishedArticlesList(int userId){
        return articleRepository.findArticlesByAuthorIdAndCheckAndRejectAndDeleteAndDraft(userId,true,false,false, false);
    }
}
