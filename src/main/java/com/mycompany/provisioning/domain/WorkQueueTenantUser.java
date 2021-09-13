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
 * A WorkQueueTenantUser.
 */
@Entity
@Table(name = "work_queue_tenant_user")
public class WorkQueueTenantUser implements Serializable {

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

    @OneToMany(mappedBy = "workQueueTenantUser")
    @JsonIgnoreProperties(value = { "workQueueTenantUser" }, allowSetters = true)
    private Set<WorkQueueTenantUserData> workQueueTenantUserData = new HashSet<>();

    @OneToMany(mappedBy = "workQueueTenantUser")
    @JsonIgnoreProperties(value = { "workQueueTenantUser" }, allowSetters = true)
    private Set<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "workQueueTenantUsers", "lanUser", "tenant" }, allowSetters = true)
    private TenantUser tenantUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkQueueTenantUser id(Long id) {
        this.id = id;
        return this;
    }

    public String getTask() {
        return this.task;
    }

    public WorkQueueTenantUser task(String task) {
        this.task = task;
        return this;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Boolean getRequiredToComplete() {
        return this.requiredToComplete;
    }

    public WorkQueueTenantUser requiredToComplete(Boolean requiredToComplete) {
        this.requiredToComplete = requiredToComplete;
        return this;
    }

    public void setRequiredToComplete(Boolean requiredToComplete) {
        this.requiredToComplete = requiredToComplete;
    }

    public LocalDate getDateCreated() {
        return this.dateCreated;
    }

    public WorkQueueTenantUser dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getDateCancelled() {
        return this.dateCancelled;
    }

    public WorkQueueTenantUser dateCancelled(LocalDate dateCancelled) {
        this.dateCancelled = dateCancelled;
        return this;
    }

    public void setDateCancelled(LocalDate dateCancelled) {
        this.dateCancelled = dateCancelled;
    }

    public LocalDate getDateCompleted() {
        return this.dateCompleted;
    }

    public WorkQueueTenantUser dateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
        return this;
    }

    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public BigDecimal getSequenceOrder() {
        return this.sequenceOrder;
    }

    public WorkQueueTenantUser sequenceOrder(BigDecimal sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
        return this;
    }

    public void setSequenceOrder(BigDecimal sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public Set<WorkQueueTenantUserData> getWorkQueueTenantUserData() {
        return this.workQueueTenantUserData;
    }

    public WorkQueueTenantUser workQueueTenantUserData(Set<WorkQueueTenantUserData> workQueueTenantUserData) {
        this.setWorkQueueTenantUserData(workQueueTenantUserData);
        return this;
    }

    public WorkQueueTenantUser addWorkQueueTenantUserData(WorkQueueTenantUserData workQueueTenantUserData) {
        this.workQueueTenantUserData.add(workQueueTenantUserData);
        workQueueTenantUserData.setWorkQueueTenantUser(this);
        return this;
    }

    public WorkQueueTenantUser removeWorkQueueTenantUserData(WorkQueueTenantUserData workQueueTenantUserData) {
        this.workQueueTenantUserData.remove(workQueueTenantUserData);
        workQueueTenantUserData.setWorkQueueTenantUser(null);
        return this;
    }

    public void setWorkQueueTenantUserData(Set<WorkQueueTenantUserData> workQueueTenantUserData) {
        if (this.workQueueTenantUserData != null) {
            this.workQueueTenantUserData.forEach(i -> i.setWorkQueueTenantUser(null));
        }
        if (workQueueTenantUserData != null) {
            workQueueTenantUserData.forEach(i -> i.setWorkQueueTenantUser(this));
        }
        this.workQueueTenantUserData = workQueueTenantUserData;
    }

    public Set<WorkQueueTenantUserPreReq> getWorkQueueTenantUserPreReqs() {
        return this.workQueueTenantUserPreReqs;
    }

    public WorkQueueTenantUser workQueueTenantUserPreReqs(Set<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqs) {
        this.setWorkQueueTenantUserPreReqs(workQueueTenantUserPreReqs);
        return this;
    }

    public WorkQueueTenantUser addWorkQueueTenantUserPreReq(WorkQueueTenantUserPreReq workQueueTenantUserPreReq) {
        this.workQueueTenantUserPreReqs.add(workQueueTenantUserPreReq);
        workQueueTenantUserPreReq.setWorkQueueTenantUser(this);
        return this;
    }

    public WorkQueueTenantUser removeWorkQueueTenantUserPreReq(WorkQueueTenantUserPreReq workQueueTenantUserPreReq) {
        this.workQueueTenantUserPreReqs.remove(workQueueTenantUserPreReq);
        workQueueTenantUserPreReq.setWorkQueueTenantUser(null);
        return this;
    }

    public void setWorkQueueTenantUserPreReqs(Set<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqs) {
        if (this.workQueueTenantUserPreReqs != null) {
            this.workQueueTenantUserPreReqs.forEach(i -> i.setWorkQueueTenantUser(null));
        }
        if (workQueueTenantUserPreReqs != null) {
            workQueueTenantUserPreReqs.forEach(i -> i.setWorkQueueTenantUser(this));
        }
        this.workQueueTenantUserPreReqs = workQueueTenantUserPreReqs;
    }

    public TenantUser getTenantUser() {
        return this.tenantUser;
    }

    public WorkQueueTenantUser tenantUser(TenantUser tenantUser) {
        this.setTenantUser(tenantUser);
        return this;
    }

    public void setTenantUser(TenantUser tenantUser) {
        this.tenantUser = tenantUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkQueueTenantUser)) {
            return false;
        }
        return id != null && id.equals(((WorkQueueTenantUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkQueueTenantUser{" +
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
