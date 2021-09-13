import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './work-template-item.reducer';
import { IWorkTemplateItem } from 'app/shared/model/work-template-item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkTemplateItem = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const workTemplateItemList = useAppSelector(state => state.workTemplateItem.entities);
  const loading = useAppSelector(state => state.workTemplateItem.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="work-template-item-heading" data-cy="WorkTemplateItemHeading">
        Work Template Items
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Work Template Item
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {workTemplateItemList && workTemplateItemList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Task</th>
                <th>Required To Complete</th>
                <th>Sequence Order</th>
                <th>Work Template</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workTemplateItemList.map((workTemplateItem, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${workTemplateItem.id}`} color="link" size="sm">
                      {workTemplateItem.id}
                    </Button>
                  </td>
                  <td>{workTemplateItem.task}</td>
                  <td>{workTemplateItem.requiredToComplete ? 'true' : 'false'}</td>
                  <td>{workTemplateItem.sequenceOrder}</td>
                  <td>
                    {workTemplateItem.workTemplate ? (
                      <Link to={`work-template/${workTemplateItem.workTemplate.id}`}>{workTemplateItem.workTemplate.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${workTemplateItem.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workTemplateItem.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workTemplateItem.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Work Template Items found</div>
        )}
      </div>
    </div>
  );
};

export default WorkTemplateItem;
