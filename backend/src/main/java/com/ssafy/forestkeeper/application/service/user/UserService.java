package com.ssafy.forestkeeper.application.service.user;


import com.ssafy.forestkeeper.application.dto.request.user.UserLoginDTO;
import com.ssafy.forestkeeper.application.dto.request.user.UserSignUpDTO;

public interface UserService {
    Integer signUp(UserSignUpDTO userSignUpDTO);
    String login(UserLoginDTO userLoginDTO);
    String getUserEmail(String token);
    Integer modifyNickname(String nickname, String email);
    Integer modifyPassword(String past_password, String new_password, String email);
    boolean withdraw(String email);
    boolean checkNickname(String nickname);
    boolean checkEmail(String email);
    boolean isValidPassword(String password);
    boolean isValidName(String name);
    boolean isValidNickname(String nickname);
}
