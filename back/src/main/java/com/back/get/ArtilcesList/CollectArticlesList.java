package com.back.get.ArtilcesList;

import com.back.get.ArticleIdsList.CollectArticleIdsList;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectArticlesList extends ArticlesList {
    @Resource
    private CollectArticleIdsList collectArticleIdsList;
    //获取用户收藏文章ID列表
    @Override
    public List<Article> getArticlesList(int userId){
        List<Integer> collectArticleIds =collectArticleIdsList.getIdsList(userId);
        return getArticlesList(collectArticleIds);
    }
}
