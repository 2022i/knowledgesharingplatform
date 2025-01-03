package com.back.get;

import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RootCommentIdsList implements IdsList {
    @Resource
    private ArticleRepository articleRepository;
    @Override
    public List<Integer> getIdsList(int articleId) {
        return articleRepository.findArticleById(articleId).getCommentIds();
    }
}
