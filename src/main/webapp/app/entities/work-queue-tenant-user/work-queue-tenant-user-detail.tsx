import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './work-queue-tenant-user.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkQueueTenantUserDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workQueueTenantUserEntity = useAppSelector(state => state.workQueueTenantUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workQueueTenantUserDetailsHeading">WorkQueueTenantUser</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{workQueueTenantUserEntity.id}</dd>
          <dt>
            <span id="task">Task</span>
          </dt>
          <dd>{workQueueTenantUserEntity.task}</dd>
          <dt>
            <span id="requiredToComplete">Required To Complete</span>
          </dt>
          <dd>{workQueueTenantUserEntity.requiredToComplete ? 'true' : 'false'}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {workQueueTenantUserEntity.dateCreated ? (
              <TextFormat value={workQueueTenantUserEntity.dateCreated} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dateCancelled">Date Cancelled</span>
          </dt>
          <dd>
            {workQueueTenantUserEntity.dateCancelled ? (
              <TextFormat value={workQueueTenantUserEntity.dateCancelled} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dateCompleted">Date Completed</span>
          </dt>
          <dd>
            {workQueueTenantUserEntity.dateCompleted ? (
              <TextFormat value={workQueueTenantUserEntity.dateCompleted} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="sequenceOrder">Sequence Order</span>
          </dt>
          <dd>{workQueueTenantUserEntity.sequenceOrder}</dd>
          <dt>Tenant User</dt>
          <dd>{workQueueTenantUserEntity.tenantUser ? workQueueTenantUserEntity.tenantUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/work-queue-tenant-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/work-queue-tenant-user/${workQueueTenantUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkQueueTenantUserDetail;
