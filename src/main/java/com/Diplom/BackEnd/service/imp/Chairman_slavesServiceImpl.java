package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.BadRequestImpl;
import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.Chairman_slavesRepo;
import com.Diplom.BackEnd.service.Chairman_slavesService;
import com.Diplom.BackEnd.service.UserDTOMapperService;
import com.Diplom.BackEnd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class Chairman_slavesServiceImpl implements Chairman_slavesService {

    @Autowired
    private Chairman_slavesRepo chairman_slavesRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDTOMapperService userDTOMapperService;

    public Chairman_Slaves getChairman_slavesBySlave(User user) throws NullPointerException{
        if(user == null){
            throw new NullPointerException("user must not be null");
        }
        Chairman_Slaves bySlavesContains = chairman_slavesRepo.findBySlavesContains(user);
        log.info("IN getChairman_slavesBySlave found {} by {}",bySlavesContains,user);
        return bySlavesContains;
    }
    public Chairman_Slaves getChairman_slavesByChairman(User user) throws NullPointerException{
        if(user == null){
            throw new NullPointerException("user must not be null");
        }
        Chairman_Slaves bySlavesContains = chairman_slavesRepo.findByChairman_Username(user.getUsername());
        log.info("IN getChairman_slavesByChairman found {} by {}",bySlavesContains,user);
        return bySlavesContains;
    }
    public Chairman_Slaves getChairman_slavesByUser(User user) throws NullPointerException{
        if(user == null){
            throw new NullPointerException("user must not be null");
        }
        Chairman_Slaves chairman_slaves = getChairman_slavesBySlave(user);
        if (chairman_slaves == null){
            chairman_slaves = getChairman_slavesByChairman(user);
            if(chairman_slaves == null){
                log.info("IN getChairman_slavesByUser found {} by {}",chairman_slaves,user);
                return new Chairman_Slaves();
            }
        }
        return chairman_slaves;
    }

    public Chairman_Slaves save(Chairman_Slaves chairman_slaves)  throws NullPointerException{
        if(chairman_slaves == null){
            throw new NullPointerException("user must not be null");
        }
        return chairman_slavesRepo.save(chairman_slaves);
    }

    @Override
    public Chairman_Slaves setSlaves(Long id, Set<UserDTO> slaves) throws MyException {
        if(id== null){
            throw new NullPointerException("id must not be null");
        }
        if(slaves == null){
            throw new NullPointerException("slaves must not be null");
        }
        User chairman = userService.findById(id);
        if(chairman==null){
            throw new BadRequestImpl();
        }
        return setSlaves(chairman,slaves);

    }

    @Override
    public Chairman_Slaves setSlaves(User chairman, Set<UserDTO> slaves) throws MyException, NullPointerException {
        if(chairman == null){
            throw new NullPointerException("chairman must not be null");
        }
        if(slaves == null){
            throw new NullPointerException("slaves must not be null");
        }
        Set<User> setSlaves = slaves.stream()
                .map(UserDTO::getUsername)
                .map(t -> {
                    User byUsername = userService.findByUsername(t);
                    if (byUsername == null) {
                        throw new BadRequestImpl();
                    }
                    return byUsername;
                })
                .collect(Collectors.toSet());
        Chairman_Slaves chairman_slaves = new Chairman_Slaves(chairman, setSlaves);
        return save(chairman_slaves);
    }

    @Override
    public Chairman_Slaves setChairman(Long id, UserDTO chairman) throws MyException {
        if(id== null){
            throw new NullPointerException("id must not be null");
        }
        if(chairman == null){
            throw new NullPointerException("chairman must not be null");
        }
        User slave = userService.findById(id);
        if(slave==null){
            throw new BadRequestImpl();
        }
        return setChairman(slave,chairman);
    }

    @Override
    public Chairman_Slaves setChairman(User slave, UserDTO chairman) throws MyException, NullPointerException {
        Chairman_Slaves chairman_slavesByUser = getChairman_slavesByUser(slave);
        chairman_slavesByUser.setChairman(userService.findByUsername(chairman.getUsername()));
        chairman_slavesByUser.setSlaves(Collections.singleton(slave));
        return save(chairman_slavesByUser);
    }

    @Override
    public Chairman_Slaves addSlave(User chairman, Set<UserDTO> slaves) throws MyException, NullPointerException {
        Chairman_Slaves chairman_slavesByUser = getChairman_slavesByUser(chairman);

        Set<User> setSlaves = slaves.stream()
                .map(UserDTO::getUsername)
                .map(t -> {
                    User byUsername = userService.findByUsername(t);
                    if (byUsername == null) {
                        throw new BadRequestImpl();
                    }
                    return byUsername;
                })
                .collect(Collectors.toSet());
        chairman_slavesByUser.getSlaves().addAll(setSlaves);
        return save(chairman_slavesByUser);
    }

    @Override
    public Chairman_Slaves addSlave(User chairman, UserDTO slave) throws MyException, NullPointerException {
        Chairman_Slaves chairman_slavesByUser = getChairman_slavesByUser(chairman);
        chairman_slavesByUser.getSlaves().add(userService.findByUsername(slave.getUsername()));
        return save(chairman_slavesByUser);
    }
}
