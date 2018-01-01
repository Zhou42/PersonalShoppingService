package com.futureshop.dao;

import com.futureshop.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yangzhou on 2017-11-22.
 */


public interface UserMapper {
    User selectByPrimaryKey(String id);

    List<User> selectAll();

    int insertSelective(User user);

    int insert(User user);

    int selectByUsername(String username);

    int selectByEmail(String email);

    User selectByUsernameAndPassword(@Param(value = "username") String username, @Param(value = "password")  String password);

    int updateByPrimaryKeySelective(User user);

    int selectCountByUsernameAndPassword(@Param(value = "username") String username, @Param(value = "password") String password);

    int updatePasswordByUsername(@Param(value = "username") String username, @Param(value = "newPassword") String newPassword);

    String selectQuestionByUsername(String username);

    String selectByUsernameAndQuestion(@Param(value = "username") String username, @Param(value = "question") String queston);
}
