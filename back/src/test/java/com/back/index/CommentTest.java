package com.back.index;

import com.back.repository.CommentRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class CommentTest {
    @Resource
    private CommentRepository commentRepository;
    @Test
    public void articleComments1() {
        for (int i = 1; i <= 10; i++) {
            Comment comment = new Comment();
            comment.setId(i);
            comment.setFatherId(0);
            comment.setUserId(i);
            comment.setArticleId(1);
            comment.setContent("Sample content " + i);
            comment.setCreateTime(LocalDateTime.now()); // 使用相同的格式解析为 LocalDateTime
            System.out.println(LocalDateTime.now());
            commentRepository.save(comment);
        }
    }
    @Test
    public void articleComments2() {
        for (int i = 11; i <= 21; i++) {
            Comment comment = new Comment();
            comment.setId(i);
            comment.setFatherId(i-10);
            comment.setUserId(i);
            comment.setArticleId(1);
            comment.setContent("Sample content " + i);
            comment.setCreateTime(LocalDateTime.now()); // 使用相同的格式解析为 LocalDateTime
            commentRepository.save(comment);
        }
    }
    @Test
    public void articleComments3() {
        for (int i = 21; i <= 31; i++) {
            Comment comment = new Comment();
            comment.setId(i);
            comment.setFatherId(i-10);
            comment.setUserId(i);
            comment.setArticleId(1);
            comment.setContent("Sample content " + i);
            comment.setCreateTime(LocalDateTime.now()); // 使用相同的格式解析为 LocalDateTime
            commentRepository.save(comment);
        }
    }
    @Test
    public void articleComments4() {
        for (int i = 31; i <= 41; i++) {
            Comment comment = new Comment();
            comment.setId(i);
            comment.setFatherId(i-20);
            comment.setUserId(i);
            comment.setArticleId(1);
            comment.setContent("Sample content " + i);
            comment.setCreateTime(LocalDateTime.now()); // 使用相同的格式解析为 LocalDateTime
            commentRepository.save(comment);
        }
    }
}