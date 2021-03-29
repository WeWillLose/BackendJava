package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.LoginDTO;
import com.Diplom.BackEnd.dto.SignupDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.model.User;

public interface AuthService {
    User authenticateUser(LoginDTO loginDTO) throws MyException;
    User registerUser (SignupDTO signupDTO) throws MyException;
}
