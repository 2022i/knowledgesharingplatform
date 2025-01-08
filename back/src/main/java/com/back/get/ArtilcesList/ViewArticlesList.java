package com.back.get.ArtilcesList;

import com.back.get.ArticleIdsList.ViewArticleIdsList;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ViewArticlesList extends ArticlesList{
    @Resource
    private ViewArticleIdsList viewArticleIdsList;
    @Override
    public List<Article> getArticlesList(int userId){
        List<Integer> viewArticleIds = viewArticleIdsList.getIdsList(userId);
        return getArticlesList(viewArticleIds);
    }
}
