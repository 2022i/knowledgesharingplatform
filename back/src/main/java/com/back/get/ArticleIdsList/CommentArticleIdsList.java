package com.back.get.ArticleIdsList;

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
        for(int i=0;i<commentRepository.findCommentsByUserId(userId).size();i++){
            commentArticleIds.add(commentRepository.findCommentsByUserId(userId).get(i).getArticleId());
        }
         return commentArticleIds;
    }
}
