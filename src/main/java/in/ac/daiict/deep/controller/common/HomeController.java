package in.ac.daiict.deep.controller.common;

import in.ac.daiict.deep.security.constant.Roles;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectBasedOnRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Roles.ROLE_ADMIN));
        boolean isStudent = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(Roles.ROLE_STUDENT));

        if (isAdmin) {
            return "redirect:/admin/home";
        } else if (isStudent) {
            return "redirect:/student/home";
        } else {
            return "redirect:/login";
        }
    }
}

