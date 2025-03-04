package com.back.index;

import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.back.get.AiForArticle;
import com.back.get.LastIdOperation;
import com.back.repository.ArticleRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SpringBootTest
public class ArticleSaverTest {

    // 假设Article类和articleRepository已经定义好
    @Resource
     private ArticleRepository articleRepository;
    @Resource
    private AiForArticle aiForArticle;
    @Resource
    private LastIdOperation lastIdOperation;

    @Test
    public void saveArticlesFromFiles() throws IOException, NoApiKeyException, InputRequiredException {
        File folder = new File("E:\\protect7");
        File[] files = folder.listFiles(); // 获取文件夹下的所有文件
        List<Article> articles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".text")) {
                    String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                    // 使用正则表达式匹配<h2>标签内的文本
                    Pattern pattern = Pattern.compile("<h2>(.*?)</h2>", Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(content);
                    String title ;
                    if (matcher.find()) {
                        title = matcher.group(1).trim(); // 获取第一个<h2>标签内的文本
                    } else {
                        title = "Default Title"; // 如果没有<h2>标签，使用默认标题
                    }
                    Article article = new Article();
                    article.setId(lastIdOperation.getArticleId()); // 假设ID是递增的
                    article.setAuthorId(1);
                    int randomNum = 1 + (int)(Math.random() * 16);
                    article.setThemeId(randomNum);
                    article.setCreateTime(LocalDateTime.now());
                    article.setContent(content);
                    article.setSummary(aiForArticle.getSummary(content));
                    article.setRelatedKnowledge(aiForArticle.generateTags(content));
                    article.setTitle(title);
                    articles.add(article);
                }
            }
        }
         articleRepository.saveAll(articles); // 保存文章到数据库
    }
}