package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WorkTemplateItemPreReq.
 */
@Entity
@Table(name = "work_template_item_pre_req")
public class WorkTemplateItemPreReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "pre_requisite_item", precision = 21, scale = 2, nullable = false)
    private BigDecimal preRequisiteItem;

    @ManyToOne
    @JsonIgnoreProperties(value = { "workTemplateItemPreReqs", "workTemplate" }, allowSetters = true)
    private WorkTemplateItem workTemplateItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkTemplateItemPreReq id(Long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getPreRequisiteItem() {
        return this.preRequisiteItem;
    }

    public WorkTemplateItemPreReq preRequisiteItem(BigDecimal preRequisiteItem) {
        this.preRequisiteItem = preRequisiteItem;
        return this;
    }

    public void setPreRequisiteItem(BigDecimal preRequisiteItem) {
        this.preRequisiteItem = preRequisiteItem;
    }

    public WorkTemplateItem getWorkTemplateItem() {
        return this.workTemplateItem;
    }

    public WorkTemplateItemPreReq workTemplateItem(WorkTemplateItem workTemplateItem) {
        this.setWorkTemplateItem(workTemplateItem);
        return this;
    }

    public void setWorkTemplateItem(WorkTemplateItem workTemplateItem) {
        this.workTemplateItem = workTemplateItem;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkTemplateItemPreReq)) {
            return false;
        }
        return id != null && id.equals(((WorkTemplateItemPreReq) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkTemplateItemPreReq{" +
            "id=" + getId() +
            ", preRequisiteItem=" + getPreRequisiteItem() +
            "}";
    }
}
