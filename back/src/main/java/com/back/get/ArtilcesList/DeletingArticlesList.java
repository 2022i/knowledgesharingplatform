package com.back.get.ArtilcesList;

import com.back.index.Article;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeletingArticlesList extends WriteArticlesList{
    public List<Article> getDeletingArticlesList(int userId){
        List<Article> articlesList = getArticlesList(userId);
        List<Article> result = new ArrayList<>();
        for(Article article:articlesList){
            if(article.isDelete()){
                result.add(article);
            }
        }
        return result;
    }
}
