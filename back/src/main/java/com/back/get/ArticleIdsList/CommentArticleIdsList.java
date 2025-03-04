package com.back.get.ArticleIdsList;

import com.back.index.Comment;
import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentArticleIdsList extends ArticleIdsList{
    @Resource
    private CommentRepository commentRepository;
    @Override
    public List<Integer> getIdsList(int userId) {
        List<Integer> commentArticleIds = new ArrayList<>();
        List<Comment> comments=commentRepository.findCommentsByUserId(userId);
        for (Comment comment : comments) {
            commentArticleIds.add(comment.getArticleId());
        }
         return removeDuplicate(commentArticleIds);
    }
    private List<Integer> removeDuplicate(List<Integer> commentArticleIds) {
        List<Integer> result = new ArrayList<>();
        for (Integer commentArticleId : commentArticleIds) {
            if (!result.contains(commentArticleId)) {
                result.add(commentArticleId);
            }
        }
        return result;
    }
}
