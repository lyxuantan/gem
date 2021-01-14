package com.example.session03.controllers;


import com.example.session03.constant.ErrorCode;
import com.example.session03.exception.CustomException;
import com.example.session03.models.User;
import com.example.session03.constant.UserStatus;
import com.example.session03.service.UserService;
import com.example.session03.upload.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${page.user.size}")
    private Integer pageSize;


    @GetMapping("/")
    public String showSignUpForm(ModelMap model) {
        User u = new User();

        model.addAttribute("USER", u);
        model.addAttribute("ACTION", "saveOrUpdate");
        return "register-user";
    }


    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(ModelMap model, @Valid @ModelAttribute("USER") User user, @RequestParam("image") MultipartFile multipartFile) throws IOException {
//        if (result.hasErrors()) {
//            return "register-user";
//        }
        //d
        try{
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        user.setPhotos(fileName);
        User savedUser = userService.register(user);
        String uploadDir = "user-photos/" + savedUser.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        //

//        try {
//            userService.register(user);

        } catch (CustomException e) {
            model.addAttribute("MSG", new CustomException(ErrorCode.FAIL_UNKNOW).getDesc());
        }
        return "login";

    }

    @RequestMapping("list")
    public String list(Model model, HttpSession session) {
        if (session.getAttribute("USERNAME") != null) {
            return findPaginated(1, model);
        }
        return "login";
    }


    @RequestMapping("/delete/{id}")
    public String delete(Model model, @PathVariable(name = "id") Long id) {
        userService.deleteById(id);
        model.addAttribute("USERS", userService.findAll());
        return findPaginated(1, model);
    }


    @RequestMapping("/edit/{id}")
    public String edit(Model model, @PathVariable(name = "id") Long id) {

        User u = userService.editUserById(id);
        model.addAttribute("USER", u);
        model.addAttribute("ACTION", "/update/" + id);
        return "update-user";


    }


    @PostMapping("update/{id}")
    public String updateStudent(@PathVariable("id") long id, @Valid @ModelAttribute("USER") User user,
                                Model model, BindingResult result) {

        try {
            userService.save(user);
            model.addAttribute("USERS", userService.findAll());

        } catch (CustomException ex) {
            model.addAttribute("MSG", new CustomException(ErrorCode.FAIL_UNKNOW).getDesc());
        }
        return "redirect:/list";

    }


    @RequestMapping("/edit/status/{id}")
    public String editStatus(ModelMap model, @PathVariable(name = "id") Long id) {
        Optional<User> user = userService.findById(id);
//                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        model.addAttribute("USER", user.get());
        return "update-status";
    }


    @PostMapping("update/status/{id}")
    public String updateStatus(@PathVariable("id") long id, @RequestParam("status") UserStatus status,
                               Model model) {


        try {
            userService.updateStatus(status, id);
            model.addAttribute("USERS", userService.findAll());
        } catch (CustomException ex) {
            model.addAttribute("MSG", "Khong tim thay User");
        }
        return "redirect:/list";
    }


    @RequestMapping("/login")
    public String showLogin() {
        return "login";
    }


    @PostMapping("/checklogin")
    public String checkLogin(Model model,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password, HttpSession session) {

        if (userService.checkLogin(username, password)) {
            session.setAttribute("USERNAME", username);
            model.addAttribute("USERS", userService.findAll());
            return "redirect:/list";
        } else {
            System.out.println("login that bai");
            model.addAttribute("ERROR", "Username or password not exist");
        }
        return "login";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("USERNAME");

        return "redirect:/login";
    }


    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model) {

        Page<User> page = userService.findPaginated(pageNo, pageSize);
        List<User> listUsers = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("USERS", listUsers);
        return "view-user";
    }


    @GetMapping("/search")
    public String viewSearchUser(Model model, @Param("keyword") String keyword) {
        List<User> listUsers = userService.searchUser(keyword);
        if (listUsers.isEmpty()) {
            model.addAttribute("MSG", "Không có User Phù Hợp");
        } else {
            model.addAttribute("USERS", listUsers);
        }
        return "view-search";
    }

    @GetMapping("/users/export/excel")
    public String exportToExcel(HttpServletResponse response,Model model) {
        try {
            userService.exportInforToExcel(response);
//            model.addAttribute("MSG", "Sucesss");
            System.out.println("lu thanh cong");
            return "redirect:/list";

        } catch (RuntimeException | IOException e) {
            model.addAttribute("MSG", ErrorCode.FAIL_UNKNOW.getDesc());
            return "redirect:/list";
        }
    }


}
