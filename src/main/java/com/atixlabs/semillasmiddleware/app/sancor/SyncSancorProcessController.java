package com.atixlabs.semillasmiddleware.app.sancor;

import com.atixlabs.semillasmiddleware.app.didi.controller.SyncDidiProcessController;
import com.atixlabs.semillasmiddleware.app.dto.ProcessResultDto;
import com.atixlabs.semillasmiddleware.app.sancor.service.SyncSancorProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(SyncSancorProcessController.URL_MAPPING_SANCOR)
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@Slf4j
public class SyncSancorProcessController {

    public static final String URL_MAPPING_SANCOR = "/syncsancor";

    private SyncSancorProcessService syncSancorProcessService;

    @Autowired
    public SyncSancorProcessController(SyncSancorProcessService syncSancorProcessService){
        this.syncSancorProcessService = syncSancorProcessService;
    }

    @GetMapping("/policy/update")
    @ResponseStatus(HttpStatus.OK)
    public ProcessResultDto processPoliciesUpdated(){
        Map<String, String> jsonMessage = new HashMap<>();
        ProcessResultDto processResultDto = null;

            processResultDto = this.syncSancorProcessService.processSancorPoliciesUpdated();


        jsonMessage.put("message", "PoliciesUpdated OK");
        return processResultDto;
    }
}
