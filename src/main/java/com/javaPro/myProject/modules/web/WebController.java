package com.javaPro.myProject.modules.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/web")
public class WebController {
    @GetMapping("/index")
    public String index(){//主页
        return "index";
    }
    @GetMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    @GetMapping("personal")
    public String personal(){

        return "personal";
    }
    @GetMapping("chongzhi")
    public String chongzhi(){

        return "chongzhi";
    }
    @GetMapping("register")
    public String register(){

        return "register";
    }
    @GetMapping("tmoney")
    public String tmoney(){

        return "tmoney/index";
    }


    @GetMapping("login")
    public String login(){

        return "login";
    }

    @GetMapping("test-api")
    public String testApi(){
        return "test-api";
    }

    @GetMapping({"/user"})
    public String user() {
        return "user/index";
    }
    @GetMapping({"/notice"})
    public String notice() {
        return "notice/index";
    }
    @GetMapping({"/type"})
    public String type() {
        return "type/index";
    }
    @GetMapping({"/producttype"})
    public String producttype() {
        return "producttype/index";
    }
    @GetMapping({"/company"})
    public String company() {
        return "company/index";
    }
    @GetMapping({"/product"})
    public String product() {
        return "product/index";
    }
    @GetMapping({"/order"})
    public String order() {
        return "order/index";
    }
    @GetMapping({"/sta"})
    public String sta() {
        return "sta";
    }
    @GetMapping({"/message"})
    public String message() {
        return "message/index";
    }
//----------------------用户
@GetMapping({"/userindex"})
public String userindex() {
    return "/webSite/index";
}
@GetMapping({"/userproductlist"})
public String userproductlist() {
    return "/webSite/shop";
}
@GetMapping({"/usercart"})
public String usercart() {
    return "/webSite/cart";
}
@GetMapping({"/usercontact"})
public String usercontact() {
    return "/webSite/contact";
}
@GetMapping({"/userblog"})
public String userblog() {
    return "/webSite/blog";
}
@GetMapping({"/usersingle"})
public String usersingle() {
    return "/webSite/single-product";
}

//----------------------服务商
@GetMapping({"/companyindex"})
public String companyindex() {
    return "/webSite/index";
}

@GetMapping({"/provider/dashboard"})
public String providerDashboard() {
    return "provider-dashboard";
}

@GetMapping("/test-user-info")
public String testUserInfo() {
    return "test-user-info";
}

}
