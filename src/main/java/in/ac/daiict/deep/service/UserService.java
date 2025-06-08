package in.ac.daiict.deep.service;

import in.ac.daiict.deep.entity.User;
import in.ac.daiict.deep.dto.ResponseDto;

public interface UserService {
    User findUser(String username);
    ResponseDto resetPassword(String username, String password);
}
