package com.back.get.CommentsList;

import com.back.index.Comment;
import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCommentsList {
    @Resource
    private CommentRepository commentRepository;

    public List<Comment> getCommentsList(int articleId) {
        return commentRepository.findCommentsByArticleId(articleId);
    }
}