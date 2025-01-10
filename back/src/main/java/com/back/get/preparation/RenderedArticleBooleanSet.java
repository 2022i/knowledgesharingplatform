//package com.back.get.preparation;
//
//import com.back.dto.article.RenderedArticle;
//import jakarta.annotation.Resource;
//
//import java.util.List;
//
//public class RenderedArticleBooleanSet {
//    @Resource
//    private Wr
//    public List<RenderedArticle> setBoolean(List<RenderedArticle> renderedArticles, int userId){
//        for (RenderedArticle renderedArticle : renderedArticles) {
//            renderedArticle.setIsSupport(isSupport(userId, renderedArticle.getId()));
//            renderedArticle.setIsOppose(isOppose(userId, renderedArticle.getId()));
//            renderedArticle.setIsCollect(isCollect(userId, renderedArticle.getId()));
//        }
//        return renderedArticles;
//    }
//    private
//}
