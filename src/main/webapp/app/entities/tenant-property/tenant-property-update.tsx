import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ITenant } from 'app/shared/model/tenant.model';
import { getEntities as getTenants } from 'app/entities/tenant/tenant.reducer';
import { ITenantPropertyKey } from 'app/shared/model/tenant-property-key.model';
import { getEntities as getTenantPropertyKeys } from 'app/entities/tenant-property-key/tenant-property-key.reducer';
import { getEntity, updateEntity, createEntity, reset } from './tenant-property.reducer';
import { ITenantProperty } from 'app/shared/model/tenant-property.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TenantPropertyUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tenants = useAppSelector(state => state.tenant.entities);
  const tenantPropertyKeys = useAppSelector(state => state.tenantPropertyKey.entities);
  const tenantPropertyEntity = useAppSelector(state => state.tenantProperty.entity);
  const loading = useAppSelector(state => state.tenantProperty.loading);
  const updating = useAppSelector(state => state.tenantProperty.updating);
  const updateSuccess = useAppSelector(state => state.tenantProperty.updateSuccess);

  const handleClose = () => {
    props.history.push('/tenant-property');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTenants({}));
    dispatch(getTenantPropertyKeys({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...tenantPropertyEntity,
      ...values,
      tenant: tenants.find(it => it.id.toString() === values.tenantId.toString()),
      tenantPropertyKey: tenantPropertyKeys.find(it => it.id.toString() === values.tenantPropertyKeyId.toString()),
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
          ...tenantPropertyEntity,
          tenantId: tenantPropertyEntity?.tenant?.id,
          tenantPropertyKeyId: tenantPropertyEntity?.tenantPropertyKey?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="provisioningApp.tenantProperty.home.createOrEditLabel" data-cy="TenantPropertyCreateUpdateHeading">
            Create or edit a TenantProperty
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
                <ValidatedField name="id" required readOnly id="tenant-property-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Value"
                id="tenant-property-value"
                name="value"
                data-cy="value"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="tenant-property-tenant" name="tenantId" data-cy="tenant" label="Tenant" type="select">
                <option value="" key="0" />
                {tenants
                  ? tenants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="tenant-property-tenantPropertyKey"
                name="tenantPropertyKeyId"
                data-cy="tenantPropertyKey"
                label="Tenant Property Key"
                type="select"
              >
                <option value="" key="0" />
                {tenantPropertyKeys
                  ? tenantPropertyKeys.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tenant-property" replace color="info">
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

export default TenantPropertyUpdate;
