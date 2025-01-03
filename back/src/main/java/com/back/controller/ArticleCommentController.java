//package com.back.controller;
//
//import com.back.dto.RenderingComment;
//import com.back.get.RenderingCommentPreparation;
//import jakarta.annotation.Resource;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/articleComment")
//public class ArticleCommentController {
//    @Resource
//    private RenderingCommentPreparation renderingCommentPreparation;
//    @GetMapping("/getArticleComments")
//    public List<RenderingComment> getComments(@RequestParam int articleId) {
//        return renderingCommentPreparation.getRenderingCommentList(articleId);
//    }
//}
