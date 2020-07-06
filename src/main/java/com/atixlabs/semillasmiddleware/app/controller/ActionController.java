package com.atixlabs.semillasmiddleware.app.controller;

import com.atixlabs.semillasmiddleware.app.dto.ActionDto;
import com.atixlabs.semillasmiddleware.app.service.ActionsService;
import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(ActionController.URL_MAPPING)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PATCH})
public class ActionController {

    public static final String URL_MAPPING = "/action";

    private ActionsService actionsService;

    public ActionController(ActionsService actionsService){
        this.actionsService = actionsService;
    }


    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    /**
     * [Nueva Credencial] - INFO -Cuando una credencial fue creada.
     * [Credencial Revocada] - INFO - Cuando una credencial fue revocada.
     * [Nueva Encuesta] - INFO - Cuando se dió de alta una encuesta.
     * [BONDAREA] - INFO - Cuando se realizó una sincronización con bondarea.
     * [DIDI] - INFO - Cuando se realizó una sincronización con DIDI.
     * [Solicitud Credenciales] - INFO - Se registró un nuevo DID-DNI con DNI: xxx y DID: xxx solicitando credenciales.
     * [BONDAREA] - ERROR - Error de conexión con bondarea.
     * [DIDI] - ERROR - Error de conexión con DIDI.
     */
    public Page<ActionDto> findAuditLog(@RequestParam(required = false)  @DefaultValue("0") Integer page,
                                        @RequestParam(required = false) String username,
                                        @RequestParam(required = false) String level,
                                        @RequestParam(required = false) String actionType,
                                        @RequestParam(required = false) String message,
                                        @RequestParam(required = false) LocalDateTime dateFrom,
                                        @RequestParam(required = false) LocalDateTime dateTo
                                          ) {

        Page<ActionDto> actions;
        try {
            log.info(String.format("find actions user &s, level %s, actiontType %s, message %s, dateFrom %s, dateTo %s", username, level, actionType, message, (dateFrom!=null ? dateFrom.toString():""), (dateTo!=null ? dateTo.toString():"")));

            actions = this.actionsService.find(page, username, level, actionType, message, dateFrom, dateTo);

        } catch (Exception e) {
            log.error("There has been an error searching for action log with the filters ", e);
            return null;
        }
        return actions;
    }

}
