package in.ac.daiict.deep.service.impl;

import in.ac.daiict.deep.constant.response.ResponseMessage;
import in.ac.daiict.deep.constant.response.ResponseStatus;
import in.ac.daiict.deep.entity.User;
import in.ac.daiict.deep.repository.UserRepo;
import in.ac.daiict.deep.service.UserService;
import in.ac.daiict.deep.dto.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUser(String username) {
        return userRepo.findById(username).orElse(null);
    }

    @Override
    public ResponseDto resetPassword(String username, String password) {
        int status= userRepo.updatePassword(username,passwordEncoder.encode(password));
        ResponseDto response;
        if(status==1) response=new ResponseDto(ResponseStatus.OK, ResponseMessage.SUCCESS_STATUS);
        else response = new ResponseDto(ResponseStatus.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);
        return response;
    }
}
