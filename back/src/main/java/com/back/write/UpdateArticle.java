package com.back.write;

import com.back.dto.Response;
import com.back.dto.article.ArticleFromFront;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UpdateArticle {
    @Resource
    private SaveData saveData;
    @Resource
    private UpdataDataSet updataDataSet;
    public Response updateArticle(ArticleFromFront articleFromFront) {
        updataDataSet.setDataSet(articleFromFront);
        saveData.saveData(updataDataSet.getArticle(), updataDataSet.getArticle().getAuthorId());
        return Response.successUpdateArticle();
    }
}
