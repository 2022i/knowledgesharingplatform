package com.back.controller.publishArticleController;

import com.back.dto.Response;
import com.back.dto.article.ArticleFromFront;
import com.back.write.UpdateArticle;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/write")
@RestController
@CrossOrigin(origins = "*")
public class UpdateArticleController {
    @Resource
    private UpdateArticle updateArticle;
    @PutMapping ("/updateArticle")
    @Description("更新文章，点击最后一个AI流程的确定键时调用这个接口，用于将文章存入数据库")
    public Response updateArticle(@RequestBody ArticleFromFront articleFromFront) {
        return updateArticle.updateArticle(articleFromFront);
    }
}
