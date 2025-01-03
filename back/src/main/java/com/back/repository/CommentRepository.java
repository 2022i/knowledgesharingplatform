package com.back.repository;

import com.back.index.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends ElasticsearchRepository<Comment,Integer> {
    List<Comment> findCommentsByArticleId(int articleId);
    List<Comment> findCommentsByUserId(int userId);
    Comment findCommentByArticleId(int articleId);
    Comment findCommentById(int id);
}
