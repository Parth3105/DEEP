package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.entity.User;
import in.ac.daiict.deep.repository.UserRepo;
import in.ac.daiict.deep.service.UserService;
import in.ac.daiict.deep.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUser(String username) {
        return userRepo.findById(username).orElse(null);
    }

    @Transactional
    @Override
    public ResponseDto resetPassword(String username, String password) {
        int status= userRepo.updatePassword(username,passwordEncoder.encode(password));
        ResponseDto response;
        if(status==1) response=new ResponseDto(ResponseStatus.OK, ResponseMessage.SUCCESS_STATUS);
        else response = new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        return response;
    }

    @Override
    public boolean migrateUserData() {
        File file=new File("C:/flyway-scripts/V5__Users_Migration_Script.sql")  ;
        try {
            if(!file.exists()) file.createNewFile();
            PrintWriter printWriter=new PrintWriter(new FileWriter(file,false));
            printWriter.println("-- WARNING: This file is critical to application configuration.\n" +
                    "-- Manual changes are strongly discouraged as they may lead to system instability or configuration errors.");
            List<User> userList=userRepo.findAll();
            for(User user:userList){
                printWriter.println("INSERT INTO users(username,password,email,role) VALUES ('"+user.getUsername()+"','"
                        +user.getPassword()+"','"+user.getEmail()+"','"+user.getRole()+"');");
                printWriter.flush();
            }
            printWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
