package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.dto.ActionDto;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ActionsService {

    @Value("${app.pageSize}")
    private String size;

    public Page<ActionDto>  find(Integer page, String username, String level, String actionType, String message, LocalDateTime dateFrom, LocalDateTime dateTo){

       // Page<Credential> credentials;
        Pageable pageable = null;
      //  if (page != null && page >= 0 && this.size != null)
        //    pageable = PageRequest.of(page, Integer.parseInt(size), Sort.by(Sort.Direction.ASC, "updated"));
        pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "updated"));

        List<ActionDto> actionDtos = this.getListMockActions();

        return new PageImpl<ActionDto>(actionDtos, pageable, 2);

        //CredentialPage credentialSet = new CredentialPage(pageDto, credentials.getNumberOfElements());

      //  return pageDto;
    }

    //[DIDI] - ERROR - Error de conexión con DIDI.
    private List<ActionDto> getListMockActions(){
        ActionDto actionDto = new ActionDto();
        actionDto.setActionType("DIDI");
        actionDto.setTexecutionDateTime(DateUtil.getLocalDateTimeNow());
        actionDto.setLevel("ERROR");
        actionDto.setMessage("Error de conexión con Didi");
        actionDto.setUser("admin");

        ActionDto actionDto2 = new ActionDto();
        actionDto2.setActionType("DIDI");
        actionDto2.setTexecutionDateTime(DateUtil.getLocalDateTimeNow());
        actionDto2.setLevel("INFO");
        actionDto2.setMessage("Sincronización DIDI OK");
        actionDto2.setUser("admin");

        ArrayList<ActionDto> actionDtos = new ArrayList<ActionDto>();
        actionDtos.add(actionDto);
        actionDtos.add(actionDto2);

        return actionDtos;
    }

}
