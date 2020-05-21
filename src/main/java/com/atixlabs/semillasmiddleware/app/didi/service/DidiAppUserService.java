package com.atixlabs.semillasmiddleware.app.didi.service;

import com.atixlabs.semillasmiddleware.app.didi.constant.DidiSyncStatus;
import com.atixlabs.semillasmiddleware.app.didi.dto.*;
import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.didi.repository.DidiAppUserRepository;
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

    public String registerNewAppUser(DidiAppUserDto didiAppUserDto) {

        DidiAppUser didiAppUser  = didiAppUserRepository.findByDni(didiAppUserDto.getDni());

        //if DNI is new.
        if (didiAppUser == null) {
            didiAppUser = new DidiAppUser(didiAppUserDto);
            didiAppUserRepository.save(didiAppUser);
            return "El nuevo usuario se registro correctamente.";
        }

        if (didiAppUser.getDid().equals(didiAppUserDto.getDid())) {
            //if DID is the same:
            switch (DidiSyncStatus.getEnumByStringValue(didiAppUser.getSyncStatus())) {
                case SYNC_OK:
                case SYNC_MISSING:
                    return "El usuario con Dni: " + didiAppUser.getDni() + " ya posee sus credenciales validadas o en espera con Didi, no se realizó ninguna operación";

                case SYNC_ERROR:
                    didiAppUser.setSyncStatus(DidiSyncStatus.SYNC_MISSING.getCode());
                    didiAppUserRepository.save(didiAppUser);
                    return "Se ha registrado una nueva solucitud de vinculacion de usuario con DID";
            }
        }
        else {
            //if DID is different requires sync:
            didiAppUser.setDid(didiAppUserDto.getDid());
            didiAppUser.setSyncStatus(DidiSyncStatus.SYNC_MISSING.getCode());
            didiAppUserRepository.save(didiAppUser);
            return "Se ha modificado el DID para un usuario que posee credenciales, se generarán nuevas credenciales.";
        }
    return "Ocurrio un error procesando la solicitud, intente nuevamente";
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

}