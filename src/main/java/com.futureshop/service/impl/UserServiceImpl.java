package com.futureshop.service.impl;

import com.futureshop.common.Const;
import com.futureshop.common.ServerResponse;
import com.futureshop.common.TokenCache;
import com.futureshop.dao.UserMapper;
import com.futureshop.pojo.User;
import com.futureshop.service.IUserService;
import com.futureshop.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by yangzhou on 2017-11-25.
 */

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    public ServerResponse<User> register(User user) {
        UUID uuid = UUID.randomUUID();
        user.setId(uuid.toString());

        user.setCreate_time(new Date());
        user.setUpdate_time(new Date());

        userMapper.insert(user);

        return ServerResponse.createBySuccess(user);
    }

    public ServerResponse login(String username, String password) throws Exception {
        // checked in controller username exist
        // select by username and md5(password)
        User user = userMapper.selectByUsernameAndPassword(username, MD5Util.getMD5(password));
        if (user == null) {
            return ServerResponse.createByErrorMessage("Wrong password");
        } else {
            return ServerResponse.createBySuccess(user);
        }
    }

    public ServerResponse<User> update(User user) {
        // check if email duplicate
        if (this.checkExist(Const.EMAIL, user.getEmail())) {
            return ServerResponse.createByErrorMessage("Deplicate email address");
        }

        // username could not be modified
        user.setUsername(null);

        // password need to be modified by separate password function
        user.setPassword(null);

        // mybatis update 后 返回的只能是count
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count < 1) {
            return ServerResponse.createByErrorMessage("Update user info failure");
        }
        User userUpdated = userMapper.selectByPrimaryKey(user.getId());
        userUpdated.setPassword(null);
        return ServerResponse.createBySuccess(userUpdated);
    }

    public boolean checkOldPasswordCorrect(String username, String oldPassword) throws Exception {
        return userMapper.selectCountByUsernameAndPassword(username, MD5Util.getMD5(oldPassword)) >= 1;
    }

    public ServerResponse resetPassword(String username, String newPassword) throws Exception {
        userMapper.updatePasswordByUsername(username, MD5Util.getMD5(newPassword));
        return ServerResponse.createBySuccess();
    }

    public ServerResponse<String> getQuestion(String username) {
        return ServerResponse.createBySuccess(userMapper.selectQuestionByUsername(username));
    }

    public boolean checkAnswer(String username, String question, String answer) {
        return answer.equals(userMapper.selectByUsernameAndQuestion(username, question));
    }

    public ServerResponse forgetPasswordAndReset(String username, String newPassword, String token) throws Exception {
        String tokenCached = TokenCache.get(TokenCache.TOKEN_PREFIX + username);
        if (tokenCached != null && tokenCached.equals(token)) {
            // reset password
            userMapper.updatePasswordByUsername(username, MD5Util.getMD5(newPassword));
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("Invalid token");
        }
    }


    public boolean checkExist(String value, String type) {
        if (Const.USERNAME.equals(type)) {
            // count how many entries that have this username
            int count = userMapper.selectByUsername(value);
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } else if (Const.EMAIL.equals(type)) {
            int count = userMapper.selectByEmail(value);
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
