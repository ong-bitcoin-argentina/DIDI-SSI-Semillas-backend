package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.action.ActionLevelEnum;
import com.atixlabs.semillasmiddleware.app.model.action.ActionLog;
import com.atixlabs.semillasmiddleware.app.model.action.ActionTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ActionLogRepositoryImpl implements  ActionLogRepositoryCustom {

    private String executionDateTime = "executionDateTime";

    @PersistenceContext
    protected EntityManager em;

    @Override
    public Page<ActionLog> find(Pageable page, String username, ActionLevelEnum level, ActionTypeEnum actionType, String message, Instant dateFrom, Instant dateTo){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ActionLog> cq = cb.createQuery(ActionLog.class);

        Root<ActionLog> actionLog = cq.from(ActionLog.class);
        List<Predicate> predicates = new ArrayList<>();

        if(username != null) {
            predicates.add(cb.like(cb.lower(actionLog.get("userName")), "%" + username.toLowerCase() + "%"));
        }

        if (level != null) {
            predicates.add(cb.equal(actionLog.get("level"), level));
        }

        if (actionType != null) {
            predicates.add(cb.equal(actionLog.get("actionType"), actionType));
        }

        if(message != null) {
            predicates.add(cb.like(cb.lower(actionLog.get("message")), "%" + message.toLowerCase() + "%"));
        }

        if (dateFrom != null) {
            predicates.add(cb.greaterThanOrEqualTo(actionLog.get(executionDateTime), dateFrom));
        }

        if (dateTo != null) {
            predicates.add(cb.lessThanOrEqualTo(actionLog.get(executionDateTime), dateTo));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        //order by updated field ASC
        Order lastUpdateOrder = cb.desc(actionLog.get(executionDateTime));
        cq.orderBy(lastUpdateOrder);

        if(page != null) {
            TypedQuery<ActionLog> typedQuery = em.createQuery(cq);
            typedQuery.setFirstResult(Math.toIntExact((page.getPageNumber()) * (long) page.getPageSize()));
            typedQuery.setMaxResults(page.getPageSize());
            return new PageImpl<>(typedQuery.getResultList(), page, 2);
        }else
            return Page.empty();

    }

}
