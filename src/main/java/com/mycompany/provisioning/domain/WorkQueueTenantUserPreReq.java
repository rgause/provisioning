package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WorkQueueTenantUserPreReq.
 */
@Entity
@Table(name = "work_queue_tenant_user_pre_req")
public class WorkQueueTenantUserPreReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "pre_requisite_item", precision = 21, scale = 2, nullable = false)
    private BigDecimal preRequisiteItem;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workQueueTenantUserData", "workQueueTenantUserPreReqs", "tenantUser" }, allowSetters = true)
    private WorkQueueTenantUser workQueueTenantUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkQueueTenantUserPreReq id(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getPreRequisiteItem() {
        return this.preRequisiteItem;
    }

    public WorkQueueTenantUserPreReq preRequisiteItem(BigDecimal preRequisiteItem) {
        this.preRequisiteItem = preRequisiteItem;
        return this;
    }

    public void setPreRequisiteItem(BigDecimal preRequisiteItem) {
        this.preRequisiteItem = preRequisiteItem;
    }

    public WorkQueueTenantUser getWorkQueueTenantUser() {
        return this.workQueueTenantUser;
    }

    public WorkQueueTenantUserPreReq workQueueTenantUser(WorkQueueTenantUser workQueueTenantUser) {
        this.setWorkQueueTenantUser(workQueueTenantUser);
        return this;
    }

    public void setWorkQueueTenantUser(WorkQueueTenantUser workQueueTenantUser) {
        this.workQueueTenantUser = workQueueTenantUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkQueueTenantUserPreReq)) {
            return false;
        }
        return id != null && id.equals(((WorkQueueTenantUserPreReq) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkQueueTenantUserPreReq{" +
            "id=" + getId() +
            ", preRequisiteItem=" + getPreRequisiteItem() +
            "}";
    }
}
