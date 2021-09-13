package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TenantPropertyKey.
 */
@Entity
@Table(name = "tenant_property_key")
public class TenantPropertyKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "tenantPropertyKey")
    @JsonIgnoreProperties(value = { "tenant", "tenantPropertyKey" }, allowSetters = true)
    private Set<TenantProperty> tenantProperties = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TenantPropertyKey id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public TenantPropertyKey name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TenantProperty> getTenantProperties() {
        return this.tenantProperties;
    }

    public TenantPropertyKey tenantProperties(Set<TenantProperty> tenantProperties) {
        this.setTenantProperties(tenantProperties);
        return this;
    }

    public TenantPropertyKey addTenantProperty(TenantProperty tenantProperty) {
        this.tenantProperties.add(tenantProperty);
        tenantProperty.setTenantPropertyKey(this);
        return this;
    }

    public TenantPropertyKey removeTenantProperty(TenantProperty tenantProperty) {
        this.tenantProperties.remove(tenantProperty);
        tenantProperty.setTenantPropertyKey(null);
        return this;
    }

    public void setTenantProperties(Set<TenantProperty> tenantProperties) {
        if (this.tenantProperties != null) {
            this.tenantProperties.forEach(i -> i.setTenantPropertyKey(null));
        }
        if (tenantProperties != null) {
            tenantProperties.forEach(i -> i.setTenantPropertyKey(this));
        }
        this.tenantProperties = tenantProperties;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantPropertyKey)) {
            return false;
        }
        return id != null && id.equals(((TenantPropertyKey) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TenantPropertyKey{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
