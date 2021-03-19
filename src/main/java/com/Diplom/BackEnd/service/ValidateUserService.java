package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.PasswordResetDTO;
import com.Diplom.BackEnd.dto.UserDTO;

public interface ValidateUserService {
    boolean validateUserDtoForUpdateInfo(UserDTO userDTO);

    boolean validateUserDtoForPasswordResetDto(PasswordResetDTO passwordResetDTO);

    boolean validateUserPassword(String password);

    boolean validateUserUsername(String username);

    boolean validateUserFirstName(String firstName);

    boolean validateUserLastName(String lastName);

    boolean validateUserPatronymic(String patronymic);
}
