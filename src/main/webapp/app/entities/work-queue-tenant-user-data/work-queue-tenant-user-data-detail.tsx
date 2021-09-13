import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './work-queue-tenant-user-data.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkQueueTenantUserDataDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workQueueTenantUserDataEntity = useAppSelector(state => state.workQueueTenantUserData.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workQueueTenantUserDataDetailsHeading">WorkQueueTenantUserData</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{workQueueTenantUserDataEntity.id}</dd>
          <dt>
            <span id="data">Data</span>
          </dt>
          <dd>{workQueueTenantUserDataEntity.data}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{workQueueTenantUserDataEntity.type}</dd>
          <dt>Work Queue Tenant User</dt>
          <dd>{workQueueTenantUserDataEntity.workQueueTenantUser ? workQueueTenantUserDataEntity.workQueueTenantUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/work-queue-tenant-user-data" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/work-queue-tenant-user-data/${workQueueTenantUserDataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkQueueTenantUserDataDetail;
