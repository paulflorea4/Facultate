package org.example.inchirieriauto.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.inchirieriauto.dto.RegistrationDTO;
import org.example.inchirieriauto.exception.EmailAlreadyUsedException;
import org.example.inchirieriauto.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.authentication.AuthenticationManager;

@Controller
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registrationDTO", new RegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegistrationDTO registrationDTO, 
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            userService.registerClient(registrationDTO.getEmail(), 
                                     registrationDTO.getPassword(), 
                                     registrationDTO.getName());
            return "redirect:/login?success";
        } catch (EmailAlreadyUsedException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registrationDTO", registrationDTO);
            return "register";
        } catch (DataAccessException e) {
            model.addAttribute("error", "Serviciul de înregistrare nu este disponibil momentan. Încearcă mai târziu.");
            model.addAttribute("registrationDTO", registrationDTO);
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLogin() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(email, password)
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            return "redirect:/cars";
        } catch (AuthenticationException ex) {
            if (isDatabaseFailure(ex)) {
                return "redirect:/login?dbError";
            }
            return "redirect:/login?error";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response,
                         Authentication authentication) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return "redirect:/login?logout";
    }

    private boolean isDatabaseFailure(Throwable exception) {
        Throwable current = exception;
        while (current != null) {
            if (current instanceof DataAccessException) {
                return true;
            }
            if (current instanceof AuthenticationServiceException && "DB_ERROR".equals(current.getMessage())) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}


