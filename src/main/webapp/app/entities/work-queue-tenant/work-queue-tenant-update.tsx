import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITenant } from 'app/shared/model/tenant.model';
import { getEntities as getTenants } from 'app/entities/tenant/tenant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './work-queue-tenant.reducer';
import { IWorkQueueTenant } from 'app/shared/model/work-queue-tenant.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkQueueTenantUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tenants = useAppSelector(state => state.tenant.entities);
  const workQueueTenantEntity = useAppSelector(state => state.workQueueTenant.entity);
  const loading = useAppSelector(state => state.workQueueTenant.loading);
  const updating = useAppSelector(state => state.workQueueTenant.updating);
  const updateSuccess = useAppSelector(state => state.workQueueTenant.updateSuccess);

  const handleClose = () => {
    props.history.push('/work-queue-tenant');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTenants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workQueueTenantEntity,
      ...values,
      tenant: tenants.find(it => it.id.toString() === values.tenantId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...workQueueTenantEntity,
          tenantId: workQueueTenantEntity?.tenant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="provisioningApp.workQueueTenant.home.createOrEditLabel" data-cy="WorkQueueTenantCreateUpdateHeading">
            Create or edit a WorkQueueTenant
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="work-queue-tenant-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Task"
                id="work-queue-tenant-task"
                name="task"
                data-cy="task"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Required To Complete"
                id="work-queue-tenant-requiredToComplete"
                name="requiredToComplete"
                data-cy="requiredToComplete"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Date Created"
                id="work-queue-tenant-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="date"
              />
              <ValidatedField
                label="Date Cancelled"
                id="work-queue-tenant-dateCancelled"
                name="dateCancelled"
                data-cy="dateCancelled"
                type="date"
              />
              <ValidatedField
                label="Date Completed"
                id="work-queue-tenant-dateCompleted"
                name="dateCompleted"
                data-cy="dateCompleted"
                type="date"
              />
              <ValidatedField
                label="Sequence Order"
                id="work-queue-tenant-sequenceOrder"
                name="sequenceOrder"
                data-cy="sequenceOrder"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField id="work-queue-tenant-tenant" name="tenantId" data-cy="tenant" label="Tenant" type="select">
                <option value="" key="0" />
                {tenants
                  ? tenants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/work-queue-tenant" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default WorkQueueTenantUpdate;
