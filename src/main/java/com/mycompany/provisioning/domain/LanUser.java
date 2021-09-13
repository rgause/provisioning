package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A LanUser.
 */
@Entity
@Table(name = "lan_user")
public class LanUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "lan_id", nullable = false, unique = true)
    private String lanId;

    @NotNull
    @Column(name = "pwp_id", nullable = false)
    private String pwpId;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @OneToMany(mappedBy = "lanUser")
    @JsonIgnoreProperties(value = { "workQueueTenantUsers", "lanUser", "tenant" }, allowSetters = true)
    private Set<TenantUser> tenantUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LanUser id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public LanUser active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getLanId() {
        return this.lanId;
    }

    public LanUser lanId(String lanId) {
        this.lanId = lanId;
        return this;
    }

    public void setLanId(String lanId) {
        this.lanId = lanId;
    }

    public String getPwpId() {
        return this.pwpId;
    }

    public LanUser pwpId(String pwpId) {
        this.pwpId = pwpId;
        return this;
    }

    public void setPwpId(String pwpId) {
        this.pwpId = pwpId;
    }

    public String getLastName() {
        return this.lastName;
    }

    public LanUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public LanUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Set<TenantUser> getTenantUsers() {
        return this.tenantUsers;
    }

    public LanUser tenantUsers(Set<TenantUser> tenantUsers) {
        this.setTenantUsers(tenantUsers);
        return this;
    }

    public LanUser addTenantUser(TenantUser tenantUser) {
        this.tenantUsers.add(tenantUser);
        tenantUser.setLanUser(this);
        return this;
    }

    public LanUser removeTenantUser(TenantUser tenantUser) {
        this.tenantUsers.remove(tenantUser);
        tenantUser.setLanUser(null);
        return this;
    }

    public void setTenantUsers(Set<TenantUser> tenantUsers) {
        if (this.tenantUsers != null) {
            this.tenantUsers.forEach(i -> i.setLanUser(null));
        }
        if (tenantUsers != null) {
            tenantUsers.forEach(i -> i.setLanUser(this));
        }
        this.tenantUsers = tenantUsers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LanUser)) {
            return false;
        }
        return id != null && id.equals(((LanUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LanUser{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", lanId='" + getLanId() + "'" +
            ", pwpId='" + getPwpId() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            "}";
    }
}
