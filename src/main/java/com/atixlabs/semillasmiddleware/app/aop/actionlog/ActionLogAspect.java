package com.atixlabs.semillasmiddleware.app.aop.actionlog;

import com.atixlabs.semillasmiddleware.app.didi.dto.DidiAppUserDto;
import com.atixlabs.semillasmiddleware.app.didi.model.constant.DidiAppUserOperationResult;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiAppUserService;
import com.atixlabs.semillasmiddleware.app.model.action.ActionLevelEnum;
import com.atixlabs.semillasmiddleware.app.model.action.ActionTypeEnum;
import com.atixlabs.semillasmiddleware.app.service.ActionLogService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class ActionLogAspect {

    @Autowired
    private ActionLogService actionLogService;

    @Autowired
    public ActionLogAspect(ActionLogService actionLogService){
        this.actionLogService = actionLogService;
    }


    @Pointcut("execution(* com.atixlabs.semillasmiddleware.app.didi.service.DidiAppUserService.registerNewAppUser(..))")
    public void registerNewAppUser() {}

    @Pointcut("execution(* com.atixlabs.semillasmiddleware.app.bondarea.service.synchronizeLoans(..))")
    public void syncBondarea() {}



    @AfterReturning(value = "registerNewAppUser()", returning = "returnValue")
    public void registerNewAppUserAfter(JoinPoint joinPoint,  Object returnValue)  {

        try {
            Object[] lArgs = joinPoint.getArgs();
            DidiAppUserDto didiAppUserDto = (DidiAppUserDto) lArgs[0];

            DidiAppUserOperationResult didiAppUserOperationResult = (DidiAppUserOperationResult)returnValue;

            if(didiAppUserOperationResult.equals(DidiAppUserOperationResult.NEW_USER_REGISTER_OK)) {

                String message = String.format("Se registró el id Didi %s para el dni %d", didiAppUserDto.getDid(), didiAppUserDto.getDni());
                this.actionLogService.registerAction(ActionTypeEnum.DIDI_CREDENTIAL_REQUEST, ActionLevelEnum.INFO, message);

            }else{
                log.debug(String.format("AOP - registerNewAppUserAfter obviated, Operation %s",didiAppUserOperationResult.name()));
            }
        }catch (Exception ex){
            log.error("AOP ERROR - registerNewAppUserAfter ",ex);
        }

    }

    @Around(value = "syncBondarea()")
    public Object syncBondareaAround(final ProceedingJoinPoint joinPoint) throws Throwable{
        Object value;

        try {
            value = joinPoint.proceed();

            if(((Boolean) value)){
                this.registerSyncBondareaOk();
            }else{
                this.registerSyncBondareaError();
            }

        } catch (Throwable throwable) {
            this.registerSyncBondareaError();
            throw throwable;
        }

        return value;
    }

    private void registerSyncBondareaOk() {
            String message = "Sincronización con Bondarea finalizada Correctamente";
            this.saveActionLog(ActionTypeEnum.BONDAREA_SYNC, ActionLevelEnum.INFO, message);
    }

    private void registerSyncBondareaError() {
        String message = "Sincronización con Bondarea finalizada con errores";
        this.saveActionLog(ActionTypeEnum.BONDAREA_SYNC, ActionLevelEnum.ERROR, message);
    }

    private void saveActionLog(ActionTypeEnum actionType, ActionLevelEnum actionLevel, String message){

        try {
            this.actionLogService.registerAction(actionType, actionLevel, message);
        } catch (Exception e) {
            log.error("ERROR when save Action Log {} {} {} ", actionType, actionLevel, message, e);
        }

    }


}
