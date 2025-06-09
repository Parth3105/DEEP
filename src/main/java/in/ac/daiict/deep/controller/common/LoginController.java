package in.ac.daiict.deep.controller.common;

import in.ac.daiict.deep.constant.endpoints.AdminEndpoint;
import in.ac.daiict.deep.constant.endpoints.CommonEndPoint;
import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.constant.template.CommonTemplate;
import in.ac.daiict.deep.entity.User;
import in.ac.daiict.deep.service.OtpVerificationService;
import in.ac.daiict.deep.service.UserService;
import in.ac.daiict.deep.dto.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class LoginController {
    private UserService userService;
    private OtpVerificationService otpVerificationService;

    @GetMapping(CommonEndPoint.LOGIN)
    public String renderLoginPage(){
        return CommonTemplate.LOGIN_PAGE;
    }

    @PostMapping(CommonEndPoint.AUTHENTICATE)
    public String authenticate(){;
        return "";
    }

    @GetMapping(CommonEndPoint.FORGOT_PASSWORD)
    public String renderForgotPasswordPage(Model model){
        return CommonTemplate.FORGOT_PASSWORD_PAGE;
    }

    @PostMapping(CommonEndPoint.FORGOT_PASSWORD)
    public String loadStudentId(@RequestParam("username") String username, RedirectAttributes redirectAttributes, Model model){
        User user=userService.findUser(username);
        if(user==null){
            redirectAttributes.addFlashAttribute("submitResponse",new ResponseDto(ResponseStatus.NOT_FOUND, ResponseMessage.USERNAME_NOT_FOUND));
            return "redirect:"+CommonEndPoint.FORGOT_PASSWORD;
        }

        otpVerificationService.generateOtpAndSendMail(user.getUsername(),user.getEmail());
        model.addAttribute("username",username);
        return CommonTemplate.VERIFY_OTP_PAGE;
    }

    @PostMapping(CommonEndPoint.VERIFY_OTP)
    public String loadOtp(@RequestParam("username") String username, @RequestParam("otp") String otp, Model model){
        ResponseDto response=otpVerificationService.verifyOtp(username,otp);
        model.addAttribute("otpVerificationResponse",response);
        model.addAttribute("username",username);
        if(response.getStatus()!=ResponseStatus.OK) return CommonTemplate.VERIFY_OTP_PAGE;
        return CommonTemplate.RESET_PASSWORD_PAGE;
    }

    @PostMapping(CommonEndPoint.RESET_PASSWORD)
    public String loadNewPassword(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes){
        // handle
        redirectAttributes.addFlashAttribute("resetResponse",userService.resetPassword(username,password));
        return "redirect:"+CommonEndPoint.LOGIN;
    }
}
