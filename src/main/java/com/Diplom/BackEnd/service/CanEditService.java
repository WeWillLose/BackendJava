package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.model.ToDo;
import com.Diplom.BackEnd.model.User;

public interface CanEditService {
    boolean canEdit(User owner);
}
