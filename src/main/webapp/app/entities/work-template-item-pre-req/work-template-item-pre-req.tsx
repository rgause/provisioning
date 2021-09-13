import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './work-template-item-pre-req.reducer';
import { IWorkTemplateItemPreReq } from 'app/shared/model/work-template-item-pre-req.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkTemplateItemPreReq = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const workTemplateItemPreReqList = useAppSelector(state => state.workTemplateItemPreReq.entities);
  const loading = useAppSelector(state => state.workTemplateItemPreReq.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="work-template-item-pre-req-heading" data-cy="WorkTemplateItemPreReqHeading">
        Work Template Item Pre Reqs
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Work Template Item Pre Req
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {workTemplateItemPreReqList && workTemplateItemPreReqList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Pre Requisite Item</th>
                <th>Work Template Item</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {workTemplateItemPreReqList.map((workTemplateItemPreReq, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${workTemplateItemPreReq.id}`} color="link" size="sm">
                      {workTemplateItemPreReq.id}
                    </Button>
                  </td>
                  <td>{workTemplateItemPreReq.preRequisiteItem}</td>
                  <td>
                    {workTemplateItemPreReq.workTemplateItem ? (
                      <Link to={`work-template-item/${workTemplateItemPreReq.workTemplateItem.id}`}>
                        {workTemplateItemPreReq.workTemplateItem.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`${match.url}/${workTemplateItemPreReq.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workTemplateItemPreReq.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${workTemplateItemPreReq.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Work Template Item Pre Reqs found</div>
        )}
      </div>
    </div>
  );
};

export default WorkTemplateItemPreReq;
