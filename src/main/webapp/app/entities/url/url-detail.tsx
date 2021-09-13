import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './url.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const URLDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const uRLEntity = useAppSelector(state => state.uRL.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="uRLDetailsHeading">URL</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{uRLEntity.id}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{uRLEntity.url}</dd>
          <dt>Url Type</dt>
          <dd>{uRLEntity.urlType ? uRLEntity.urlType.id : ''}</dd>
          <dt>Tenant</dt>
          <dd>{uRLEntity.tenant ? uRLEntity.tenant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/url" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/url/${uRLEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default URLDetail;
