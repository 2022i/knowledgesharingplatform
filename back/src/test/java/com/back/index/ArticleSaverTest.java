package com.back.index;

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
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.time.format.DateTimeFormatter;
@SpringBootTest
public class ArticleSaverTest {

    // 假设Article类和articleRepository已经定义好
    @Resource
     private ArticleRepository articleRepository;

    @Test
    public void saveArticlesFromFiles() throws IOException {
        File folder = new File("D:\\Python_study\\protect7");
        File[] files = folder.listFiles(); // 获取文件夹下的所有文件
        List<Article> articles = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".text")) {
                    String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

                    // 使用正则表达式匹配<h2>标签内的文本
                    Pattern pattern = Pattern.compile("<h2>(.*?)</h2>", Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(content);
                    String title = "";
                    if (matcher.find()) {
                        title = matcher.group(1).trim(); // 获取第一个<h2>标签内的文本
                    } else {
                        title = "Default Title"; // 如果没有<h2>标签，使用默认标题
                    }

                    Article article = new Article();
                    article.setId(articles.size() + 1); // 假设ID是递增的
                    article.setAuthorId(1);
                    article.setThemeId(1);
                    article.setCreateTime(LocalDateTime.now());
                    article.setTitle(title);
                    article.setContent(content);

                    articles.add(article);
                }
            }
        }

         articleRepository.saveAll(articles); // 保存文章到数据库
    }
}