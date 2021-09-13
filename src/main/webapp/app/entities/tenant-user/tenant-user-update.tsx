import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ILanUser } from 'app/shared/model/lan-user.model';
import { getEntities as getLanUsers } from 'app/entities/lan-user/lan-user.reducer';
import { ITenant } from 'app/shared/model/tenant.model';
import { getEntities as getTenants } from 'app/entities/tenant/tenant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './tenant-user.reducer';
import { ITenantUser } from 'app/shared/model/tenant-user.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TenantUserUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const lanUsers = useAppSelector(state => state.lanUser.entities);
  const tenants = useAppSelector(state => state.tenant.entities);
  const tenantUserEntity = useAppSelector(state => state.tenantUser.entity);
  const loading = useAppSelector(state => state.tenantUser.loading);
  const updating = useAppSelector(state => state.tenantUser.updating);
  const updateSuccess = useAppSelector(state => state.tenantUser.updateSuccess);

  const handleClose = () => {
    props.history.push('/tenant-user');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getLanUsers({}));
    dispatch(getTenants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...tenantUserEntity,
      ...values,
      lanUser: lanUsers.find(it => it.id.toString() === values.lanUserId.toString()),
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
          ...tenantUserEntity,
          lanUserId: tenantUserEntity?.lanUser?.id,
          tenantId: tenantUserEntity?.tenant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="provisioningApp.tenantUser.home.createOrEditLabel" data-cy="TenantUserCreateUpdateHeading">
            Create or edit a TenantUser
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="tenant-user-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Active" id="tenant-user-active" name="active" data-cy="active" check type="checkbox" />
              <ValidatedField
                label="Provisioned"
                id="tenant-user-provisioned"
                name="provisioned"
                data-cy="provisioned"
                check
                type="checkbox"
              />
              <ValidatedField id="tenant-user-lanUser" name="lanUserId" data-cy="lanUser" label="Lan User" type="select">
                <option value="" key="0" />
                {lanUsers
                  ? lanUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="tenant-user-tenant" name="tenantId" data-cy="tenant" label="Tenant" type="select">
                <option value="" key="0" />
                {tenants
                  ? tenants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tenant-user" replace color="info">
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

export default TenantUserUpdate;
