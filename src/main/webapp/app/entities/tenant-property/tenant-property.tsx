import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './tenant-property.reducer';
import { ITenantProperty } from 'app/shared/model/tenant-property.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TenantProperty = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const tenantPropertyList = useAppSelector(state => state.tenantProperty.entities);
  const loading = useAppSelector(state => state.tenantProperty.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="tenant-property-heading" data-cy="TenantPropertyHeading">
        Tenant Properties
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Tenant Property
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {tenantPropertyList && tenantPropertyList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Value</th>
                <th>Tenant</th>
                <th>Tenant Property Key</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {tenantPropertyList.map((tenantProperty, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${tenantProperty.id}`} color="link" size="sm">
                      {tenantProperty.id}
                    </Button>
                  </td>
                  <td>{tenantProperty.value}</td>
                  <td>{tenantProperty.tenant ? <Link to={`tenant/${tenantProperty.tenant.id}`}>{tenantProperty.tenant.id}</Link> : ''}</td>
                  <td>
                    {tenantProperty.tenantPropertyKey ? (
                      <Link to={`tenant-property-key/${tenantProperty.tenantPropertyKey.id}`}>{tenantProperty.tenantPropertyKey.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${tenantProperty.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${tenantProperty.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${tenantProperty.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Tenant Properties found</div>
        )}
      </div>
    </div>
  );
};

export default TenantProperty;
