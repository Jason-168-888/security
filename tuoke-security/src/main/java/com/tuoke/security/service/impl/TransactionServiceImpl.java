package com.tuoke.security.service.impl;

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

import com.tuoke.security.common.error.impl.BussinessErrorImpl;
import com.tuoke.security.common.exception.BussinessException;
import com.tuoke.security.dao.TransactionMapper;
import com.tuoke.security.entity.Transaction;
import com.tuoke.security.service.ITransactionService;
import com.tuoke.security.service.model.SecurityBufferModel;
import com.tuoke.security.service.model.SecurityResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;


/**
 * 所有业务逻辑均在此处理，根据具体的业务规则抽象各种方法分而治之，
 * 通过注入DAO，直接与数据库交互。
 * Created by Jason on April 16, 2021
 */
@Service
public class TransactionServiceImpl implements ITransactionService{
    @Autowired
    private TransactionMapper transactionMapper;

    // 全局交易记录列表变量
    private List<Transaction> transactionList = null;

    /**
     * 缓存机制调用DAO访问Mysql数据库，查询transaction表所有交易记录
     * @return List<Transaction>  对应tansaction表所有数据
     */
    @Override
    public List<Transaction> loadTableFromDB() throws BussinessException {
        // 应用缓存机制提高程序性能，第一次从数据库里读取，以后每次页面刷新不再读数据库，直接用缓存即可
        if(transactionList == null) {
            transactionList = transactionMapper.selectAll();
        }

        // 校验tansaction表是否无数据，若无数据，则用业务通常异常捕获，返回给前端
        if(transactionList.size() == 0){
            throw new BussinessException(BussinessErrorImpl.TABLE_DATA_EMPTY);
        }

        // 校验tansaction表记录是否过大，若超过20条（测试不宜太复杂），则用业务通常异常捕获，返回给前端
        if(transactionList.size() > 20){
            throw new BussinessException(BussinessErrorImpl.TABLE_DATA_TOO_BIG);
        }

        return transactionList;
    }

    /**
     * 封装所有业务规则处理逻辑，处理载入的数据库交易记录，返回结果集给Controller
     * @return List<SecurityResultModel> 最终处理好可返回给前端的结果集
     */
    @Override
    public List<SecurityResultModel> executeTransaction() throws BussinessException {
        Transaction transaction = null;
        LinkedHashMap<String, SecurityBufferModel> bufferMap = new LinkedHashMap<>();

        // 将交易记录transactionList拆分两个List，版本号为1的firstList优先随机抽取处理股票头寸
        // 其他UPDATE和CANCEL放入非优先secondList，后面再随机抽取处理股票头寸
        List<Transaction> firstList = new ArrayList<>();
        List<Transaction> secondList = new ArrayList<>();

        for (int i = 0; i < transactionList.size(); i++) {
            if(transactionList.get(i).getVersion() == 1){
                firstList.add(transactionList.get(i));
            }
            else{
                secondList.add(transactionList.get(i));
            }
        }

        int firstTimes = firstList.size();
        int secondTimes = secondList.size();

        // 在firstList里随机抽出一条交易记录并按交易规则进行业务处理，
        // 结果缓存在LinkedHashMap容器bufferMap里
        for (int i = 0; i < firstTimes; i++) {
            // 1、随机抽取交易记录
            transaction = randomArrivedTransaction(firstList);

            // 2、按业务规则处理股票头寸
            runBussinessRules(transaction, bufferMap);
        }

        // 在secondList里随机抽出一条交易记录并按交易规则进行业务处理，
        // 结果缓存在LinkedHashMap容器bufferMap里
        for (int i = 0; i < secondTimes; i++) {
            // 1、随机抽取交易记录
            transaction = randomArrivedTransaction(secondList);

            // 2、按业务规则处理股票头寸
            runBussinessRules(transaction, bufferMap);
        }

        // 3、将缓存Map转换成最终结果集List
        List<SecurityResultModel> resultList = convertMapToList(bufferMap);

        // 4、清除中间缓存,并返回股票头寸结果List
        bufferMap.clear();
        return resultList;
    }

    /**
     * 采用随机抽取List<Transaction>里对象，模拟交易记录按不同的先后顺序到达
     * @param transactionList 所有交易记录
     * @return Transaction 当前待处理的一条交易记录
     */
    @Override
    public Transaction randomArrivedTransaction(List<Transaction> transactionList) throws BussinessException{
        int transactionNo = -1;
        Transaction currentTransaction = null;

        // 如果List容器为空，则说明交易记录已经处理完毕
        if(transactionList == null || transactionList.size() == 0){
            throw new BussinessException(BussinessErrorImpl.TRADE_RECORD_EMPTY);
        }

        // 只有一个Transaction对象，直接get就行，不用再随机抽取
        if(transactionList.size() == 1){
            return transactionList.get(0);
        }

        // 对象2个以上，随机抽取某条交易记录，模拟交易记录可以按不同的先后顺序到达
        if(transactionList.size() >= 2){
            Random random = new Random();
            transactionNo = random.nextInt(transactionList.size());
            currentTransaction = transactionList.get(transactionNo);

            // 抽取之后，要从List里删除该交易记录，避免重复交易
            transactionList.remove(transactionNo);
        }

        return currentTransaction;
    }

    /**
     * 按交易规则处理当前单条交易记录，生成目标股票头寸中间缓存bufferMap（LinkedHashMap保证插入顺序）
     * @param transaction 当前要处理的交易记录
     * @param bufferMap 目标股票头寸的中间Map缓存
     */
    @Override
    public void runBussinessRules(Transaction transaction, LinkedHashMap<String, SecurityBufferModel> bufferMap) throws BussinessException{
        if(transaction == null){
            throw new BussinessException(BussinessErrorImpl.INPUT_PARAMETER_ERROR, "交易记录transaction不能为NULL");
        }

        if(bufferMap == null){
            throw new BussinessException(BussinessErrorImpl.INPUT_PARAMETER_ERROR, "中间缓存bufferMap不能为NULL");
        }

        String strSecurityCode = transaction.getSecurityCode();
        String strAction = transaction.getAction();
        String strDirection = transaction.getTradeDirection();

        // 处理INSERT交易记录
        if(strAction.equalsIgnoreCase("INSERT")){
            int intQuantity = transaction.getQuantity();

            SecurityBufferModel securityBufferModel = new SecurityBufferModel();
            securityBufferModel.setAction("INSERT");

            if(strDirection.equalsIgnoreCase("BUY")){
                securityBufferModel.setQuantity(intQuantity);
            }
            // SELL操作，将Quantity正数转化为相应的负数
            else{
                intQuantity = intQuantity * -1;
                securityBufferModel.setQuantity(intQuantity);
            }

            // 如果缓存Map没有数据，直接插入对象
            if(bufferMap.size() == 0){
                bufferMap.put(strSecurityCode, securityBufferModel);
            }
            else{
                // 如果缓存Map有数据，但是没有该证券代码，直接插入对象
                if(bufferMap.get(strSecurityCode) == null){
                    bufferMap.put(strSecurityCode, securityBufferModel);
                    return;
                }

                // 如果缓存Map有该证券代码，但是已经执行过CANCEL，就不能再INSERT操作
                if(bufferMap.get(strSecurityCode).getAction().equalsIgnoreCase("CANCEL")){
                    return;
                }

                // 否则交易记录的头寸需要与缓存Map对应的头寸进行运算
                int mapQuantity = bufferMap.get(strSecurityCode).getQuantity();
                bufferMap.get(strSecurityCode).setQuantity(mapQuantity + intQuantity);
                bufferMap.get(strSecurityCode).setAction("INSERT");
            }
        }

        // 处理UPDATE交易记录
        if(strAction.equalsIgnoreCase("UPDATE")){
            int intQuantity = transaction.getQuantity();

            // SELL操作，将Quantity正数转化为相应的负数
            if(strDirection.equalsIgnoreCase("SELL")){
                intQuantity = intQuantity * -1;
            }

            // 缓存Map存在一样的证券代码
            if(bufferMap.get(strSecurityCode) != null){
                // 如果缓存Map已经执行过CANCEL，就不再UPDATE操作
                if(bufferMap.get(strSecurityCode).getAction().equalsIgnoreCase("CANCEL")){
                    return;
                }

                bufferMap.get(strSecurityCode).setQuantity(intQuantity);
                bufferMap.get(strSecurityCode).setAction("UPDATE");
            }
        }

        // 处理CANCEL交易记录
        if(strAction.equalsIgnoreCase("CANCEL")){
            // 缓存Map存在一样的证券代码
            if(bufferMap.get(strSecurityCode) != null){
                bufferMap.get(strSecurityCode).setQuantity(0);
                bufferMap.get(strSecurityCode).setAction("CANCEL");
            }
        }

        return;
    }

    /**
     * 将股票头寸中间缓存bufferMap转化成最终前端可显示的结果集
     * @param bufferMap 中间缓存，用来处理股票头寸用
     * @return List<SecurityResultModel> 前端最终显示的结果集
     */
    @Override
    public List<SecurityResultModel> convertMapToList(LinkedHashMap<String, SecurityBufferModel> bufferMap) throws BussinessException{
        int intQuantity = -1;
        String strQuantity = "";

        List<SecurityResultModel> resultList = new ArrayList<>();

        // 若中间缓存Map为空，则用业务通常异常捕获，返回给前端业务出错信息
        if(bufferMap == null || bufferMap.size() == 0){
            throw new BussinessException(BussinessErrorImpl.TRADE_BUFFER_EMPTY);
        }

        // 拼装securityResultModelList里的每个对象
        for(String key: bufferMap.keySet()){
            SecurityResultModel securityResultModel = new SecurityResultModel();
            securityResultModel.setSecurityCode(key);
            intQuantity = bufferMap.get(key).getQuantity();

            // 交易数量为0的特殊处理
            if(intQuantity == 0){
                securityResultModel.setQuantity("0");
                resultList.add(securityResultModel);
                continue;
            }

            // 交易数量正数与负数的整数转字符串处理
            strQuantity = intQuantity > 0 ? "+" + intQuantity : String.valueOf(intQuantity);
            securityResultModel.setQuantity(strQuantity);
            resultList.add(securityResultModel);
        }

        return resultList;
    }
}