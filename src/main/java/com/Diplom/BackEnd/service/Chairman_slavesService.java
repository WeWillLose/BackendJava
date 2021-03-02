package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.User;

public interface Chairman_slavesService {

    Chairman_Slaves getChairman_slavesBySlave(User user) throws NullPointerException;
    Chairman_Slaves getChairman_slavesByChairman(User user) throws NullPointerException;
}
