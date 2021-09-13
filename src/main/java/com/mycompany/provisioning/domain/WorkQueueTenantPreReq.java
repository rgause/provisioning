package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WorkQueueTenantPreReq.
 */
@Entity
@Table(name = "work_queue_tenant_pre_req")
public class WorkQueueTenantPreReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "pre_requisite_item", precision = 21, scale = 2, nullable = false)
    private BigDecimal preRequisiteItem;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workQueueTenantData", "workQueueTenantPreReqs", "tenant" }, allowSetters = true)
    private WorkQueueTenant workQueueItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkQueueTenantPreReq id(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getPreRequisiteItem() {
        return this.preRequisiteItem;
    }

    public WorkQueueTenantPreReq preRequisiteItem(BigDecimal preRequisiteItem) {
        this.preRequisiteItem = preRequisiteItem;
        return this;
    }

    public void setPreRequisiteItem(BigDecimal preRequisiteItem) {
        this.preRequisiteItem = preRequisiteItem;
    }

    public WorkQueueTenant getWorkQueueItem() {
        return this.workQueueItem;
    }

    public WorkQueueTenantPreReq workQueueItem(WorkQueueTenant workQueueTenant) {
        this.setWorkQueueItem(workQueueTenant);
        return this;
    }

    public void setWorkQueueItem(WorkQueueTenant workQueueTenant) {
        this.workQueueItem = workQueueTenant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkQueueTenantPreReq)) {
            return false;
        }
        return id != null && id.equals(((WorkQueueTenantPreReq) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkQueueTenantPreReq{" +
            "id=" + getId() +
            ", preRequisiteItem=" + getPreRequisiteItem() +
            "}";
    }
}
