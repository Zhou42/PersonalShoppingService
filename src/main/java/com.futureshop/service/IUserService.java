package com.futureshop.service;

import com.futureshop.common.ServerResponse;
import com.futureshop.pojo.User;

import java.util.concurrent.ExecutionException;

/**
 * Created by yangzhou on 2017-11-25.
 */


public interface IUserService {

    ServerResponse<User> register(User user);

    ServerResponse login(String username, String password) throws Exception;

    ServerResponse<User> update(User user);

    boolean checkExist(String value, String type);

    boolean checkOldPasswordCorrect(String username, String password) throws Exception;

    ServerResponse resetPassword(String username, String newPassword) throws Exception;

    ServerResponse<String> getQuestion(String username);

    boolean checkAnswer(String username, String question, String answer);

    ServerResponse forgetPasswordAndReset(String username, String newPassword, String token) throws Exception;
}
