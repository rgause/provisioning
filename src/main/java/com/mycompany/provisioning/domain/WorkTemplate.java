package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WorkTemplate.
 */
@Entity
@Table(name = "work_template")
public class WorkTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "workTemplate")
    @JsonIgnoreProperties(value = { "workTemplateItemPreReqs", "workTemplate" }, allowSetters = true)
    private Set<WorkTemplateItem> workTemplateItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkTemplate id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public WorkTemplate name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<WorkTemplateItem> getWorkTemplateItems() {
        return this.workTemplateItems;
    }

    public WorkTemplate workTemplateItems(Set<WorkTemplateItem> workTemplateItems) {
        this.setWorkTemplateItems(workTemplateItems);
        return this;
    }

    public WorkTemplate addWorkTemplateItem(WorkTemplateItem workTemplateItem) {
        this.workTemplateItems.add(workTemplateItem);
        workTemplateItem.setWorkTemplate(this);
        return this;
    }

    public WorkTemplate removeWorkTemplateItem(WorkTemplateItem workTemplateItem) {
        this.workTemplateItems.remove(workTemplateItem);
        workTemplateItem.setWorkTemplate(null);
        return this;
    }

    public void setWorkTemplateItems(Set<WorkTemplateItem> workTemplateItems) {
        if (this.workTemplateItems != null) {
            this.workTemplateItems.forEach(i -> i.setWorkTemplate(null));
        }
        if (workTemplateItems != null) {
            workTemplateItems.forEach(i -> i.setWorkTemplate(this));
        }
        this.workTemplateItems = workTemplateItems;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkTemplate)) {
            return false;
        }
        return id != null && id.equals(((WorkTemplate) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkTemplate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
