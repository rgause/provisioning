package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TenantProperty.
 */
@Entity
@Table(name = "tenant_property")
public class TenantProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tenantProperties", "tenantUsers", "workQueueTenants", "tenantType" }, allowSetters = true)
    private Tenant tenant;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tenantProperties" }, allowSetters = true)
    private TenantPropertyKey tenantPropertyKey;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TenantProperty id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return this.value;
    }

    public TenantProperty value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public TenantProperty tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public TenantPropertyKey getTenantPropertyKey() {
        return this.tenantPropertyKey;
    }

    public TenantProperty tenantPropertyKey(TenantPropertyKey tenantPropertyKey) {
        this.setTenantPropertyKey(tenantPropertyKey);
        return this;
    }

    public void setTenantPropertyKey(TenantPropertyKey tenantPropertyKey) {
        this.tenantPropertyKey = tenantPropertyKey;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantProperty)) {
            return false;
        }
        return id != null && id.equals(((TenantProperty) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantProperty{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}
