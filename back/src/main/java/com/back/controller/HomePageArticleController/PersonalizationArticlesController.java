package com.back.controller.HomePageArticleController;

import com.back.dto.Response;
import com.back.get.HomePageArticle.PersonalizationArticleForUser;
import com.back.get.HomePageArticle.PersonalizationArticleForVisitor;
import com.back.get.HomePageArticle.PersonalizationArticleForVisitor;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/getList")
@CrossOrigin(origins = "*")
public class PersonalizationArticlesController {
    @Resource
    private PersonalizationArticleForVisitor personalizationArticleforVisitor;
    @Resource
    private PersonalizationArticleForUser personalizationArticleforUser;

    @GetMapping("/barrierArticlesVisitor")
    @Description("游客身份下推荐信息壁垒文章部分")
    public Response getBarrierArticles(@RequestParam List<Integer> ids){
        personalizationArticleforVisitor.getBarrierArticles(ids);
        return Response.successAddDataAndSentMessage();
    }

    @GetMapping("/recommendedArticlesVisitor")
    @Description("游客身份下获取推荐文章")
    public Response getRecommendedArticles(@RequestParam List<Integer> ids) {
        personalizationArticleforVisitor.getRecommendedArticles(ids);
        return Response.successAddDataAndSentMessage();
    }

    @GetMapping("/barrierArticlesUser")
    @Description("用户身份下获取信息壁垒文章")
    public Response getBarrierArticles(@RequestParam int userId) {
        personalizationArticleforUser.getBarrierArticles(userId);
        return Response.successAddDataAndSentMessage();
    }

    @GetMapping("/recommendedArticlesUser")
    @Description("用户身份下获取推荐文章")
    public Response getRecommendedArticles(@RequestParam int userId) {
        personalizationArticleforUser.getRecommendedArticles(userId);
        return Response.successAddDataAndSentMessage();
    }
}
