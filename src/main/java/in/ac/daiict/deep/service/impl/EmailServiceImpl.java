package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private JavaMailSender javaMailSender;

    public void sendOtp(String to, String otp){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("OTP for Password Reset");
        simpleMailMessage.setText("Use the following OTP to reset password: "+otp);
        javaMailSender.send(simpleMailMessage);
    }
}
