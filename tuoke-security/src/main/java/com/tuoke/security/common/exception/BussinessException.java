package com.tuoke.security.common.exception;

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

import com.tuoke.security.common.error.IBussinessError;

/**
 * 封装后端校验业务出错时抛出的异常，往上一直抛给BaseController处理
 * 并可放置BussinessErrorImpl枚举，前端可方便地获取错误码和错误信息。
 * Created by Jason on April 16, 2021
 */
public class BussinessException extends Exception implements IBussinessError {
    private IBussinessError bussinessError;

    public BussinessException(IBussinessError bussinessError){
        super();
        this.bussinessError = bussinessError;
    }

    public BussinessException(IBussinessError bussinessError,String errMsg) {
        super();
        this.bussinessError = bussinessError;
        this.bussinessError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return bussinessError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return bussinessError.getErrMsg();
    }

    @Override
    public IBussinessError setErrMsg(String errMsg) {
        bussinessError.setErrMsg(errMsg);
        return this.bussinessError;
    }
}
