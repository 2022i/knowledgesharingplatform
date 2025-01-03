//package com.back.get;
//
//import com.back.dto.RenderingComment;
//import com.back.get.UserIdsList.RootCommentUserIdsList;
//import com.back.index.Comment;
//import jakarta.annotation.Resource;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class RenderingCommentPreparation {
//    @Resource
//    private RootCommentUserIdsList rootCommentUserIdsList;
//    public List<RenderingComment> getRenderingCommentList(int articleId){
//        List<Integer> rootCommentIds = rootCommentUserIdsList.getIdsList(articleId);
//        List<RenderingComment> renderingComments = new ArrayList<>();
//        for(Comment comment : comments){
//            renderingComments.add(new RenderingComment(comment));
//        }
//        return renderingComments;
//    }
//}
