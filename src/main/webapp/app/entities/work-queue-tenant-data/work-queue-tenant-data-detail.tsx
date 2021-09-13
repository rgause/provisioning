import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './work-queue-tenant-data.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkQueueTenantDataDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workQueueTenantDataEntity = useAppSelector(state => state.workQueueTenantData.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workQueueTenantDataDetailsHeading">WorkQueueTenantData</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{workQueueTenantDataEntity.id}</dd>
          <dt>
            <span id="data">Data</span>
          </dt>
          <dd>{workQueueTenantDataEntity.data}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{workQueueTenantDataEntity.type}</dd>
          <dt>Work Queue Tenant</dt>
          <dd>{workQueueTenantDataEntity.workQueueTenant ? workQueueTenantDataEntity.workQueueTenant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/work-queue-tenant-data" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/work-queue-tenant-data/${workQueueTenantDataEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkQueueTenantDataDetail;
