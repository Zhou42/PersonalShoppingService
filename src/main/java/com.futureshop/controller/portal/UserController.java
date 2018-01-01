package com.futureshop.controller.portal;


import javax.validation.Valid;

import com.futureshop.common.Const;
import com.futureshop.common.ServerResponse;
import com.futureshop.common.TokenCache;
import com.futureshop.pojo.User;
import com.futureshop.service.IUserService;
import com.futureshop.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;


/**
 * Created by yangzhou on 2017-10-26.
 */

@Controller
@RequestMapping("/user")
public class UserController {

    static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService iUserService;


    // 改用REST 风格的API
    // register
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> register(@Valid User user) throws Exception {
        // Need extra check if all needed fields exist

        // 需要validate user info
        // check if username valid
        if (this.checkExist(user.getUsername(), Const.USERNAME)) {
            return ServerResponse.createByErrorMessage("Duplicate username");
        }
        // check if email valid
        if (this.checkExist(user.getEmail(), Const.EMAIL)) {
            return ServerResponse.createBySuccessMessage("Duplicate email");
        }
        // 加密 password
        user.setPassword(MD5Util.getMD5(user.getPassword()));
        return iUserService.register(user);
    }

    // login
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse login(String username, String password, HttpSession session) throws Exception {
        if (!this.checkExist(username, Const.USERNAME)) {
            return ServerResponse.createBySuccessMessage("User does not exist!");
        }
        ServerResponse response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = (User) response.getData();
            user.setPassword(null);
            session.setAttribute(Const.CURRENT_USER, user);
        }
        return response;
    }

    // logout
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    // get user info
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        // 无需传入user id, 每个user 只能获得最的info
        // session 是per browser instance
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("User not logged in.");
        } else {
            return ServerResponse.createBySuccess(user);
        }
    }


    // update user info
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update(@Valid User user, HttpSession session) {
        // 无需传入user id, 每个user 只能update这个用户的info
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("User not logged in.");
        }
        user.setId(currentUser.getId());
        ServerResponse response = iUserService.update(user);
        session.setAttribute(Const.CURRENT_USER, response.getData());
        return response;
    }

    // reset password after logged in
    @RequestMapping(value = "/password/reset", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse passwordReset(String oldPassword, String newPassword, HttpSession session) throws Exception {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("User not logged in");
        }
        // check if oldPassword correct
        if (!iUserService.checkOldPasswordCorrect(user.getUsername(), oldPassword)) {
            return ServerResponse.createByErrorMessage("Wrong old password");
        }

        // update password
        return iUserService.resetPassword(user.getUsername(), newPassword);
    }

    // forget and reset password
    @RequestMapping(value = "/password/forget_reset", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse forgetPasswordAndReset(String username, String newPassword, String token) throws Exception {
        if (!iUserService.checkExist(username, Const.USERNAME)) {
            return ServerResponse.createByErrorMessage("Username does not exist");
        }
        return iUserService.forgetPasswordAndReset(username, newPassword, token);
    }

    @RequestMapping(value = "/password/question", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> forgetPasswordAndGetQuestion(String username) {
        if (!iUserService.checkExist(username, Const.USERNAME)) {
            return ServerResponse.createByErrorMessage("Username does not exist");
        }
        return iUserService.getQuestion(username);
    }

    @RequestMapping(value = "/password/answer", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkAnswerAndGetToken(String username, String question, String answer) {
        if (!iUserService.checkExist(username, Const.USERNAME)) {
            return ServerResponse.createByErrorMessage("Username does not exist");
        }
        if (!iUserService.checkAnswer(username, question, answer)) {
            return ServerResponse.createByErrorMessage("Wrong answer");
        }
        // return token key
        String token = UUID.randomUUID().toString();
        TokenCache.set(TokenCache.TOKEN_PREFIX + username, token);
        return ServerResponse.createBySuccess(token);
    }

    private boolean checkExist(String value, String type) {
        return iUserService.checkExist(value, type);
    }

}
