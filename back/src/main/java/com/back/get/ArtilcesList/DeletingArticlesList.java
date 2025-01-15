package com.back.get.ArtilcesList;

import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeletingArticlesList extends WriteArticlesList{
    @Resource
    private ArticleRepository articleRepository;
    public List<Article> getDeletingArticlesList(int userId){
        return articleRepository.findArticlesByAuthorIdAndDelete(userId,true);
    }
}
