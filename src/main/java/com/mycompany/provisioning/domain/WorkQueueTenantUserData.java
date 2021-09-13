package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WorkQueueTenantUserData.
 */
@Entity
@Table(name = "work_queue_tenant_user_data")
public class WorkQueueTenantUserData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data", nullable = false)
    private String data;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

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

    public WorkQueueTenantUserData id(Long id) {
        this.id = id;
        return this;
    }

    public String getData() {
        return this.data;
    }

    public WorkQueueTenantUserData data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return this.type;
    }

    public WorkQueueTenantUserData type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WorkQueueTenantUser getWorkQueueTenantUser() {
        return this.workQueueTenantUser;
    }

    public WorkQueueTenantUserData workQueueTenantUser(WorkQueueTenantUser workQueueTenantUser) {
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
        if (!(o instanceof WorkQueueTenantUserData)) {
            return false;
        }
        return id != null && id.equals(((WorkQueueTenantUserData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkQueueTenantUserData{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
