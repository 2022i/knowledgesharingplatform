package com.back.controller.authorActionController;

import com.back.dto.Response;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("")
public class DirectDeletionController {
    @Resource
    private ArticleRepository articleRepository;
    @Description("用户删除发布待审核的文章")
    @DeleteMapping("/directDelete")
    public Response deleteArticle(@RequestParam int articleId){
        articleRepository.deleteById(articleId);
        return Response.successDeleteArticle();
    }
}
