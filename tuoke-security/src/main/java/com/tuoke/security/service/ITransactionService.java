package com.tuoke.security.service;

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

import com.tuoke.security.common.exception.BussinessException;
import com.tuoke.security.entity.Transaction;
import com.tuoke.security.service.model.SecurityBufferModel;
import com.tuoke.security.service.model.SecurityResultModel;

import java.util.LinkedHashMap;
import java.util.List;

public interface ITransactionService {
    List<Transaction> loadTableFromDB() throws BussinessException;;

    List<SecurityResultModel> executeTransaction() throws BussinessException;

    Transaction randomArrivedTransaction(List<Transaction> transactionList) throws BussinessException;

    void runBussinessRules(Transaction transaction, LinkedHashMap<String, SecurityBufferModel> bussinessMap) throws BussinessException;

    List<SecurityResultModel> convertMapToList(LinkedHashMap<String, SecurityBufferModel> securityMap) throws BussinessException;
}
