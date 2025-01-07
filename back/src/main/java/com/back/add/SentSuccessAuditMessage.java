package com.back.add;

import org.springframework.stereotype.Service;

@Service
public class SentSuccessAuditMessage extends SentMessage{
    @Override
    protected void setMessageTitle() {
        message.setTitle("SuccessAudit");
    }
}
