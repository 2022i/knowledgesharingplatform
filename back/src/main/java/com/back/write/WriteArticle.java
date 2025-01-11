package com.back.write;

import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.back.dto.article.ArticleFromFront;
import com.back.get.AiForArticle;
import com.back.get.LastIdOperation;
import com.back.dto.Response;
import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class WriteArticle {
    @Resource
    private ArticleRepository articleRepository;
    @Resource
    private LastIdOperation lastIdOperation;
    @Resource
    private AiForArticle aiForArticle;
    @Resource
    private UserDataRepository userDataRepository;
    public Response writeArticle(ArticleFromFront articleFromFront) throws ApiException, NoApiKeyException, InputRequiredException {
        Article article = new Article();
        article.setId(lastIdOperation.getArticleId());
        article.setAuthorId(articleFromFront.getAuthorId());
        article.setThemeId(articleFromFront.getThemeId());
        article.setTitle(articleFromFront.getTitle());
        article.setContent(articleFromFront.getContent());
        article.setSummary(aiForArticle.getSummary(articleFromFront.getContent()).toString());
        article.setRelatedKnowledge(Collections.singletonList(aiForArticle.generateTags(articleFromFront.getContent()).toString()));
        article.setCreateTime(LocalDateTime.now());
        articleRepository.save(article);
        UserData userData = userDataRepository.findUserDataById(articleFromFront.getAuthorId());
        List<Integer> writeArticleIds= userData.getWriteArticleIds();
        writeArticleIds.add(article.getId());
        userData.setWriteArticleIds(writeArticleIds);
        userDataRepository.save(userData);
        return Response.successWriteArticle();
    }
}
