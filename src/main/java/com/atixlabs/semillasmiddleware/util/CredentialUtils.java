package com.atixlabs.semillasmiddleware.util;

import com.atixlabs.semillasmiddleware.app.exceptions.CredentialNotExistsException;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CredentialUtils {

    private CredentialUtils(){}

    public static CredentialTypesCodes getCredentialCode(Credential credential) {
        //get the credential type
        try {
            return CredentialTypesCodes.getEnumByStringValue(credential.getCredentialDescription());
        } catch (IllegalArgumentException ex) {
            log.error("Impossible to revoke credential. There is no credential with type {}", credential.getCredentialDescription());
            throw new CredentialNotExistsException("No se encontró la credencial solicitada, con el estado " + credential.getCredentialDescription());
        }
    }

    public static  <T> List<T> wrapList(List<T> childs, List<T> familyMembers){
        List<T> allFamily = childs;
        allFamily.addAll(familyMembers);
        return allFamily;
    }
}
