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
        log.info("AOP addNewDidiAppUser");
    }

 
    @Pointcut("execution(* com.atixlabs.semillasmiddleware.app.didi.service.DidiAppUserService.registerNewAppUser(..))")
    public void registerNewAppUser() {}



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


}
