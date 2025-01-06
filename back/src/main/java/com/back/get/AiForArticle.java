package com.back.get;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AiForArticle {
    @Resource
    private ArticleRepository articleRepository;

    private static final String API_KEY = "sk-4bbb3c09ffea4106bbf07e0ee66d03b7";
    private static final String MODEL = "qwen-turbo";
    private static final String SYSTEM_MESSAGE = "You are a helpful assistant.";
    private static final String SUMMARY_PROMPT = "请给出文章概要，直接给出内容即可,限制在150字以内";
    private static final String TAGS_PROMPT = "请为文章生成标签，形如食品、生活、财经等,直接给出标签即可";

    public GenerationResult getSummary(String content) throws ApiException, NoApiKeyException, InputRequiredException {
        return generate(content, SUMMARY_PROMPT);
    }

    public GenerationResult generateTags(String content) throws ApiException, NoApiKeyException, InputRequiredException {
        return generate(content, TAGS_PROMPT);
    }

    private GenerationResult generate(String content, String prompt) throws ApiException, NoApiKeyException, InputRequiredException {
        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(SYSTEM_MESSAGE)
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(content + prompt)
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(API_KEY)
                .model(MODEL)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        return gen.call(param);
    }
}
