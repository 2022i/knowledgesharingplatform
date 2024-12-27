package com.back.update;

import com.back.index.Article;
import com.back.index.UserData;
import com.back.repository.ArticleRepository;
import com.back.repository.UserDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UpdateSupportDataTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserDataRepository userDataRepository;

    @InjectMocks
    private AddSupportData updateSupportData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateArticleData_addsUserIdToSupportUserIds() {
        Article article = new Article();
        article.setSupportUserIds(new ArrayList<>());
        when(articleRepository.findArticleById(1)).thenReturn(article);

        updateSupportData.updateArticleData(1, 100);

        assertTrue(article.getSupportUserIds().contains(100));
        verify(articleRepository, times(1)).save(article);
    }

    @Test
    void updateArticleData_articleNotFound() {
        when(articleRepository.findArticleById(1)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> updateSupportData.updateArticleData(1, 100));
    }

    @Test
    void updateUserData_addsArticleIdToSupportArticleId() {
        UserData userData = new UserData();
        userData.setSupportArticleId(new ArrayList<>());
        when(userDataRepository.findUserDataById(100)).thenReturn(userData);

        updateSupportData.updateUserData(1, 100);

        assertTrue(userData.getSupportArticleId().contains(1));
        verify(userDataRepository, times(1)).save(userData);
    }

    @Test
    void updateUserData_userNotFound() {
        when(userDataRepository.findUserDataById(100)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> updateSupportData.updateUserData(1, 100));
    }
}