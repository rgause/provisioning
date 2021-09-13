import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './work-queue-tenant-pre-req.reducer';
import { IWorkQueueTenantPreReq } from 'app/shared/model/work-queue-tenant-pre-req.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkQueueTenantPreReq = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const workQueueTenantPreReqList = useAppSelector(state => state.workQueueTenantPreReq.entities);
  const loading = useAppSelector(state => state.workQueueTenantPreReq.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="work-queue-tenant-pre-req-heading" data-cy="WorkQueueTenantPreReqHeading">
        Work Queue Tenant Pre Reqs
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Work Queue Tenant Pre Req
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {workQueueTenantPreReqList && workQueueTenantPreReqList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Pre Requisite Item</th>
                <th>Work Queue Item</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workQueueTenantPreReqList.map((workQueueTenantPreReq, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${workQueueTenantPreReq.id}`} color="link" size="sm">
                      {workQueueTenantPreReq.id}
                    </Button>
                  </td>
                  <td>{workQueueTenantPreReq.preRequisiteItem}</td>
                  <td>
                    {workQueueTenantPreReq.workQueueItem ? (
                      <Link to={`work-queue-tenant/${workQueueTenantPreReq.workQueueItem.id}`}>
                        {workQueueTenantPreReq.workQueueItem.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`${match.url}/${workQueueTenantPreReq.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workQueueTenantPreReq.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workQueueTenantPreReq.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Work Queue Tenant Pre Reqs found</div>
        )}
      </div>
    </div>
  );
};

export default WorkQueueTenantPreReq;
