package com.back.get.ArtilcesList;

import com.back.get.ArticleIdsList.WriteArticleIdsList;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WriteArticlesList extends ArticlesList{
    @Resource
    private WriteArticleIdsList writeArticleIdsList;
    @Override
    public List<Article> getArticlesList(int userId){
        List<Integer> writeArticleIds = writeArticleIdsList.getIdsList(userId);
        List<Article> articles=new ArrayList<>();
        for (Integer id : writeArticleIds) {
            articles.add(articlerepository.findArticleById(id));
        }
        return articles;
    }
}
