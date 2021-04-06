package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.ToDo;
import com.Diplom.BackEnd.model.User;

public interface CanEditService {
    boolean canEdit(User editor,User owner);
    boolean canEditOnlyAdmin(User user);
    boolean canEditReportWithChairman(User editor, Report report);
}
