package com.back.controller;

import com.back.dto.Response;
import com.back.add.AddSupportData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddSupportDataControllerTest {

    @Mock
    private AddSupportData updateSupportData;

    @InjectMocks
    private AddSupportDataController addSupportDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addArticleSupportData_success() {
        doNothing().when(updateSupportData).addArticleData(1, 100);

        Response response = addSupportDataController.addArticleSupportData(1, 100);

        assertEquals(Response.success(), response);
        verify(updateSupportData, times(1)).addArticleData(1, 100);
    }

    @Test
    void addArticleSupportData_articleNotFound() {
        doThrow(new NullPointerException()).when(updateSupportData).addArticleData(1, 100);

        assertThrows(NullPointerException.class, () -> addSupportDataController.addArticleSupportData(1, 100));
    }

//    @Test
//    void updateUserSupportData_success() {
//        doNothing().when(updateSupportData).updateUserData(1, 100);
//
//        Response response = updateSupportDataController.updateUserSupportData(1, 100);
//
//        assertEquals(Response.success(), response);
//        verify(updateSupportData, times(1)).updateUserData(1, 100);
//    }
//
//    @Test
//    void updateUserSupportData_userNotFound() {
//        doThrow(new NullPointerException()).when(updateSupportData).updateUserData(1, 100);
//
//        assertThrows(NullPointerException.class, () -> updateSupportDataController.updateUserSupportData(1, 100));
//    }
}