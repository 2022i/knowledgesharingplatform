package com.back.get.CommentsList;

import com.back.dto.RenderedComment;
import com.back.get.RenderedCommentsListGeneration;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCommentedList {
    @Resource
    private RenderedCommentsListGeneration renderedCommentsListGeneration;

    public List<RenderedComment> getUserCommentedList(int commentId) {
            return renderedCommentsListGeneration.getUserRenderingCommentedList(commentId);
    }
}
