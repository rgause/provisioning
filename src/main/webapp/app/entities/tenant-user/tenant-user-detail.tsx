import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './tenant-user.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TenantUserDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tenantUserEntity = useAppSelector(state => state.tenantUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tenantUserDetailsHeading">TenantUser</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tenantUserEntity.id}</dd>
          <dt>
            <span id="active">Active</span>
          </dt>
          <dd>{tenantUserEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="provisioned">Provisioned</span>
          </dt>
          <dd>{tenantUserEntity.provisioned ? 'true' : 'false'}</dd>
          <dt>Lan User</dt>
          <dd>{tenantUserEntity.lanUser ? tenantUserEntity.lanUser.id : ''}</dd>
          <dt>Tenant</dt>
          <dd>{tenantUserEntity.tenant ? tenantUserEntity.tenant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tenant-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tenant-user/${tenantUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TenantUserDetail;
