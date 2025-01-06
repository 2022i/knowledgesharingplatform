package com.back.get;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.back.index.Article;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AiForArticleSummary {
    @Resource
    private ArticleRepository articleRepository;
    public GenerationResult getSummary(int articleId) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        String articleContent = articleRepository.findArticleById(articleId).getContent();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(articleContent+"请给出文章概要，直接给出内容即可")
                .build();
        GenerationParam param = GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey("sk-4bbb3c09ffea4106bbf07e0ee66d03b7")
                // 模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .model("qwen-turbo")
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }
}
