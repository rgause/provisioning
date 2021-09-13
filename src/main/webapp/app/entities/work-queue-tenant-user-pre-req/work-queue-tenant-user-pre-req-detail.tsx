import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './work-queue-tenant-user-pre-req.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkQueueTenantUserPreReqDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workQueueTenantUserPreReqEntity = useAppSelector(state => state.workQueueTenantUserPreReq.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workQueueTenantUserPreReqDetailsHeading">WorkQueueTenantUserPreReq</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{workQueueTenantUserPreReqEntity.id}</dd>
          <dt>
            <span id="preRequisiteItem">Pre Requisite Item</span>
          </dt>
          <dd>{workQueueTenantUserPreReqEntity.preRequisiteItem}</dd>
          <dt>Work Queue Tenant User</dt>
          <dd>{workQueueTenantUserPreReqEntity.workQueueTenantUser ? workQueueTenantUserPreReqEntity.workQueueTenantUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/work-queue-tenant-user-pre-req" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/work-queue-tenant-user-pre-req/${workQueueTenantUserPreReqEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkQueueTenantUserPreReqDetail;
