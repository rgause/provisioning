package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WorkQueueTenant.
 */
@Entity
@Table(name = "work_queue_tenant")
public class WorkQueueTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "task", nullable = false)
    private String task;

    @NotNull
    @Column(name = "required_to_complete", nullable = false)
    private Boolean requiredToComplete;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_cancelled")
    private LocalDate dateCancelled;

    @Column(name = "date_completed")
    private LocalDate dateCompleted;

    @NotNull
    @Column(name = "sequence_order", precision = 21, scale = 2, nullable = false)
    private BigDecimal sequenceOrder;

    @OneToMany(mappedBy = "workQueueTenant")
    @JsonIgnoreProperties(value = { "workQueueTenant" }, allowSetters = true)
    private Set<WorkQueueTenantData> workQueueTenantData = new HashSet<>();

    @OneToMany(mappedBy = "workQueueItem")
    @JsonIgnoreProperties(value = { "workQueueItem" }, allowSetters = true)
    private Set<WorkQueueTenantPreReq> workQueueTenantPreReqs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "tenantProperties", "tenantUsers", "workQueueTenants", "tenantType" }, allowSetters = true)
    private Tenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkQueueTenant id(Long id) {
        this.id = id;
        return this;
    }

    public String getTask() {
        return this.task;
    }

    public WorkQueueTenant task(String task) {
        this.task = task;
        return this;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Boolean getRequiredToComplete() {
        return this.requiredToComplete;
    }

    public WorkQueueTenant requiredToComplete(Boolean requiredToComplete) {
        this.requiredToComplete = requiredToComplete;
        return this;
    }

    public void setRequiredToComplete(Boolean requiredToComplete) {
        this.requiredToComplete = requiredToComplete;
    }

    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    public WorkQueueTenant dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateCancelled() {
        return this.dateCancelled;
    }

    public WorkQueueTenant dateCancelled(LocalDate dateCancelled) {
        this.dateCancelled = dateCancelled;
        return this;
    }

    public void setDateCancelled(LocalDate dateCancelled) {
        this.dateCancelled = dateCancelled;
    }

    public LocalDate getDateCompleted() {
        return this.dateCompleted;
    }

    public WorkQueueTenant dateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
        return this;
    }

    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public BigDecimal getSequenceOrder() {
        return this.sequenceOrder;
    }

    public WorkQueueTenant sequenceOrder(BigDecimal sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
        return this;
    }

    public void setSequenceOrder(BigDecimal sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public Set<WorkQueueTenantData> getWorkQueueTenantData() {
        return this.workQueueTenantData;
    }

    public WorkQueueTenant workQueueTenantData(Set<WorkQueueTenantData> workQueueTenantData) {
        this.setWorkQueueTenantData(workQueueTenantData);
        return this;
    }

    public WorkQueueTenant addWorkQueueTenantData(WorkQueueTenantData workQueueTenantData) {
        this.workQueueTenantData.add(workQueueTenantData);
        workQueueTenantData.setWorkQueueTenant(this);
        return this;
    }

    public WorkQueueTenant removeWorkQueueTenantData(WorkQueueTenantData workQueueTenantData) {
        this.workQueueTenantData.remove(workQueueTenantData);
        workQueueTenantData.setWorkQueueTenant(null);
        return this;
    }

    public void setWorkQueueTenantData(Set<WorkQueueTenantData> workQueueTenantData) {
        if (this.workQueueTenantData != null) {
            this.workQueueTenantData.forEach(i -> i.setWorkQueueTenant(null));
        }
        if (workQueueTenantData != null) {
            workQueueTenantData.forEach(i -> i.setWorkQueueTenant(this));
        }
        this.workQueueTenantData = workQueueTenantData;
    }

    public Set<WorkQueueTenantPreReq> getWorkQueueTenantPreReqs() {
        return this.workQueueTenantPreReqs;
    }

    public WorkQueueTenant workQueueTenantPreReqs(Set<WorkQueueTenantPreReq> workQueueTenantPreReqs) {
        this.setWorkQueueTenantPreReqs(workQueueTenantPreReqs);
        return this;
    }

    public WorkQueueTenant addWorkQueueTenantPreReq(WorkQueueTenantPreReq workQueueTenantPreReq) {
        this.workQueueTenantPreReqs.add(workQueueTenantPreReq);
        workQueueTenantPreReq.setWorkQueueItem(this);
        return this;
    }

    public WorkQueueTenant removeWorkQueueTenantPreReq(WorkQueueTenantPreReq workQueueTenantPreReq) {
        this.workQueueTenantPreReqs.remove(workQueueTenantPreReq);
        workQueueTenantPreReq.setWorkQueueItem(null);
        return this;
    }

    public void setWorkQueueTenantPreReqs(Set<WorkQueueTenantPreReq> workQueueTenantPreReqs) {
        if (this.workQueueTenantPreReqs != null) {
            this.workQueueTenantPreReqs.forEach(i -> i.setWorkQueueItem(null));
        }
        if (workQueueTenantPreReqs != null) {
            workQueueTenantPreReqs.forEach(i -> i.setWorkQueueItem(this));
        }
        this.workQueueTenantPreReqs = workQueueTenantPreReqs;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public WorkQueueTenant tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkQueueTenant)) {
            return false;
        }
        return id != null && id.equals(((WorkQueueTenant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkQueueTenant{" +
            "id=" + getId() +
            ", task='" + getTask() + "'" +
            ", requiredToComplete='" + getRequiredToComplete() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", dateCancelled='" + getDateCancelled() + "'" +
            ", dateCompleted='" + getDateCompleted() + "'" +
            ", sequenceOrder=" + getSequenceOrder() +
            "}";
    }
}
