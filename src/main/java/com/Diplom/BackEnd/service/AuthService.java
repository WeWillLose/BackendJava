package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.LoginDTO;
import com.Diplom.BackEnd.dto.SignupDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;

public interface AuthService {
    UserDTO authenticateUser(LoginDTO loginDTO) throws MyException;
    UserDTO registerUser (SignupDTO signupDTO) throws MyException;
}
