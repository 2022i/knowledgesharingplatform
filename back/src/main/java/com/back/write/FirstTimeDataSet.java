package com.back.write;

import com.back.dto.article.ArticleFromFront;
import com.back.get.LastIdOperation;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FirstTimeDataSet extends DataSet {
    @Resource
    private LastIdOperation lastIdOperation;
    @Override
    public void setDataSet(ArticleFromFront articleFromFront) {
        setBasicData(articleFromFront);
        setId(lastIdOperation.getArticleId());
    }
}
