//package com.back.get.UserIdsList;
//
//import com.back.repository.CommentRepository;
//import jakarta.annotation.Resource;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class RootCommentUserIdsList extends UserIdsList {
//    @Resource
//    private CommentRepository commentRepository;
//    @Override
//    public List<Integer> getIdsList(int articleId) {
//        List<Integer> rootCommentUserIds = new ArrayList<>();
//        for(int i=0;i<commentRepository.findCommentsByArticleId(articleId).size();i++){
//            if(commentRepository.findCommentsByArticleId(articleId).get(i).isRoot()){
//                rootCommentUserIds.add(commentRepository.findCommentsByArticleId(articleId).get(i).getUserId());
//            }
//        }
//        return rootCommentUserIds;
//    }
//}
