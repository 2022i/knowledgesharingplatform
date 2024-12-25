package com.back.index;

import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public  class ArticleTest {
    @Resource
    ArticleRepository articleRepository;
    @Test
    public void initialArticle() {
        Article article = new Article();
        article.setId("1");
        article.setTitle("东方哲学");
        article.setContent("东方哲学是东方国家（中国、日本、印度，有时包括伊斯兰国家）哲学领域的总称。指区别于在古希腊哲学影响下形成的西方哲学体系，东方哲学不是统一的哲学体系，但有相似的文化特征，中国的儒学带有浓厚的道德倾向，关注人的伦理价值。受到西方哲学影响，百余年来东方哲学都使用西方哲学标准来加以分类与论述，因此现今东方哲学无论是中国哲学，还是日本哲学，所有区域性哲学仍可划分形而上学、宇宙论、逻辑学、宗教哲学、社会哲学、政治哲学和文化哲学等种类。");
        Article article1 = new Article();
        article1.setId("2");
        article1.setTitle("西方哲学");
        article1.setContent("西方哲学是指古希腊哲学以来，欧洲和美洲的哲学。西方哲学的起源可以追溯到古希腊，古希腊哲学家们提出了许多关于宇宙、人类、自然、道德、政治等方面的问题，形成了许多重要的哲学思想。西方哲学的发展经历了古代哲学、中世纪哲学、近代哲学和现代哲学四个阶段。西方哲学的主要流派有理性主义、经验主义、实证主义、实在主义、现象学、存在主义、分析哲学、后现代主义等。");
        Article article2 = new Article();
        article2.setId("3");
        article2.setTitle("印度哲学");
        article2.setContent("印度哲学是指印度的哲学思想。印度哲学的起源可以追溯到古代印度，古代印度哲学家们提出了许多关于宇宙、人类、自然、道德、政治等方面的问题，形成了许多重要的哲学思想。印度哲学的发展经历了古代哲学、中世纪哲学、近代哲学和现代哲学四个阶段。印度哲学的主要流派有耆那教、佛教、吠陀哲学、尼亚亚学派、瑜伽学派、密宗、梵学、吠陀学派等。");
        List<Article> articles = List.of(article, article1, article2);
        articleRepository.saveAll(articles);
    }
}