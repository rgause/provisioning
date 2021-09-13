import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './work-queue-tenant-data.reducer';
import { IWorkQueueTenantData } from 'app/shared/model/work-queue-tenant-data.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkQueueTenantData = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const workQueueTenantDataList = useAppSelector(state => state.workQueueTenantData.entities);
  const loading = useAppSelector(state => state.workQueueTenantData.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="work-queue-tenant-data-heading" data-cy="WorkQueueTenantDataHeading">
        Work Queue Tenant Data
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Work Queue Tenant Data
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {workQueueTenantDataList && workQueueTenantDataList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Data</th>
                <th>Type</th>
                <th>Work Queue Tenant</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workQueueTenantDataList.map((workQueueTenantData, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${workQueueTenantData.id}`} color="link" size="sm">
                      {workQueueTenantData.id}
                    </Button>
                  </td>
                  <td>{workQueueTenantData.data}</td>
                  <td>{workQueueTenantData.type}</td>
                  <td>
                    {workQueueTenantData.workQueueTenant ? (
                      <Link to={`work-queue-tenant/${workQueueTenantData.workQueueTenant.id}`}>
                        {workQueueTenantData.workQueueTenant.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${workQueueTenantData.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workQueueTenantData.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workQueueTenantData.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Work Queue Tenant Data found</div>
        )}
      </div>
    </div>
  );
};

export default WorkQueueTenantData;
