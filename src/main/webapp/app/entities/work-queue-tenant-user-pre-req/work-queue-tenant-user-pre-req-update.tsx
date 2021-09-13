import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorkQueueTenantUser } from 'app/shared/model/work-queue-tenant-user.model';
import { getEntities as getWorkQueueTenantUsers } from 'app/entities/work-queue-tenant-user/work-queue-tenant-user.reducer';
import { getEntity, updateEntity, createEntity, reset } from './work-queue-tenant-user-pre-req.reducer';
import { IWorkQueueTenantUserPreReq } from 'app/shared/model/work-queue-tenant-user-pre-req.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkQueueTenantUserPreReqUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workQueueTenantUsers = useAppSelector(state => state.workQueueTenantUser.entities);
  const workQueueTenantUserPreReqEntity = useAppSelector(state => state.workQueueTenantUserPreReq.entity);
  const loading = useAppSelector(state => state.workQueueTenantUserPreReq.loading);
  const updating = useAppSelector(state => state.workQueueTenantUserPreReq.updating);
  const updateSuccess = useAppSelector(state => state.workQueueTenantUserPreReq.updateSuccess);

  const handleClose = () => {
    props.history.push('/work-queue-tenant-user-pre-req');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkQueueTenantUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workQueueTenantUserPreReqEntity,
      ...values,
      workQueueTenantUser: workQueueTenantUsers.find(it => it.id.toString() === values.workQueueTenantUserId.toString()),
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
          ...workQueueTenantUserPreReqEntity,
          workQueueTenantUserId: workQueueTenantUserPreReqEntity?.workQueueTenantUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="provisioningApp.workQueueTenantUserPreReq.home.createOrEditLabel" data-cy="WorkQueueTenantUserPreReqCreateUpdateHeading">
            Create or edit a WorkQueueTenantUserPreReq
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
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="work-queue-tenant-user-pre-req-id"
                  label="ID"
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label="Pre Requisite Item"
                id="work-queue-tenant-user-pre-req-preRequisiteItem"
                name="preRequisiteItem"
                data-cy="preRequisiteItem"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                id="work-queue-tenant-user-pre-req-workQueueTenantUser"
                name="workQueueTenantUserId"
                data-cy="workQueueTenantUser"
                label="Work Queue Tenant User"
                type="select"
              >
                <option value="" key="0" />
                {workQueueTenantUsers
                  ? workQueueTenantUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/work-queue-tenant-user-pre-req"
                replace
                color="info"
              >
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

export default WorkQueueTenantUserPreReqUpdate;
