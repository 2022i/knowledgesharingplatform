package com.back.write;

import com.back.dto.article.ArticleFromFront;
import com.back.index.Article;
import lombok.Getter;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Getter
@Service
public abstract class DataSet {
    private Article article;
    public abstract void setDataSet(ArticleFromFront articleFromFront);
    protected void setBasicData(ArticleFromFront articleFromFront) {
        article = new Article();
        article.setAuthorId(articleFromFront.getAuthorId());
        article.setThemeId(articleFromFront.getThemeId());
        article.setTitle(articleFromFront.getTitle());
        article.setContent(articleFromFront.getContent());
        article.setSummary(articleFromFront.getSummary());
        article.setRelatedKnowledge(articleFromFront.getRelatedKnowledge());
        article.setCreateTime(LocalDateTime.now());
    }
    protected void setId(int id){
        article.setId(id);
    }
}
