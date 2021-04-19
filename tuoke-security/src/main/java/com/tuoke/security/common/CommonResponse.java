package com.tuoke.security.common;

/*   ┏┓　　　┏┓
 * ┏┛┻━━━┛┻┓　
 * ┃　　　━　　　┃
 * ┃　┳┛　┗┳　┃
 * ┃　　　┻　　　┃
 * ┗━┓　　　┏━┛
 * 　　┃　　　┗━━━┓
 * 　　┃　  神兽保佑　┣┓
 * 　　┃　  永无BUG　 ┏┛
 * 　　┗┓┓┏━┳┓┏┛
 * 　　　┃┫┫　┃┫┫
 * 　　　┗┻┛　┗┻┛ 　　　
 */

import lombok.Data;

/**
 * 封装后端发往前端的响应数据，统一数据结构对象CommonResponse返回
 * Created by Jason on April 16, 2021
 */
@Data
public class CommonResponse {
    // 请求的返回结果“success”或“fail"
    private String status;

    // 若status=success，则data内返回前端需要的json数据
    // 若status=fail，则data内使用通用的错误码格式
    private Object data;

    // 返回一个data为null成功的数据响应包，如CommonResponse.success();
    public static CommonResponse success() {
        return CommonResponse.make("success", null);
    }

    // 返回一个带data对象成功的数据响应包，如CommonResponse.success(data);
    public static CommonResponse success(Object result) {
        return CommonResponse.make("success", result);
    }

    public static CommonResponse make(String status, Object result) {
        CommonResponse commonReturn = new CommonResponse();
        commonReturn.setStatus(status);
        commonReturn.setData(result);
        return commonReturn;
    }
}