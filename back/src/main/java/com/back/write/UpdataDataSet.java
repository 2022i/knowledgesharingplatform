package com.back.write;

import com.back.dto.article.ArticleFromFront;
import org.springframework.stereotype.Service;

@Service
public class UpdataDataSet extends DataSet {
    @Override
    public void setDataSet(ArticleFromFront articleFromFront) {
        setBasicData(articleFromFront);
        setId(articleFromFront.getId());
    }
}
