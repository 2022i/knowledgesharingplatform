package com.back.controller;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.back.get.AiForArticle;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class AiForArticleSummaryController {
    @Resource
    private AiForArticle aiForArticle;

    @GetMapping("/summary")
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
