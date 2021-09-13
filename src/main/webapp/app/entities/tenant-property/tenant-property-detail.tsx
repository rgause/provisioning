import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './tenant-property.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TenantPropertyDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tenantPropertyEntity = useAppSelector(state => state.tenantProperty.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tenantPropertyDetailsHeading">TenantProperty</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tenantPropertyEntity.id}</dd>
          <dt>
            <span id="value">Value</span>
          </dt>
          <dd>{tenantPropertyEntity.value}</dd>
          <dt>Tenant</dt>
          <dd>{tenantPropertyEntity.tenant ? tenantPropertyEntity.tenant.id : ''}</dd>
          <dt>Tenant Property Key</dt>
          <dd>{tenantPropertyEntity.tenantPropertyKey ? tenantPropertyEntity.tenantPropertyKey.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tenant-property" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tenant-property/${tenantPropertyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TenantPropertyDetail;
