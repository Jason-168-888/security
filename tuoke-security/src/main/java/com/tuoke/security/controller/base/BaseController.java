package com.tuoke.security.controller.base;

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

import com.tuoke.security.common.CommonResponse;
import com.tuoke.security.common.error.impl.BussinessErrorImpl;
import com.tuoke.security.common.exception.BussinessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理从子类Controller层抛过来的Exception
 * Created by Jason on April 16, 2021
 */
public class BaseController {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception exception) {
        Map<String,Object> responseMap = new HashMap<>();

        if(exception instanceof BussinessException){
            BussinessException bussinessException = (BussinessException)exception;
            responseMap.put("errCode",bussinessException.getErrCode());
            responseMap.put("errMsg",bussinessException.getErrMsg());
        }
        else {
            responseMap.put("errCode", BussinessErrorImpl.ORTHER_UNKNOWN_EXCEPTION.getErrCode());
            responseMap.put("errMsg",BussinessErrorImpl.ORTHER_UNKNOWN_EXCEPTION.getErrMsg());
        }

        return CommonResponse.make("fail", responseMap);
    }
}
