package com.back.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ArticleServiceTest {
    @Resource
    ArticleService articleService;
    @Test
    public void testFindByTitle() {
        System.out.println(articleService.findByTitle("哲学"));
    }
//    @Test
//    public void testFindByContent() {
//        System.out.println(articleService.findByContent("东方哲学"));
//    }
}