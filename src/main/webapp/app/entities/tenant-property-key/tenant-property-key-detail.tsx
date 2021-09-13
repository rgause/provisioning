import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './tenant-property-key.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TenantPropertyKeyDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tenantPropertyKeyEntity = useAppSelector(state => state.tenantPropertyKey.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tenantPropertyKeyDetailsHeading">TenantPropertyKey</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tenantPropertyKeyEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{tenantPropertyKeyEntity.name}</dd>
        </dl>
        <Button tag={Link} to="/tenant-property-key" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tenant-property-key/${tenantPropertyKeyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TenantPropertyKeyDetail;
