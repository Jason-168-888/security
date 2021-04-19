package com.tuoke.security.service.model;

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
 * 作为最终展示股票头寸结果，结合List<SecurityResultModel>，
 * 存储了所有股票头寸信息对象，集中式处理securityCode（证券代码）、quantity（数量）
 * Created by Jason on April 17, 2021
 */
@Data
public class SecurityResultModel {
    private String securityCode;
    private String quantity;
}
