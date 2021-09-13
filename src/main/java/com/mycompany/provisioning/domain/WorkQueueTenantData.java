package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WorkQueueTenantData.
 */
@Entity
@Table(name = "work_queue_tenant_data")
public class WorkQueueTenantData implements Serializable {

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
    @JsonIgnoreProperties(value = { "workQueueTenantData", "workQueueTenantPreReqs", "tenant" }, allowSetters = true)
    private WorkQueueTenant workQueueTenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkQueueTenantData id(Long id) {
        this.id = id;
        return this;
    }

    public String getData() {
        return this.data;
    }

    public WorkQueueTenantData data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return this.type;
    }

    public WorkQueueTenantData type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public WorkQueueTenant getWorkQueueTenant() {
        return this.workQueueTenant;
    }

    public WorkQueueTenantData workQueueTenant(WorkQueueTenant workQueueTenant) {
        this.setWorkQueueTenant(workQueueTenant);
        return this;
    }

    public void setWorkQueueTenant(WorkQueueTenant workQueueTenant) {
        this.workQueueTenant = workQueueTenant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkQueueTenantData)) {
            return false;
        }
        return id != null && id.equals(((WorkQueueTenantData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkQueueTenantData{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
