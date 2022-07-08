package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.action.ActionLevelEnum;
import com.atixlabs.semillasmiddleware.app.model.action.ActionLog;
import com.atixlabs.semillasmiddleware.app.model.action.ActionTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface ActionLogRepositoryCustom {

    Page<ActionLog> find(Pageable page, String username, ActionLevelEnum level, ActionTypeEnum actionType, String message, Instant dateFrom, Instant dateTo);

}
