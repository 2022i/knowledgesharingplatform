package com.back.mail.context;


public class ResetPasswordMailContext extends AbstractMailContext {
    @Override
    public void setContext() {
        context.setVariable("subject", "RESET");
        context.setVariable("src","https://img.zcool.cn/community/013063604068a611013f3745c7f003.png@3000w_1l_2o_100sh.png");
    }
}
