package com.back.mail.send;

import com.back.mail.context.AbstractMailContext;
import com.back.redis.RedisCacheServiceImpl;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
/**
 * @author lzz
 */
@Data
@Service
public abstract class AbstractMailSend {
    @Resource
    private RedisCacheServiceImpl redisCacheService;
    @Resource
    protected MessageHelperImp messageHelper;
    protected AbstractMailContext context;

    public void sendMail(String to) {
        try {
            setRequiredInformation(to);
            messageHelper.setMessageHelper();
            setContext();
            messageHelper.setText(context.getContext());
            messageHelper.send();
            redisCacheService.set(to,getCode(),60, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void setContext();

    public String getCode() {
        return messageHelper.getRequiredInformationForEmail().getCode();
    }

    public abstract void setRequiredInformation(String to);
}
