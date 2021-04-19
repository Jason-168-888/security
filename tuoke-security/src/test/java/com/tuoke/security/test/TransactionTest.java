package com.tuoke.security.test;

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

import com.tuoke.security.SecurityTestApplication;
import com.tuoke.security.common.exception.BussinessException;
import com.tuoke.security.dao.TransactionMapper;
import com.tuoke.security.entity.Transaction;
import com.tuoke.security.service.ITransactionService;
import com.tuoke.security.service.model.SecurityBufferModel;
import com.tuoke.security.service.model.SecurityResultModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * TransactionTest包含所有的单元测试用例，根据Service的业务逻辑抽象出来
 * Created by Jason on April 17, 2021
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={SecurityTestApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TransactionTest {
    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private ITransactionService transactionService;

    // 全局交易记录列表变量,从数据库读取
    private List<Transaction> transactionList = null;

    /**
     * 测试Service的loadTableFromDB方法
     * 方法功能：缓存机制调用DAO访问Mysql数据库，查询transaction表所有交易记录
     * @throws BussinessException
     */
    @Test
    public void loadTableFromDBTest() throws BussinessException {
        Assert.assertNotNull("全局交易记录列表变量不能为空", transactionService.loadTableFromDB());

        Assert.assertFalse("Mysql的transaction数据表不能没有数据", transactionService.loadTableFromDB().size()==0);
        Assert.assertFalse("测试不宜数据过多，超过20会触发业务错误提醒", transactionService.loadTableFromDB().size()>20);
    }

    /**
     * 测试Service的randomArrivedTransaction方法
     * 方法功能：采用随机抽取List<Transaction>里对象，模拟交易记录按不同的先后顺序到达
     */
    @Test
    public void randomArrivedTransactionTest() throws BussinessException {
        transactionList = transactionMapper.selectAll();

        Transaction randomTransaction = null;
        List<Transaction> insertList = new ArrayList<>();

        for (int i = 0; i < transactionList.size(); i++) {
            // 抽取INSERT交易记录
            if(transactionList.get(i).getVersion() == 1){
                insertList.add(transactionList.get(i));
            }
        }

        Assert.assertNotNull("", insertList);
        Assert.assertFalse("如果List容器为空，则说明交易记录已经处理完毕,不需要再随机抽取，同时会触发业务错误提醒", insertList.size()==0);
        Assert.assertFalse("List容器只有1个对象，不参与随机抽取，直接insertList.get(0);", insertList.size()==1);
        Assert.assertTrue("对象2个以上，随机抽取某条交易记录，模拟交易记录可以按不同的先后顺序到达", insertList.size()>=2);

        // 以下验证randomArrivedTransaction方法抽取交易记录的随机性且不重复

        System.out.println("测试对比随机抽取与源交易记录差别：");
        System.out.println();
        // 先按顺序打印出List里所有交易记录
        System.out.println("------ 顺序打印List里所有交易记录 ------");
        for(Transaction transaction: insertList){
            System.out.println(transaction);
        }

        System.out.println();
        System.out.println("------ 打印从List里随机抽取的交易记录 ------");
        int loopTimes = insertList.size();

        // 全部随机抽出来，对比更明显
        for (int i = 0; i < loopTimes; i++) {
            randomTransaction = transactionService.randomArrivedTransaction(insertList);
            System.out.println(randomTransaction);
        }
    }

    /**
     * 测试Service的runBussinessRules方法
     * 方法功能：按交易规则处理当前单条交易记录，生成目标股票头寸中间缓存bufferMap（LinkedHashMap保证插入顺序）
     */
    @Test
    public void runBussinessRulesTest() throws BussinessException {
        Transaction transaction = new Transaction(6,4,1,"INF",20,"INSERT","SELL");

        LinkedHashMap<String, SecurityBufferModel> bufferMap = new LinkedHashMap<>();

        // Service方法runBussinessRules校验参数transaction是否为NULL，如果为NULL，则触发业务错误异常，返回给前端定位问题
        Assert.assertNotNull("交易记录transaction不能为NULL", transaction);

        // Service方法runBussinessRules校验参数bufferMap是否为NULL，如果为NULL，则触发业务错误异常，返回给前端定位问题
        Assert.assertNotNull("中间缓存bufferMap不能为NULL", bufferMap);

        // INSERT场景1：如果缓存bufferMap没有数据，INSERT交易记录会被插入bufferMap,并且数量为相应的负数
        transactionService.runBussinessRules(transaction, bufferMap);
        Assert.assertEquals(-20, bufferMap.get("INF").getQuantity().longValue());

        // INSERT场景2：如果缓存bufferMap有数据，但是没有该证券代码，INSERT交易记录会被插入bufferMap,并且数量为相应的负数
        bufferMap.clear();
        bufferMap.put("REL", new SecurityBufferModel(50,"INSERT"));
        transactionService.runBussinessRules(transaction, bufferMap);
        Assert.assertEquals(-20, bufferMap.get("INF").getQuantity().longValue());

        // INSERT场景3：如果缓存bufferMap有该证券代码，但是已经执行过CANCEL，就不能再INSERT操作
        bufferMap.clear();
        bufferMap.put("INF", new SecurityBufferModel(0,"CANCEL"));
        transactionService.runBussinessRules(transaction, bufferMap);
        Assert.assertEquals(0, bufferMap.get("INF").getQuantity().longValue());

        // INSERT场景4：如果缓存bufferMap有该证券代码，但是没有执行过CANCEL，交易记录的头寸需要与缓存bufferMap对应的头寸进行运算
        bufferMap.clear();
        bufferMap.put("INF", new SecurityBufferModel(70,"INSERT"));
        transactionService.runBussinessRules(transaction, bufferMap);
        Assert.assertEquals(50, bufferMap.get("INF").getQuantity().longValue());

        // UPDATE场景1：如果缓存bufferMap有该证券代码，已经执行过CANCEL，就不再UPDATE操作
        bufferMap.clear();
        bufferMap.put("INF", new SecurityBufferModel(0,"CANCEL"));
        transactionService.runBussinessRules(transaction, bufferMap);
        Assert.assertEquals(0, bufferMap.get("INF").getQuantity().longValue());

        // UPDATE场景2：如果缓存bufferMap有该证券代码，但是没有执行过CANCEL，则要更新bufferMap
        bufferMap.clear();
        bufferMap.put("REL", new SecurityBufferModel(50,"INSERT"));
        transaction = new Transaction(4,1,2,"REL",60,"UPDATE","BUY");
        transactionService.runBussinessRules(transaction, bufferMap);
        Assert.assertEquals(60, bufferMap.get("REL").getQuantity().longValue());

        // CANCEL场景：如果缓存bufferMap有该证券代码,做CANCEL处理
        bufferMap.clear();
        bufferMap.put("ITC", new SecurityBufferModel(40,"INSERT"));
        transaction = new Transaction(5,2,2,"ITC",30,"CANCEL","BUY");

        transactionService.runBussinessRules(transaction, bufferMap);
        Assert.assertEquals(0, bufferMap.get("ITC").getQuantity().longValue());
    }

    /**
     * 测试Service的convertMapToListTest方法
     * 方法功能：将股票头寸中间缓存bufferMap转化成最终前端可显示的结果集
     */
    @Test
    public void convertMapToListTest() throws BussinessException {
        LinkedHashMap<String, SecurityBufferModel> bufferMap = new LinkedHashMap<>();
        bufferMap.put("REL", new SecurityBufferModel(60,"UPDATE"));
        bufferMap.put("ITC", new SecurityBufferModel(0,"CANCEL"));
        bufferMap.put("INF", new SecurityBufferModel(50,"INSERT"));

        Assert.assertNotNull("bufferMap不能为空", bufferMap);
        Assert.assertFalse("中间缓存Map无数据，触发业务错误，返回给前端业务出错信息", bufferMap.size()==0);

        // 把LinkedHashMap<String, SecurityBufferModel>数据结构转化为List<SecurityResultModel>
        // 拼装securityResultModelList里的每个对象，交易数量为0、正数、负数的整数转字符串处理
        List<SecurityResultModel> resultlList = transactionService.convertMapToList(bufferMap);
        Assert.assertNotNull("结果集resultList不能为空", resultlList);
        Assert.assertFalse("结果集resultList没有数据", resultlList.size()==0);

        // 测试对比转换前后的差别：
        System.out.println("测试对比转换前后的差别:");
        System.out.println();
        System.out.println("------ 打印bufferMap ------");
        for(String key: bufferMap.keySet()){
            System.out.println(key + "," + bufferMap.get(key));
        }

        System.out.println();
        System.out.println("------ 打印resultlList ------");
        for(SecurityResultModel result: resultlList){
            System.out.println(result);
        }
    }
}