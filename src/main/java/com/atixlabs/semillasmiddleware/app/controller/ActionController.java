package com.atixlabs.semillasmiddleware.app.controller;

import com.atixlabs.semillasmiddleware.app.dto.ActionDto;
import com.atixlabs.semillasmiddleware.app.model.action.ActionLevelEnum;
import com.atixlabs.semillasmiddleware.app.model.action.ActionLog;
import com.atixlabs.semillasmiddleware.app.model.action.ActionTypeEnum;
import com.atixlabs.semillasmiddleware.app.service.ActionLogService;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(ActionController.URL_MAPPING)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PATCH})
public class ActionController {

    public static final String URL_MAPPING = "/action";

    private ActionLogService actionLogService;

    public ActionController(ActionLogService actionLogService){
        this.actionLogService = actionLogService;
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
                                        @RequestParam(required = false) Integer level,
                                        @RequestParam(required = false) Integer actionType,
                                        @RequestParam(required = false) String message,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo
                                          ) {

        Page<ActionDto> actions;
        try {
            log.info(String.format("find actions user &s, level %s, actiontType %s, message %s, dateFrom %s, dateTo %s", username, level, actionType, message, (dateFrom!=null ? dateFrom.toString():""), (dateTo!=null ? dateTo.toString():"")));

            actions = this.actionLogService.find(page, username, level, actionType, message, dateFrom, dateTo);

        } catch (Exception e) {
            log.error("There has been an error searching for action log with the filters ", e);
            return null;
        }
        return actions;
    }

    @GetMapping("/levels")
    public ActionLevelEnum[] getActionsLevel(){
        return ActionLevelEnum.values();
    }

    @GetMapping("/types")
    public ActionTypeEnum[] getActionsTypes(){
        return ActionTypeEnum.values();
    }



    //TODO delete
    @GetMapping("/save")
    public void save(){
        ActionLog actionLog = new ActionLog();
        actionLog.setActionType(ActionTypeEnum.DIDI_CREDENTIAL_REQUEST);
        actionLog.setExecutionDateTime(DateUtil.getLocalDateTimeNow());
        actionLog.setLevel(ActionLevelEnum.INFO);
        actionLog.setMessage("messge");
        actionLog.setUserName("username");
        this.actionLogService.save(actionLog);

        ActionLog actionLog2 = new ActionLog();
        actionLog2.setActionType(ActionTypeEnum.BONDAREA_SYNC);
        actionLog2.setExecutionDateTime(DateUtil.getLocalDateTimeNow().minusDays(8));
        actionLog2.setLevel(ActionLevelEnum.ERROR);
        actionLog2.setMessage("men sa je 2");
        actionLog2.setUserName("aaaabbbb");
        this.actionLogService.save(actionLog2);
    }



}
