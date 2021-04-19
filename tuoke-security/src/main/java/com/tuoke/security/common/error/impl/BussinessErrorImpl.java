package com.tuoke.security.common.error.impl;

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
 * 强大的BussinessErrorImpl枚举，除了可设置和获取错误码和错误信息之外，
 * 还可以很好的拓展项目业务逻辑出错的枚举，非常友好准确的提示前端，快速定位问题。
 * Created by Jason on April 16, 2021
 */
public enum BussinessErrorImpl implements IBussinessError{
    // 通用错误（10001开头）
    ORTHER_UNKNOWN_EXCEPTION(10001, "业务错误之外的未知异常"),
    INPUT_PARAMETER_ERROR(10002, "输入参数有误"),

    // 获取交易记录错误（20001开头）
    TABLE_DATA_EMPTY(20001, "transaction数据表无记录"),
    TABLE_DATA_TOO_BIG(20002, "transaction数据表记录不宜超过20条"),

    // 处理交易错误（30001开头）
    TRADE_RECORD_EMPTY(30001,"没有任何待处理的交易记录"),
    TRADE_BUFFER_EMPTY(30002, "中间MAP缓存无数据");

    private int errCode;
    private String errMsg;

    private BussinessErrorImpl(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public IBussinessError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
