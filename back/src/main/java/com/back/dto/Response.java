package com.back.dto;

import lombok.Data;

@Data
public class Response {
    private int code;
    private String msg;
    private String additionalInformation;
    public static Response successAddDataAndSentMessage() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("success");
        return result;
    }

    public static  Response Forbidden(HttpStatus status) {
        Response result = new Response();
        result.setCode(status.getCode());
        result.setMsg(status.getMsg());
        return result;
    }
    public static  Response successRegister() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("成功注册");
        return result;
    }

    public static  Response successMail() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("成功发送验证码");
        return result;
    }


    public static  Response alreadyExistsEmail() {
        Response result = new Response();
        result.setCode(409);
        result.setMsg("该邮箱已被使用");
        return result;
    }

    public static  Response alreadyExistsUsername() {
        Response result = new Response();
        result.setCode(409);
        result.setMsg("该用户名已被使用");
        return result;
    }

    public static  Response codeExpired() {
        Response result = new Response();
        result.setCode(401);
        result.setMsg("验证码已过期");
        return result;
    }

    public static  Response errorCode() {
        Response result = new Response();
        result.setCode(401);
        result.setMsg("验证码错误");
        return result;
    }

    public static Response successLoginUser(int userId,String token, String username) {
        Response result = new Response();
        result.setCode(userId);
        result.setMsg(token);
        result.setAdditionalInformation(username);
        return result;
    }
    public static Response successLoginAdministrator(String token) {
        Response result = new Response();
        result.setCode(200);
        result.setMsg(token);
        result.setAdditionalInformation("Administrator");
        return result;
    }

    public static Response notFoundUser() {
        Response result = new Response();
        result.setCode(404);
        result.setMsg("用户不存在 ");
        return result;
    }

    public static Response errorPassword() {
        Response result = new Response();
        result.setCode(401);
        result.setMsg("密码错误");
        return result;
    }

    public static Response successResetPassword() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("密码重置成功");
        return result;
    }

    public static Response successComment() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("评论成功");
        return result;
    }

    public static Response successWriteArticle() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("文章提交成功，等待审核");
        return result;
    }

    public static Response successUpdateArticle() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("文章修改成功，等待审核");
        return result;
    }

    public static Response successWriteDraft() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("草稿提交成功");
        return result;
    }

    public static Response successApproveArticle() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("审核成功");
        return result;
    }

    public static Response successRejectArticle() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("拒绝成功");
        return result;
    }

    public static Response successDeleteArticle() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("删除成功");
        return result;
    }

    public static Response successApplyDelete() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("申请删除成功,等待审核");
        return result;
    }

    public static Response successUndoDelete() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("撤销删除成功");
        return result;
    }

    public static Response failDeleteArticle() {
        Response result = new Response();
        result.setCode(400);
        result.setMsg("删除失败");
        return result;
    }

    public static Response successAddData() {
        Response result = new Response();
        result.setCode(200);
        result.setMsg("添加成功");
        return result;
    }
}
