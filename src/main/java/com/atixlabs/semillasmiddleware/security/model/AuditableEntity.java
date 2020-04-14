package com.atixlabs.semillasmiddleware.security.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class AuditableEntity implements Serializable {

  @Column(name = "CREATED", updatable = false)
  @CreationTimestamp
  LocalDateTime created;

  @Column(name = "UPDATED")
  @UpdateTimestamp
  LocalDateTime updated;
}
