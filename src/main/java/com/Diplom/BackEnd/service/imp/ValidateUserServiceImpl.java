package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.PasswordResetDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.service.ValidateUserService;
import org.springframework.stereotype.Service;

@Service
public class ValidateUserServiceImpl implements ValidateUserService {


    public boolean validateUserDtoForUpdateInfo(UserDTO userDTO) {
        if(userDTO == null){
            return false;
        }
        return true;
    }
    public boolean validateUserDtoForPasswordResetDto(PasswordResetDTO passwordResetDTO){
        if(passwordResetDTO == null){
            return false;
        }
        if(!validateUserPassword(passwordResetDTO.getPassword())  ){
            return false;
        }
        return true;
    }
    public boolean validateUserPassword(String password){
        if(password == null || password.isBlank() || password.length() < 6){
            return false;
        }
        return true;
    }
    public boolean validateUserUsername(String username){
        if(username == null || username.isBlank() || username.length() < 4){
            return false;
        }
        return true;
    }

    public boolean validateUserFirstName(String firstName) {
        if(firstName == null || firstName.isBlank()){
            return false;
        }
        return true;
    }

    public boolean validateUserLastName(String lastName) {
        if(lastName == null || lastName.isBlank()){
            return false;
        }
        return true;
    }

    public boolean validateUserPatronymic(String patronymic) {
        if(patronymic == null || patronymic.isBlank()){
            return false;
        }
        return true;
    }
}
