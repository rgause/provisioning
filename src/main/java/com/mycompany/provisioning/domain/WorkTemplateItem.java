package com.mycompany.provisioning.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A WorkTemplateItem.
 */
@Entity
@Table(name = "work_template_item")
public class WorkTemplateItem implements Serializable {

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

    @NotNull
    @Column(name = "sequence_order", precision = 21, scale = 2, nullable = false)
    private BigDecimal sequenceOrder;

    @OneToMany(mappedBy = "workTemplateItem")
    @JsonIgnoreProperties(value = { "workTemplateItem" }, allowSetters = true)
    private Set<WorkTemplateItemPreReq> workTemplateItemPreReqs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "workTemplateItems" }, allowSetters = true)
    private WorkTemplate workTemplate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkTemplateItem id(Long id) {
        this.id = id;
        return this;
    }

    public String getTask() {
        return this.task;
    }

    public WorkTemplateItem task(String task) {
        this.task = task;
        return this;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Boolean getRequiredToComplete() {
        return this.requiredToComplete;
    }

    public WorkTemplateItem requiredToComplete(Boolean requiredToComplete) {
        this.requiredToComplete = requiredToComplete;
        return this;
    }

    public void setRequiredToComplete(Boolean requiredToComplete) {
        this.requiredToComplete = requiredToComplete;
    }

    public BigDecimal getSequenceOrder() {
        return this.sequenceOrder;
    }

    public WorkTemplateItem sequenceOrder(BigDecimal sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
        return this;
    }

    public void setSequenceOrder(BigDecimal sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public Set<WorkTemplateItemPreReq> getWorkTemplateItemPreReqs() {
        return this.workTemplateItemPreReqs;
    }

    public WorkTemplateItem workTemplateItemPreReqs(Set<WorkTemplateItemPreReq> workTemplateItemPreReqs) {
        this.setWorkTemplateItemPreReqs(workTemplateItemPreReqs);
        return this;
    }

    public WorkTemplateItem addWorkTemplateItemPreReq(WorkTemplateItemPreReq workTemplateItemPreReq) {
        this.workTemplateItemPreReqs.add(workTemplateItemPreReq);
        workTemplateItemPreReq.setWorkTemplateItem(this);
        return this;
    }

    public WorkTemplateItem removeWorkTemplateItemPreReq(WorkTemplateItemPreReq workTemplateItemPreReq) {
        this.workTemplateItemPreReqs.remove(workTemplateItemPreReq);
        workTemplateItemPreReq.setWorkTemplateItem(null);
        return this;
    }

    public void setWorkTemplateItemPreReqs(Set<WorkTemplateItemPreReq> workTemplateItemPreReqs) {
        if (this.workTemplateItemPreReqs != null) {
            this.workTemplateItemPreReqs.forEach(i -> i.setWorkTemplateItem(null));
        }
        if (workTemplateItemPreReqs != null) {
            workTemplateItemPreReqs.forEach(i -> i.setWorkTemplateItem(this));
        }
        this.workTemplateItemPreReqs = workTemplateItemPreReqs;
    }

    public WorkTemplate getWorkTemplate() {
        return this.workTemplate;
    }

    public WorkTemplateItem workTemplate(WorkTemplate workTemplate) {
        this.setWorkTemplate(workTemplate);
        return this;
    }

    public void setWorkTemplate(WorkTemplate workTemplate) {
        this.workTemplate = workTemplate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkTemplateItem)) {
            return false;
        }
        return id != null && id.equals(((WorkTemplateItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WorkTemplateItem{" +
            "id=" + getId() +
            ", task='" + getTask() + "'" +
            ", requiredToComplete='" + getRequiredToComplete() + "'" +
            ", sequenceOrder=" + getSequenceOrder() +
            "}";
    }
}
