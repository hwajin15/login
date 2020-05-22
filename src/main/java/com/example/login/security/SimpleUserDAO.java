package com.example.login.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SimpleUserDAO {
    @Autowired JdbcTemplate jt;

    public List<Map<String, ?>> getUserInfo(String username) {

        return jt.query("select * from simple_users where username=?", new Object[] {username} , (rs, rowNum) -> {
            Map<String, Object> anUser = new HashMap<>();
            anUser.put("username", rs.getString(2));
            anUser.put("password", rs.getString(3));
            anUser.put("role", rs.getString(4));
            anUser.put("food", rs.getString(5));
            return anUser;
        });
    }

    public int insertUserInfo(Map<String, String> user) {
        String sql = "insert into "
                + "simple_users(iduser, username, password, role, food, email, real_name) "
                + "values(0, ?, ?, 'GUEST', ?, ?, ?)";

        return jt.update(sql,
                user.get("user-id"),
                user.get("user-password"),
                user.get("user-food"),
                user.get("user-email"),
                user.get("user-real-name")
        );
    }

}