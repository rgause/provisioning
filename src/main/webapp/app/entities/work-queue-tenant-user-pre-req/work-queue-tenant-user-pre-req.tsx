import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './work-queue-tenant-user-pre-req.reducer';
import { IWorkQueueTenantUserPreReq } from 'app/shared/model/work-queue-tenant-user-pre-req.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkQueueTenantUserPreReq = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const workQueueTenantUserPreReqList = useAppSelector(state => state.workQueueTenantUserPreReq.entities);
  const loading = useAppSelector(state => state.workQueueTenantUserPreReq.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="work-queue-tenant-user-pre-req-heading" data-cy="WorkQueueTenantUserPreReqHeading">
        Work Queue Tenant User Pre Reqs
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Work Queue Tenant User Pre Req
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {workQueueTenantUserPreReqList && workQueueTenantUserPreReqList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Pre Requisite Item</th>
                <th>Work Queue Tenant User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workQueueTenantUserPreReqList.map((workQueueTenantUserPreReq, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${workQueueTenantUserPreReq.id}`} color="link" size="sm">
                      {workQueueTenantUserPreReq.id}
                    </Button>
                  </td>
                  <td>{workQueueTenantUserPreReq.preRequisiteItem}</td>
                  <td>
                    {workQueueTenantUserPreReq.workQueueTenantUser ? (
                      <Link to={`work-queue-tenant-user/${workQueueTenantUserPreReq.workQueueTenantUser.id}`}>
                        {workQueueTenantUserPreReq.workQueueTenantUser.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`${match.url}/${workQueueTenantUserPreReq.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workQueueTenantUserPreReq.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workQueueTenantUserPreReq.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Work Queue Tenant User Pre Reqs found</div>
        )}
      </div>
    </div>
  );
};

export default WorkQueueTenantUserPreReq;
