package com.back.update;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.back.dto.Response;
import com.back.dto.article.ArticleFromFront;
import com.back.get.AiForArticle;
import com.back.write.WriteArticle;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UpdateArticle {
    @Resource
    private WriteArticle writeArticle;
    @Resource
    private AiForArticle aiForArticle;


    public Response updateArticle(ArticleFromFront articleFromFront, String content,String title) throws NoApiKeyException, InputRequiredException {
        articleFromFront.setContent(content);
        articleFromFront.setSummary(aiForArticle.getSummary(content).toString());
        articleFromFront.setRelatedKnowledge(aiForArticle.generateTags(content));
        articleFromFront.setTitle(title);
        writeArticle.writeArticle(articleFromFront);
        return Response.successWriteArticle();
    }
}
