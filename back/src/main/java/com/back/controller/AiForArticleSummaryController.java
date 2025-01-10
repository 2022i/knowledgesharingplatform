package com.back.controller;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.back.get.AiForArticle;
import jakarta.annotation.Resource;
import jdk.jfr.Description;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class AiForArticleSummaryController {
    @Resource
    private AiForArticle aiForArticle;

    @GetMapping("/summary")
    @Description("使用ai生成文章概要，限制在150字以内")
    public String getSummary(@RequestBody String content){
        try {
            GenerationResult result = aiForArticle.getSummary(content);
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.err.println("错误信息："+e.getMessage());
            System.out.println("请参考文档：https://help.aliyun.com/zh/model-studio/developer-reference/error-code");
        }
        return "获取文章概要失败";
    }

    @GetMapping("/tags")
    @Description("使用AI生成文章关联知识，形如财经、人工智能等")
    public String getTags(@RequestBody String content){
        try {
            GenerationResult result = aiForArticle.generateTags(content);
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            System.err.println("错误信息："+e.getMessage());
            System.out.println("请参考文档：https://help.aliyun.com/zh/model-studio/developer-reference/error-code");
        }
        return "获取文章标签失败";
    }
}
