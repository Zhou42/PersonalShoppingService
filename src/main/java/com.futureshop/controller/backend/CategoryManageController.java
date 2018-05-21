package com.futureshop.controller.backend;

import com.futureshop.common.Const;
import com.futureshop.common.ServerResponse;
import com.futureshop.pojo.Category;
import com.futureshop.pojo.User;
import com.futureshop.service.ICategoryService;
import com.futureshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by yangzhou on 2018-05-16.
 */

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(
            value = "add",
            method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(
            HttpSession session,
            String categoryName,
            @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    @RequestMapping(
            value = "set_name",
            method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(
            HttpSession session,
            @RequestBody Category category) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.updateCategoryName(category.getId(), category.getName());
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping(
            value = "get/{categoryId}",
            method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(
            HttpSession session,
            @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    @RequestMapping(
            value = "get_deep/{categoryId}",
            method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(
            HttpSession session,
            @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


}
