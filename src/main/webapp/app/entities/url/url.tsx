import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './url.reducer';
import { IURL } from 'app/shared/model/url.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const URL = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const uRLList = useAppSelector(state => state.uRL.entities);
  const loading = useAppSelector(state => state.uRL.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="url-heading" data-cy="URLHeading">
        URLS
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new URL
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {uRLList && uRLList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Url</th>
                <th>Url Type</th>
                <th>Tenant</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {uRLList.map((uRL, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${uRL.id}`} color="link" size="sm">
                      {uRL.id}
                    </Button>
                  </td>
                  <td>{uRL.url}</td>
                  <td>{uRL.urlType ? <Link to={`url-type/${uRL.urlType.id}`}>{uRL.urlType.id}</Link> : ''}</td>
                  <td>{uRL.tenant ? <Link to={`tenant/${uRL.tenant.id}`}>{uRL.tenant.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${uRL.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${uRL.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${uRL.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No URLS found</div>
        )}
      </div>
    </div>
  );
};

export default URL;
