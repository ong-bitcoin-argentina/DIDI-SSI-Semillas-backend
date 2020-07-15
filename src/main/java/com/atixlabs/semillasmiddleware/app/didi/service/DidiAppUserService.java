package com.atixlabs.semillasmiddleware.app.didi.service;

import com.atixlabs.semillasmiddleware.app.didi.constant.DidiSyncStatus;
import com.atixlabs.semillasmiddleware.app.didi.dto.*;
import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.didi.model.constant.DidiAppUserOperationResult;
import com.atixlabs.semillasmiddleware.app.didi.repository.DidiAppUserRepository;
import com.atixlabs.semillasmiddleware.app.model.action.ActionLevelEnum;
import com.atixlabs.semillasmiddleware.app.model.action.ActionTypeEnum;
import com.atixlabs.semillasmiddleware.app.service.ActionLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Slf4j
@Service
public class DidiAppUserService {



    private DidiAppUserRepository didiAppUserRepository;

    @Autowired
    public DidiAppUserService(DidiAppUserRepository didiAppUserRepository) {
        this.didiAppUserRepository = didiAppUserRepository;
    }

/*
    private void registerNewAppDidiUserAction(DidiAppUserDto didiAppUserDto){

        String message = String.format("Se registró el id Didi %s para el dni %d",didiAppUserDto.getDid(),didiAppUserDto.getDni());
        //TODO to aop
        this.actionLogService.registerAction(ActionTypeEnum.DIDI_CREDENTIAL_REQUEST, ActionLevelEnum.INFO,message);
    }*/

    public DidiAppUserOperationResult addNewDidiAppUser(DidiAppUserDto didiAppUserDto){

        log.info("addNewDidiAppUser");

        DidiAppUser didiAppUser = new DidiAppUser(didiAppUserDto);
        didiAppUserRepository.save(didiAppUser);
        //TODO to aop
        // this.registerNewAppDidiUserAction(didiAppUserDto);
        return DidiAppUserOperationResult.NEW_USER_REGISTER_OK;
    }

    public DidiAppUserOperationResult registerNewAppUser(DidiAppUserDto didiAppUserDto) {

        DidiAppUser didiAppUser  = didiAppUserRepository.findByDni(didiAppUserDto.getDni());


        //if DNI is new.
        if (didiAppUser == null) {
            return this.addNewDidiAppUser(didiAppUserDto);
        }

        if (didiAppUser.getDid().equals(didiAppUserDto.getDid())) {
            //if DID is the same:
            switch (DidiSyncStatus.getEnumByStringValue(didiAppUser.getSyncStatus())) {
                case SYNC_OK:
                case SYNC_MISSING:
                    return DidiAppUserOperationResult.USER_ALREADY_EXIST_NO_CHANGES;

                case SYNC_ERROR:
                    didiAppUser.setSyncStatus(DidiSyncStatus.SYNC_MISSING.getCode());
                    didiAppUserRepository.save(didiAppUser);
                    return DidiAppUserOperationResult.NEW_REQUEST_REGISTERED;
            }
        }
        else {
            //if DID is different requires sync:
            didiAppUser.setDid(didiAppUserDto.getDid());
            didiAppUser.setSyncStatus(DidiSyncStatus.SYNC_MISSING.getCode());
            didiAppUserRepository.save(didiAppUser);
            return DidiAppUserOperationResult.NEW_DID_REGISTERED_FOR_USER;
        }
    return DidiAppUserOperationResult.ERROR;
    }


    public boolean updateAppUserStatusByCode(Long creditHolderDni, String syncStatusCode) {

        DidiAppUser didiAppUser = didiAppUserRepository.findByDni(creditHolderDni);

        if (didiAppUser != null){
            didiAppUser.setSyncStatus(syncStatusCode);
            didiAppUserRepository.save(didiAppUser);
            return true;
        }
        return false;
    }

    public DidiAppUser getDidiAppUserByDni(Long dni){
        return didiAppUserRepository.findByDni(dni);
    }

}