package com.atixlabs.semillasmiddleware.app.aop.actionlog;

import com.atixlabs.semillasmiddleware.app.model.action.ActionLevelEnum;
import com.atixlabs.semillasmiddleware.app.model.action.ActionTypeEnum;
import com.atixlabs.semillasmiddleware.app.service.ActionLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ActionLogAspectTest {
    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private ActionLogService actionLogService;

    @InjectMocks
    private ActionLogAspect actionLogAspect;

    @Before
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void executeSyncBondareaOkThenTrue() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenReturn(true);
        Boolean result = (Boolean) actionLogAspect.syncBondareaAround(proceedingJoinPoint);
        verify(proceedingJoinPoint, times(1)).proceed();
        verify(actionLogService, times(1)).registerAction(ActionTypeEnum.BONDAREA_SYNC, ActionLevelEnum.INFO, "Sincronización con Bondarea finalizada Correctamente");
        Assert.assertTrue(result);
    }

    @Test
    public void executeSyncBondareaNotOkThenFalse() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenReturn(false);

        Boolean result = (Boolean) actionLogAspect.syncBondareaAround(proceedingJoinPoint);
        verify(proceedingJoinPoint, times(1)).proceed();
        verify(actionLogService, times(1)).registerAction(ActionTypeEnum.BONDAREA_SYNC, ActionLevelEnum.ERROR, "Sincronización con Bondarea finalizada con errores");

        Assert.assertFalse(result);

    }

    @Test(expected = Exception.class)
    public void executeSyncBondareaNotOkThenExeption() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenThrow(new Exception());

        Boolean result = (Boolean) actionLogAspect.syncBondareaAround(proceedingJoinPoint);

        verify(proceedingJoinPoint, times(1)).proceed();
        verify(actionLogService, times(1)).registerAction(ActionTypeEnum.BONDAREA_SYNC, ActionLevelEnum.ERROR, "Sincronización con Bondarea finalizada con errores");


    }

    @Test
    public void executeSyncBondareaSaveExeptionThenOk() throws Throwable {
        when(proceedingJoinPoint.proceed()).thenReturn(true);
        when(actionLogService.registerAction(any(),any(), any())).thenThrow(new RuntimeException());

        Boolean result = (Boolean) actionLogAspect.syncBondareaAround(proceedingJoinPoint);

        verify(proceedingJoinPoint, times(1)).proceed();
        Assert.assertTrue(result);

    }
}
