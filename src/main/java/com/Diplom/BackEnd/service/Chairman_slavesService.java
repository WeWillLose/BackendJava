package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.User;

import java.util.Set;

public interface Chairman_slavesService {

    Chairman_Slaves getChairman_slavesBySlave(User user) throws NullPointerException;
    Chairman_Slaves getChairman_slavesByChairman(User user) throws NullPointerException;
    Chairman_Slaves getChairman_slavesByUser(User user) throws NullPointerException;
    Chairman_Slaves save(Chairman_Slaves chairman_slaves) throws NullPointerException;
    Chairman_Slaves setSlaves(Long id, Set<UserDTO> userDTO) throws MyException, NullPointerException;
    Chairman_Slaves setSlaves(User chairman, Set<UserDTO> userDTO) throws MyException, NullPointerException;
    Chairman_Slaves setChairman(Long id, UserDTO userDTO) throws MyException, NullPointerException;
    Chairman_Slaves setChairman(User slave, UserDTO userDTO) throws MyException, NullPointerException;
    Chairman_Slaves addSlave(User chairman, UserDTO slave) throws MyException, NullPointerException;
    Chairman_Slaves addSlave(User chairman, Set<UserDTO> slaves) throws MyException, NullPointerException;
}
