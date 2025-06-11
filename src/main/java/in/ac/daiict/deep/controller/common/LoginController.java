package in.ac.daiict.deep.controller.common;

import in.ac.daiict.deep.constant.deadlines.Deadline;
import in.ac.daiict.deep.constant.endpoints.CommonEndPoint;
import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.template.CommonTemplate;
import in.ac.daiict.deep.entity.User;
import in.ac.daiict.deep.service.OtpVerificationService;
import in.ac.daiict.deep.service.UserService;
import in.ac.daiict.deep.dto.ResponseDto;
import in.ac.daiict.deep.util.sessionhelper.SessionAttribute;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;

@Controller
@AllArgsConstructor
public class LoginController {
    private UserService userService;
    private OtpVerificationService otpVerificationService;

    @GetMapping(CommonEndPoint.LOGIN)
    public String renderLoginPage(HttpSession session, Model model) {
        session.setAttribute("loginSession", new SessionAttribute<>(true, Duration.ofMinutes(Deadline.LOGIN_SESSION_DURATION_MINUTES)));
        session.getId();
        return CommonTemplate.LOGIN_PAGE;
    }

    @PostMapping(CommonEndPoint.AUTHENTICATE)
    public String authenticate(HttpSession session) {
        return "";
    }

    @GetMapping(CommonEndPoint.FORGOT_PASSWORD)
    public String renderForgotPasswordPage(Model model, HttpSession session) {
        if (session.getAttribute("loginSession") == null) {
            return "redirect:" + CommonEndPoint.LOGIN;
        }
        session.getId();
        return CommonTemplate.FORGOT_PASSWORD_PAGE;
    }

    @PostMapping(CommonEndPoint.FORGOT_PASSWORD)
    public String loadStudentId(@RequestParam("username") String username, RedirectAttributes redirectAttributes, Model model, HttpSession session) {
        if (session.getAttribute("loginSession") == null) return "redirect:" + CommonEndPoint.LOGIN;
        else {
            SessionAttribute<Boolean> sessionAttribute = (SessionAttribute<Boolean>) session.getAttribute("loginSession");
            if (sessionAttribute.isExpired()) {
                redirectAttributes.addFlashAttribute("sessionExpired", new ResponseDto(ResponseStatus.SESSION_TIMEOUT, ResponseMessage.SESSION_EXPIRED));
                return "redirect:" + CommonEndPoint.LOGIN;
            }
        }
        User user = userService.findUser(username);
        if (user == null) {
            redirectAttributes.addFlashAttribute("submitResponse", new ResponseDto(ResponseStatus.NOT_FOUND, ResponseMessage.USERNAME_NOT_FOUND));
            return "redirect:" + CommonEndPoint.FORGOT_PASSWORD;
        }
        otpVerificationService.generateOtpAndSendMail(user.getUsername(), user.getEmail());
        session.setAttribute("forgotPasswordSession", new SessionAttribute<>(username, Duration.ofMinutes(Deadline.FORGOT_PASSWORD_SESSION_DURATION_MINUTES)));
        session.setAttribute("userEmail", new SessionAttribute<>(user.getEmail(), Duration.ofMinutes(Deadline.FORGOT_PASSWORD_SESSION_DURATION_MINUTES)));
        return CommonTemplate.VERIFY_OTP_PAGE;
    }

    @PostMapping(CommonEndPoint.RESEND_OTP)
    public String resendOtp(@RequestParam("token") String randomInput, @PathVariable("random") String randomVariable, RedirectAttributes redirectAttributes, HttpSession session) {
        if (!randomInput.equals(randomVariable)) return "redirect:" + CommonEndPoint.LOGIN;
        String username, email;
        if (session.getAttribute("forgotPasswordSession") == null) return "redirect:" + CommonEndPoint.LOGIN;
        else {
            SessionAttribute<String> sessionAttribute = (SessionAttribute<String>) session.getAttribute("forgotPasswordSession");
            SessionAttribute<String> userEmail = (SessionAttribute<String>) session.getAttribute("userEmail");
            username = sessionAttribute.getValue();
            email = userEmail.getValue();
            if (sessionAttribute.isExpired()) {
                redirectAttributes.addFlashAttribute("sessionExpired", new ResponseDto(ResponseStatus.SESSION_TIMEOUT, ResponseMessage.SESSION_EXPIRED));
                return "redirect:" + CommonEndPoint.LOGIN;
            }
        }
        otpVerificationService.generateOtpAndSendMail(username, email);
        return CommonTemplate.VERIFY_OTP_PAGE;
    }

    @PostMapping(CommonEndPoint.VERIFY_OTP)
    public String loadOtp(@RequestParam("otp") String otp, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        String username;
        if (session.getAttribute("forgotPasswordSession") == null) return "redirect:" + CommonEndPoint.LOGIN;
        else {
            SessionAttribute<String> sessionAttribute = (SessionAttribute<String>) session.getAttribute("forgotPasswordSession");
            username = sessionAttribute.getValue();
            if (sessionAttribute.isExpired()) {
                redirectAttributes.addFlashAttribute("sessionExpired", new ResponseDto(ResponseStatus.SESSION_TIMEOUT, ResponseMessage.SESSION_EXPIRED));
                return "redirect:" + CommonEndPoint.LOGIN;
            }
        }
        ResponseDto response = otpVerificationService.verifyOtp(username, otp);
        model.addAttribute("otpVerificationResponse", response);
        if (response.getStatus() != ResponseStatus.OK) return CommonTemplate.VERIFY_OTP_PAGE;
        session.setAttribute("resetSession", new SessionAttribute<>(username, Duration.ofMinutes(Deadline.RESET_SESSION_DURATION_MINUTES)));
        return CommonTemplate.RESET_PASSWORD_PAGE;
    }

    @PostMapping(CommonEndPoint.RESET_PASSWORD)
    public String loadNewPassword(@RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session) {
        String username;
        if (session.getAttribute("resetSession") == null) return "redirect:" + CommonEndPoint.LOGIN;
        else {
            SessionAttribute<String> sessionAttribute = (SessionAttribute<String>) session.getAttribute("resetSession");
            username = sessionAttribute.getValue();
            if (sessionAttribute.isExpired()) {
                redirectAttributes.addFlashAttribute("sessionExpired", new ResponseDto(ResponseStatus.SESSION_TIMEOUT, ResponseMessage.SESSION_EXPIRED));
                return "redirect:" + CommonEndPoint.LOGIN;
            }
        }
        redirectAttributes.addFlashAttribute("resetResponse", userService.resetPassword(username, password));
        return "redirect:" + CommonEndPoint.LOGIN;
    }
}
