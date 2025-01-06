package com.back.index;

import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ArticleTest {
    @Resource
    private ArticleRepository articleRepository;
    private List<Integer> count =new ArrayList<>();
    @Test
    public void saveMultipleArticles() {
        count.add(1);
        count.add(2);
        List<Article> articles = IntStream.range(1, 101)
                .mapToObj(i -> {
                    Article article = new Article();
                    article.setId(i);
                    article.setAuthorId(i);
                    article.setThemeId(i);
                    article.setSupportUserIds(count);
                    article.setTitle("Test Title " + i);
                    article.setContent("在日前召开的“博物馆教育与家庭文明建设——‘家风故事汇’项目”学术研讨会上，全国百家博物馆联合发起新时代家庭文明建设倡议，呼吁发挥博物馆教育优势，萃取中华文化宝库精华，共建开放融合的家庭教育生态，深挖馆藏文物价值，助力建设好家庭、涵养好家教、传承好家风，做家庭文明的传播者、推动者、赋能者。\n" +
                            "研讨会由中国博物馆协会指导，全国妇联家庭和儿童工作部、中国妇女儿童博物馆主办。来自文博、传播、教育、志愿服务等领域的专家学者围绕“博物馆资源在家庭文明建设中的创新应用”“激活博物馆资源助力‘爱心妈妈’关爱服务”“壮大博物馆教育中的志愿服务力量”等主题进行分享，共同深化了博物馆在家庭文明建设中发挥独特作用的认识，也为博物馆创新实践提供了多元思路和可行方向。\n" +
                            "据介绍，“家家幸福安康工程”启动实施以来，中国妇女儿童博物馆积极参与，深入挖掘馆藏资源，自2019年起精心打造“家风故事汇”融媒体品牌项目，推出融合话剧、出版物、有声作品、系列动画、文创等一系列产品，以有形有感的博物馆教育，传播中华优秀家风文化。 " + i);
                    return article;
                })
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);
    }
}