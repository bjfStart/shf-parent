package com.atguigu.base;

import org.springframework.ui.Model;

/**
 * @author feng
 * @create 2022-06-11 16:14
 */
public class BaseController {
    private static final String PAGE_SUCCESS = "common/successPage";
    private static final String PAGE_ERROR = "common/errorPage";

    public String successPage(Model model, String successMessage){
        model.addAttribute("messagePage",successMessage);
        return PAGE_SUCCESS;
    }

    public String errorPage(Model model, String errorMessage){
        model.addAttribute("errorMessage",errorMessage);
        return PAGE_ERROR;
    }
}
