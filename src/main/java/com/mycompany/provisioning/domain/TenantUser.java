package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TenantUser.
 */
@Entity
@Table(name = "tenant_user")
public class TenantUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "provisioned", nullable = false)
    private Boolean provisioned;

    @OneToMany(mappedBy = "tenantUser")
    @JsonIgnoreProperties(value = { "workQueueTenantUserData", "workQueueTenantUserPreReqs", "tenantUser" }, allowSetters = true)
    private Set<WorkQueueTenantUser> workQueueTenantUsers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "tenantUsers" }, allowSetters = true)
    private LanUser lanUser;

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

    public TenantUser id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public TenantUser active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getProvisioned() {
        return this.provisioned;
    }

    public TenantUser provisioned(Boolean provisioned) {
        this.provisioned = provisioned;
        return this;
    }

    public void setProvisioned(Boolean provisioned) {
        this.provisioned = provisioned;
    }

    public Set<WorkQueueTenantUser> getWorkQueueTenantUsers() {
        return this.workQueueTenantUsers;
    }

    public TenantUser workQueueTenantUsers(Set<WorkQueueTenantUser> workQueueTenantUsers) {
        this.setWorkQueueTenantUsers(workQueueTenantUsers);
        return this;
    }

    public TenantUser addWorkQueueTenantUser(WorkQueueTenantUser workQueueTenantUser) {
        this.workQueueTenantUsers.add(workQueueTenantUser);
        workQueueTenantUser.setTenantUser(this);
        return this;
    }

    public TenantUser removeWorkQueueTenantUser(WorkQueueTenantUser workQueueTenantUser) {
        this.workQueueTenantUsers.remove(workQueueTenantUser);
        workQueueTenantUser.setTenantUser(null);
        return this;
    }

    public void setWorkQueueTenantUsers(Set<WorkQueueTenantUser> workQueueTenantUsers) {
        if (this.workQueueTenantUsers != null) {
            this.workQueueTenantUsers.forEach(i -> i.setTenantUser(null));
        }
        if (workQueueTenantUsers != null) {
            workQueueTenantUsers.forEach(i -> i.setTenantUser(this));
        }
        this.workQueueTenantUsers = workQueueTenantUsers;
    }

    public LanUser getLanUser() {
        return this.lanUser;
    }

    public TenantUser lanUser(LanUser lanUser) {
        this.setLanUser(lanUser);
        return this;
    }

    public void setLanUser(LanUser lanUser) {
        this.lanUser = lanUser;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public TenantUser tenant(Tenant tenant) {
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
        if (!(o instanceof TenantUser)) {
            return false;
        }
        return id != null && id.equals(((TenantUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantUser{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", provisioned='" + getProvisioned() + "'" +
            "}";
    }
}
