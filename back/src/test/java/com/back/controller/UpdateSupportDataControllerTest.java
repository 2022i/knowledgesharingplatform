package com.back.controller;

import com.back.dto.Response;
import com.back.update.UpdateSupportData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateSupportDataControllerTest {

    @Mock
    private UpdateSupportData updateSupportData;

    @InjectMocks
    private UpdateSupportDataController updateSupportDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateArticleSupportData_success() {
        doNothing().when(updateSupportData).updateArticleData(1, 100);

        Response response = updateSupportDataController.updateArticleSupportData(1, 100);

        assertEquals(Response.success(), response);
        verify(updateSupportData, times(1)).updateArticleData(1, 100);
    }

    @Test
    void updateArticleSupportData_articleNotFound() {
        doThrow(new NullPointerException()).when(updateSupportData).updateArticleData(1, 100);

        assertThrows(NullPointerException.class, () -> updateSupportDataController.updateArticleSupportData(1, 100));
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