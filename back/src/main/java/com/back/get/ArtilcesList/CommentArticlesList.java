package com.back.get.ArtilcesList;

import com.back.get.ArticleIdsList.CommentArticleIdsList;
import com.back.index.Article;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentArticlesList extends ArticlesList {
    @Resource
    private CommentArticleIdsList commentArticleIdsList;

    @Override
    public List<Article> getArticlesList(int userId) {
        List<Integer> commentArticleIds =commentArticleIdsList.getIdsList(userId);
        return getArticlesList(commentArticleIds);
    }
}
