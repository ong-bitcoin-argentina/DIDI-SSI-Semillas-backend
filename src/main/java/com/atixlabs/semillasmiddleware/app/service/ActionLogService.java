package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.repository.ActionLogRepository;
import com.atixlabs.semillasmiddleware.app.dto.ActionDto;
import com.atixlabs.semillasmiddleware.app.dto.ActionFilterDto;
import com.atixlabs.semillasmiddleware.app.model.action.ActionLevelEnum;
import com.atixlabs.semillasmiddleware.app.model.action.ActionLog;
import com.atixlabs.semillasmiddleware.app.model.action.ActionTypeEnum;
import com.atixlabs.semillasmiddleware.security.util.AuthUtil;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class ActionLogService {

    private ActionLogRepository actionLogRepository;

    private AuthUtil authUtil;

    @Autowired
    public ActionLogService(ActionLogRepository actionLogRepository, AuthUtil authUtil){
        this.actionLogRepository = actionLogRepository;
        this.authUtil = authUtil;
    }

    private Specification<ActionLog> getActionSpecification (ActionFilterDto actionFilterDto) {
        return (Specification<ActionLog>) (root, query, cb) -> {
            Stream<Predicate> predicates = Stream.of(
                    actionFilterDto.getLevel().map(value -> cb.equal(root.get("level"), value)),
                    actionFilterDto.getActionType().map(value -> cb.equal(root.get("actionType"), value)),
                    actionFilterDto.getUsername().map(value -> {
                    String username = "%" + value + "%";
                        return cb.like(root.get("userName"), username);
                    }),
                    actionFilterDto.getMessage().map(value -> {
                        String message = "%" + value + "%";
                        return cb.like(cb.upper(root.get("message")), message.toUpperCase());
                    }),
                    actionFilterDto.getDateFrom().map(value -> cb.greaterThanOrEqualTo(root.get(Constants.EXECUTION_DATE_TIME), value)),
                    actionFilterDto.getDateTo().map(value ->cb.lessThanOrEqualTo(root.get(Constants.EXECUTION_DATE_TIME), value))
            ).flatMap(Optional::stream);
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    @Value("${app.pageSize}")
    private String size;

    public Page<ActionDto>  find(Integer page, ActionFilterDto actionFilterDto){
        Pageable pageable = PageRequest.of(page, Integer.parseInt(size), Sort.by(Constants.EXECUTION_DATE_TIME).descending());
        return actionLogRepository.findAll(getActionSpecification(actionFilterDto), pageable)
                .map(ActionDto::new);
    }

    public ActionLog save(ActionLog actionLog){

        return  this.actionLogRepository.save(actionLog);
    }


    public ActionLog registerAction(ActionTypeEnum type, ActionLevelEnum level, String message){
        ActionLog actionLog = new ActionLog();
        String username = this.getCurrentUsername();
        log.info(username);
        if(username==null) username = Constants.CURRENT_USER_NAME;
        actionLog.setUserName(username);
        log.info(actionLog.getUserName());
        actionLog.setActionType(type);
        actionLog.setLevel(level);
        actionLog.setMessage(message);
        actionLog.setExecutionDateTime(DateUtil.getInstantNow());

        actionLog = actionLogRepository.save(actionLog);

        log.debug(String.format("new action log register type: %s, level: %s, message: %s",type.getDescription(), level.getDescription(), message));
        return actionLog;

    }

    private String getCurrentUsername(){
        String currentUsername = null;
        try{
            currentUsername = authUtil.getCurrentUsername();
        }catch (Exception e){
            log.error("Can't get username logged ",e);
        }

        return currentUsername;
    }

    private static class Constants{
        public static final String EXECUTION_DATE_TIME = "executionDateTime";
        public static final String CURRENT_USER_NAME = "desconocido";
    }

}
