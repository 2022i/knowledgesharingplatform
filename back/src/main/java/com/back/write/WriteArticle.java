package com.back.write;

import com.alibaba.dashscope.exception.ApiException;
import com.back.dto.article.ArticleFromFront;
import com.back.dto.Response;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class WriteArticle {
    @Resource
    private SaveData saveData;
    @Resource
    private FirstTimeDataSet firstTimeDataSet;
    public Response writeArticle(ArticleFromFront articleFromFront) throws ApiException{
        firstTimeDataSet.setDataSet(articleFromFront);
        saveData.saveData(firstTimeDataSet.getArticle(), firstTimeDataSet.getArticle().getAuthorId());
        return Response.successWriteArticle();
    }
}
