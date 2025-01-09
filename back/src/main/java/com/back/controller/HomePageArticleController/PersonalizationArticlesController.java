package com.back.controller.HomePageArticleController;

import com.back.dto.Response;
import com.back.get.HomePageArticle.PersonalizationArticleForUser;
import com.back.get.HomePageArticle.PersonalizationArticleForVisitor;
import com.back.get.HomePageArticle.PersonalizationArticleForVisitor;
import jakarta.annotation.Resource;
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

    @GetMapping("/barrier-articles-visitor")
    public Response getBarrierArticles(@RequestParam List<Integer> ids){
        personalizationArticleforVisitor.getBarrierArticles(ids);
        return Response.successAddDataAndSentMessage();
    }

    @GetMapping("/recommended-articles-visitor")
    public Response getRecommendedArticles(@RequestParam List<Integer> ids) {
        personalizationArticleforVisitor.getRecommendedArticles(ids);
        return Response.successAddDataAndSentMessage();
    }

    @GetMapping("/barrier-articles-user")
    public Response getBarrierArticles(@RequestParam int userId) {
        personalizationArticleforUser.getBarrierArticles(userId);
        return Response.successAddDataAndSentMessage();
    }

    @GetMapping("/recommended-articles-user")
    public Response getRecommendedArticles(@RequestParam int userId) {
        personalizationArticleforUser.getRecommendedArticles(userId);
        return Response.successAddDataAndSentMessage();
    }
}
