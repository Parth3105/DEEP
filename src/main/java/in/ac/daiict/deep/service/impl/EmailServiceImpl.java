package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private JavaMailSender javaMailSender;

    public void sendOtp(String username, String to, String otp){
        MimeMessage message=javaMailSender.createMimeMessage();
        try {
            Resource resource = new ClassPathResource("templates/common/otp-email-template.html");
            String htmlContent = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
            htmlContent = htmlContent.replace("{{otp}}", otp);
            htmlContent = htmlContent.replace("{{Username}}", username);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("DEEP: Password Reset Verification Code");
            helper.setText(htmlContent,true);
            Resource logo=new ClassPathResource("/static/admin/images/DEEP Logo.png");
            helper.addInline("logoImage",logo);

            javaMailSender.send(message);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
