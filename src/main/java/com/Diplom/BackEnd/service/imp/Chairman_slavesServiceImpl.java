package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.Chairman_slavesRepo;
import com.Diplom.BackEnd.service.Chairman_slavesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Chairman_slavesServiceImpl implements Chairman_slavesService {

    @Autowired
    private Chairman_slavesRepo chairman_slavesRepo;

    public Chairman_Slaves getChairman_slavesBySlave(User user) throws NullPointerException{
        if(user == null){
            throw new NullPointerException("user must not be null");
        }
        Chairman_Slaves bySlavesContains = chairman_slavesRepo.findBySlavesContains(user);
        log.info("IN getChairman_slaves found {} by {}",bySlavesContains,user);
        return bySlavesContains;
    }
    public Chairman_Slaves getChairman_slavesByChairman(User user) throws NullPointerException{
        if(user == null){
            throw new NullPointerException("user must not be null");
        }
        Chairman_Slaves bySlavesContains = chairman_slavesRepo.findByChairman_Username(user.getUsername());
        log.info("IN getChairman_slaves found {} by {}",bySlavesContains,user);
        return bySlavesContains;
    }
}
