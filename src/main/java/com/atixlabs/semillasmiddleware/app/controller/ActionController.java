package com.atixlabs.semillasmiddleware.app.controller;

import com.atixlabs.semillasmiddleware.app.didi.dto.DidiAppUserDto;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiAppUserService;
import com.atixlabs.semillasmiddleware.app.dto.ActionDto;
import com.atixlabs.semillasmiddleware.app.dto.ActionFilterDto;
import com.atixlabs.semillasmiddleware.app.model.action.ActionLevelEnum;
import com.atixlabs.semillasmiddleware.app.model.action.ActionTypeEnum;
import com.atixlabs.semillasmiddleware.app.service.ActionLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(ActionController.URL_MAPPING)
@CrossOrigin(origins = {"http://localhost:8080", "${didi.server.url}"}, methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.PATCH})
public class ActionController {

    public static final String URL_MAPPING = "/action";

    private ActionLogService actionLogService;
    private DidiAppUserService didiAppUserService;

    @Autowired
    public ActionController(ActionLogService actionLogService,DidiAppUserService didiAppUserService){
        this.actionLogService = actionLogService;
        this.didiAppUserService = didiAppUserService;
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
                                        @RequestParam(required = false) Optional<String> username,
                                        @RequestParam(required = false) Optional<Integer> level,
                                        @RequestParam(required = false) Optional<Integer> actionType,
                                        @RequestParam(required = false) Optional<String> message,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<Instant> dateFrom,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Optional<Instant> dateTo
                                          ) {
        ActionFilterDto actionFilterDto = ActionFilterDto.builder()
                .username(username)
                .level(level)
                .actionType(actionType)
                .message(message)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .build();

        Page<ActionDto> actions;
        try {
            log.info(String.format("find actions user %s, level %s, actiontType %s, message %s, dateFrom %s, dateTo %s", username, level, actionType, message, dateFrom.toString(), dateTo.toString()));
            actions = this.actionLogService.find(page, actionFilterDto);
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

    @GetMapping("/test")
    public void getActionsTest(){
        log.info("--------------- test");
        DidiAppUserDto didiAppUser = new DidiAppUserDto(12345678L,"123wer");

        didiAppUserService.registerNewAppUser(didiAppUser);
        log.info("--------------- FIN test");
    }

    @GetMapping("/test2")
    public void getActionsTest2(){
        log.info("--------------- test");
        DidiAppUserDto didiAppUser = new DidiAppUserDto(12445678L,"1233wer");

        didiAppUserService.registerNewAppUser(didiAppUser);
        log.info("--------------- FIN test");
    }

}
