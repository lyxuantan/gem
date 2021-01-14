package com.example.session03.service;

import com.example.session03.models.User;
import com.example.session03.constant.UserStatus;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User entity);


    Optional<User> findById(Long id);


    List<User> findAll();

    User editUserById(Long id);


    void deleteById(Long s);


    boolean checkLogin(String username, String password);

    User register(User user);

    void updateStatus(UserStatus status, Long id);


    Page<User> findPaginated(int pageNo, int pageSize);

    List<User> searchUser(String keyword);

     void exportInforToExcel(HttpServletResponse response) throws IOException;


}
