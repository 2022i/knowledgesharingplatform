package com.back.get.ArtilcesList;

import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PublishingArticlesList extends WriteArticlesList{
    @Resource
    private ArticleRepository articleRepository;
    public List<Article> getPublishingArticlesList(int userId){
        return articleRepository.findArticlesByAuthorIdAndCheckAndRejectAndDelete(userId,false,false,false);
    }
}
