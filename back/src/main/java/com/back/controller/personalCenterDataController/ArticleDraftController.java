package com.back.controller.personalCenterDataController;

import com.back.dto.article.ArticleDraft;
import com.back.get.ArtilcesList.ArticleDraftsList;
import com.back.get.preparation.ArticleDraftPreparation;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/personalCenterData")
public class ArticleDraftController {
    @Resource
    private ArticleDraftsList articleDraftsList;
    @Resource
    private ArticleDraftPreparation articleDraftPreparation;
    @Description("个人中心接口，获取用户的草稿列表")
    @GetMapping("/getDrafts")
    public List<ArticleDraft> getDrafts(@RequestParam int userId) {
        return articleDraftPreparation.getArticleDrafts( articleDraftsList.getDrafts(userId));
    }
}
