package com.tuoke.security.controller;

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
import com.tuoke.security.common.exception.BussinessException;
import com.tuoke.security.controller.base.BaseController;
import com.tuoke.security.entity.Transaction;
import com.tuoke.security.service.ITransactionService;
import com.tuoke.security.service.model.SecurityResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 交易控制接口，唯一负责与前端直接交互，
 * 只控制流转，业务逻辑处理都交给Service，自身不负责任何业务处理。
 * Created by Jason on April 16, 2021
 */
@RestController
@RequestMapping("/transaction")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class TransactionController extends BaseController{

    @Autowired
    private ITransactionService transactionService;

    /**
     * 调用Service读取Mysql数据库transaction表所有数据
     * @return CommonResponse  以通用数据包方式返回给前端
     */
    @RequestMapping(value = "/load", method = {RequestMethod.GET})
    public CommonResponse loadTableFromDB() throws BussinessException {
        List<Transaction> transactionList = transactionService.loadTableFromDB();
        return CommonResponse.success(transactionList);
    }

    /**
     * 调用Service业务逻辑，处理载入的数据库交易记录
     * @return CommonResponse  以通用数据包方式返回给前端
     */
    @RequestMapping(value = "/execute", method = {RequestMethod.POST})
    public CommonResponse executeTransaction() throws BussinessException, InterruptedException {
        List<SecurityResultModel> securityResultList = transactionService.executeTransaction();
        goodVision(1);
        return CommonResponse.success(securityResultList);
    }

    /**
     * 让页面缓second秒载入数据，视觉体验更好一点◠‿◠
     * @param second 秒数
     */
    public void goodVision(int second){
        try {
            Thread.sleep(second*1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
