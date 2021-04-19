package com.tuoke.security.dao;

import com.tuoke.security.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DAO，由Mybatis generator自动生成
 */
@Component
public interface TransactionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transaction
     *
     * @mbg.generated Thu Apr 15 20:53:45 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transaction
     *
     * @mbg.generated Thu Apr 15 20:53:45 CST 2021
     */
    int insert(Transaction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transaction
     *
     * @mbg.generated Thu Apr 15 20:53:45 CST 2021
     */
    int insertSelective(Transaction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transaction
     *
     * @mbg.generated Thu Apr 15 20:53:45 CST 2021
     */
    Transaction selectByPrimaryKey(Integer id);

    List<Transaction> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transaction
     *
     * @mbg.generated Thu Apr 15 20:53:45 CST 2021
     */
    int updateByPrimaryKeySelective(Transaction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transaction
     *
     * @mbg.generated Thu Apr 15 20:53:45 CST 2021
     */
    int updateByPrimaryKey(Transaction record);
}