package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A URL.
 */
@Entity
@Table(name = "url")
public class URL implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    private URLType urlType;

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

    public URL id(Long id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return this.url;
    }

    public URL url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public URLType getUrlType() {
        return this.urlType;
    }

    public URL urlType(URLType uRLType) {
        this.setUrlType(uRLType);
        return this;
    }

    public void setUrlType(URLType uRLType) {
        this.urlType = uRLType;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public URL tenant(Tenant tenant) {
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
        if (!(o instanceof URL)) {
            return false;
        }
        return id != null && id.equals(((URL) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "URL{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
