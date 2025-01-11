package com.back.controller.publishArticleController;

import com.back.dto.Response;
import com.back.dto.article.ArticleFromFront;
import com.back.write.WriteDraft;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/write")
public class WriteDraftController {
    @Resource
    private WriteDraft writeDraft;
    @Description("写草稿，点击保存草稿时调用这个接口，用于将文章存入数据库")
    @PostMapping("/draft")
    public Response writeGraft(@RequestBody ArticleFromFront articleFromFront)  {
        return writeDraft.writeDraft(articleFromFront);
    }
}
