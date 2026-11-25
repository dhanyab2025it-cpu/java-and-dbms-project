package com.codequest.platform.controller;

import com.codequest.platform.model.Student;
import com.codequest.platform.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final StudentRepository students;
    private final PasswordEncoder encoder;
    public AuthController(StudentRepository students, PasswordEncoder encoder) {
        this.students = students; this.encoder = encoder;
    }

    @GetMapping("/login") public String login() { return "login"; }

    @GetMapping("/register") public String registerForm() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email,
            @RequestParam String password, @RequestParam(required=false) String department,
            @RequestParam(required=false) Integer yearOfStudy, @RequestParam(required=false) String phone,
            RedirectAttributes ra) {
        if (students.existsByEmail(email.trim().toLowerCase())) {
            ra.addFlashAttribute("error", "An account with this email already exists.");
            return "redirect:/register";
        }
        if (password.length() < 8) {
            ra.addFlashAttribute("error", "Password must contain at least 8 characters.");
            return "redirect:/register";
        }
        Student s = new Student();
        s.setName(name.trim()); s.setEmail(email.trim().toLowerCase());
        s.setPassword(encoder.encode(password)); s.setDepartment(department);
        s.setYearOfStudy(yearOfStudy); s.setPhone(phone);
        students.save(s);
        ra.addFlashAttribute("success", "Account created. Please sign in.");
        return "redirect:/login";
    }
}
