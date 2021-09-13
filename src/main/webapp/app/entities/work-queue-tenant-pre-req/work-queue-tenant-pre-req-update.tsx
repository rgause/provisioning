import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorkQueueTenant } from 'app/shared/model/work-queue-tenant.model';
import { getEntities as getWorkQueueTenants } from 'app/entities/work-queue-tenant/work-queue-tenant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './work-queue-tenant-pre-req.reducer';
import { IWorkQueueTenantPreReq } from 'app/shared/model/work-queue-tenant-pre-req.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkQueueTenantPreReqUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workQueueTenants = useAppSelector(state => state.workQueueTenant.entities);
  const workQueueTenantPreReqEntity = useAppSelector(state => state.workQueueTenantPreReq.entity);
  const loading = useAppSelector(state => state.workQueueTenantPreReq.loading);
  const updating = useAppSelector(state => state.workQueueTenantPreReq.updating);
  const updateSuccess = useAppSelector(state => state.workQueueTenantPreReq.updateSuccess);

  const handleClose = () => {
    props.history.push('/work-queue-tenant-pre-req');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkQueueTenants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workQueueTenantPreReqEntity,
      ...values,
      workQueueItem: workQueueTenants.find(it => it.id.toString() === values.workQueueItemId.toString()),
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
          ...workQueueTenantPreReqEntity,
          workQueueItemId: workQueueTenantPreReqEntity?.workQueueItem?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="provisioningApp.workQueueTenantPreReq.home.createOrEditLabel" data-cy="WorkQueueTenantPreReqCreateUpdateHeading">
            Create or edit a WorkQueueTenantPreReq
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
                <ValidatedField name="id" required readOnly id="work-queue-tenant-pre-req-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Pre Requisite Item"
                id="work-queue-tenant-pre-req-preRequisiteItem"
                name="preRequisiteItem"
                data-cy="preRequisiteItem"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                id="work-queue-tenant-pre-req-workQueueItem"
                name="workQueueItemId"
                data-cy="workQueueItem"
                label="Work Queue Item"
                type="select"
              >
                <option value="" key="0" />
                {workQueueTenants
                  ? workQueueTenants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/work-queue-tenant-pre-req" replace color="info">
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

export default WorkQueueTenantPreReqUpdate;
