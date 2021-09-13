import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './lan-user.reducer';
import { ILanUser } from 'app/shared/model/lan-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LanUser = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const lanUserList = useAppSelector(state => state.lanUser.entities);
  const loading = useAppSelector(state => state.lanUser.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="lan-user-heading" data-cy="LanUserHeading">
        Lan Users
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Lan User
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {lanUserList && lanUserList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Active</th>
                <th>Lan Id</th>
                <th>Pwp Id</th>
                <th>Last Name</th>
                <th>First Name</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {lanUserList.map((lanUser, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${lanUser.id}`} color="link" size="sm">
                      {lanUser.id}
                    </Button>
                  </td>
                  <td>{lanUser.active ? 'true' : 'false'}</td>
                  <td>{lanUser.lanId}</td>
                  <td>{lanUser.pwpId}</td>
                  <td>{lanUser.lastName}</td>
                  <td>{lanUser.firstName}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${lanUser.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${lanUser.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${lanUser.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Lan Users found</div>
        )}
      </div>
    </div>
  );
};

export default LanUser;
