package com.example.login.controller;
import java.util.Map;

import com.example.login.security.SimpleUserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {

    @Autowired
    SimpleUserDAO sud;

    @GetMapping("/signup")
    public String signUp(Model model) {

        return "signup/signup";
    }

    @PostMapping("/signup")
    public String signUpProc(Model model, @RequestParam Map<String, String> params) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(params);

        String userId = params.get("user-id");
        String userPwd = params.get("user-password");
        String userRealName = params.get("user-real-name");
        String userFood = params.get("user-food");

        // back-end validation
        String result = "";
        boolean isAllValidate = true;
        if(userId.length() < 4) {
            result += "user-id must be at least 4 character long.\n";
            isAllValidate = false;
        }
        if(userPwd.length() < 4) {
            result += "user-password must be at least 4 character long.\n";
            isAllValidate = false;
        }
        if(userRealName.length() <= 0) {
            result += "user-name is not entered.\n";
            isAllValidate = false;
        }
        if(userFood.length() <= 0) {
            result += "user-food is not entered.\n";
            isAllValidate = false;
        }

        // submit to database
        if(isAllValidate) {
            params.remove("_csrf");
            params.put("user-password", passwordEncoder.encode(userPwd));

            try {
                int insertResult = sud.insertUserInfo(params);
                if(insertResult >= 1) {
                    result += "You have successfully registered.";
                } else {
                    result += "Sign-up failed. There may be duplicate information or other issues.";
                    isAllValidate = false;
                }
            } catch(Exception e) {
                e.printStackTrace();
                result += "Sign-up failed. There may be duplicate information or other issues.";
                isAllValidate = false;
            }

        }

        model.addAttribute("isSuccess", isAllValidate);
        model.addAttribute("resultMsg", result);
        return "signup/signup-result";
    }
}