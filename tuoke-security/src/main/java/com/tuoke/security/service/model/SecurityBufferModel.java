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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 作为处理交易记录的中间缓存，结合LinkedHashMap<String, SecurityBufferModel>，
 * 保证了顺序性，并集中式处理securityCode（证券代码）、quantity（数量）、action（交易操作）
 * Created by Jason on April 17, 2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityBufferModel {
    private Integer quantity;
    private String action;
}
