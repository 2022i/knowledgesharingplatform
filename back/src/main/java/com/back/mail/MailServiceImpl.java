package com.back.mail;

import com.back.mail.send.MessageHelperImp;
import com.back.redis.RedisCacheServiceImpl;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import java.util.concurrent.TimeUnit;

@Data
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private RedisCacheServiceImpl redisCacheService;
    @Resource
    private MessageHelperImp messageHelper;
    private Context context;

    @Override
    public void sendMail(String to, String subject) {
        try {
            messageHelper.setRequiredInformation(to, subject);
            messageHelper.setMessageHelper();
            setContext(subject);
            messageHelper.setText(context);
            messageHelper.send();
            redisCacheService.set(to,getCode(),65, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mailSendForRegister(String to) {
        sendMail(to,"REGISTER");
    }

    @Override
    public void mailSendForForgetPassword(String to) {
        sendMail( to, "RESET");
    }

    @Override
    public void setContext(String subject) {
        context=new Context();
        context.setVariable("code", getCode());
        context.setVariable("subject", subject);
        if(subject.equals("REGISTER")){
            context.setVariable("src","https://img.zcool.cn/community/019b615de70a5ba8012138534fa69f.jpg??imageMogr2/format/webp");
        }else{
            context.setVariable("src","https://img.zcool.cn/community/013063604068a611013f3745c7f003.png@3000w_1l_2o_100sh.png");
        }
    }

    @Override
    public String getCode() {
        return messageHelper.getRequiredInformationForEmail().getCode();
    }
}