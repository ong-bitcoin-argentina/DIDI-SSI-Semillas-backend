package com.atixlabs.semillasmiddleware.app.didi.service;

import com.atixlabs.semillasmiddleware.app.didi.constant.DidiSyncStatus;
import com.atixlabs.semillasmiddleware.app.didi.dto.*;
import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.didi.repository.DidiAppUserRepository;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.repository.CredentialIdentityRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialRepository;
import com.atixlabs.semillasmiddleware.app.repository.CredentialStateRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


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
            didiAppUser = new DidiAppUser();
            didiAppUser.loadFromDto(didiAppUserDto);
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
                    didiAppUser.loadFromDto(didiAppUserDto);
                    didiAppUserRepository.save(didiAppUser);
                    return "Se ha registrado una nueva solucitud de vinculacion de usuario con DID";
            }
        }
        else {
            //if DID is different requires sync:
            didiAppUser.loadFromDto(didiAppUserDto);
            didiAppUserRepository.save(didiAppUser);
            return "Se ha modificado el DID para un usuario que posee credenciales, se generarán nuevas credenciales.";
        }
    return "Ocurrio un error procesando la solicitud, intente nuevamente";
    }
}