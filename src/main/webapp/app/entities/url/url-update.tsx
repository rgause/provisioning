import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IURLType } from 'app/shared/model/url-type.model';
import { getEntities as getURlTypes } from 'app/entities/url-type/url-type.reducer';
import { ITenant } from 'app/shared/model/tenant.model';
import { getEntities as getTenants } from 'app/entities/tenant/tenant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './url.reducer';
import { IURL } from 'app/shared/model/url.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const URLUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const uRLTypes = useAppSelector(state => state.uRLType.entities);
  const tenants = useAppSelector(state => state.tenant.entities);
  const uRLEntity = useAppSelector(state => state.uRL.entity);
  const loading = useAppSelector(state => state.uRL.loading);
  const updating = useAppSelector(state => state.uRL.updating);
  const updateSuccess = useAppSelector(state => state.uRL.updateSuccess);

  const handleClose = () => {
    props.history.push('/url');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getURlTypes({}));
    dispatch(getTenants({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...uRLEntity,
      ...values,
      urlType: uRLTypes.find(it => it.id.toString() === values.urlTypeId.toString()),
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
          ...uRLEntity,
          urlTypeId: uRLEntity?.urlType?.id,
          tenantId: uRLEntity?.tenant?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="provisioningApp.uRL.home.createOrEditLabel" data-cy="URLCreateUpdateHeading">
            Create or edit a URL
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="url-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Url"
                id="url-url"
                name="url"
                data-cy="url"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField id="url-urlType" name="urlTypeId" data-cy="urlType" label="Url Type" type="select">
                <option value="" key="0" />
                {uRLTypes
                  ? uRLTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="url-tenant" name="tenantId" data-cy="tenant" label="Tenant" type="select">
                <option value="" key="0" />
                {tenants
                  ? tenants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/url" replace color="info">
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

export default URLUpdate;
