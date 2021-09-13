import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './lan-user.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LanUserDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const lanUserEntity = useAppSelector(state => state.lanUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="lanUserDetailsHeading">LanUser</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{lanUserEntity.id}</dd>
          <dt>
            <span id="active">Active</span>
          </dt>
          <dd>{lanUserEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="lanId">Lan Id</span>
          </dt>
          <dd>{lanUserEntity.lanId}</dd>
          <dt>
            <span id="pwpId">Pwp Id</span>
          </dt>
          <dd>{lanUserEntity.pwpId}</dd>
          <dt>
            <span id="lastName">Last Name</span>
          </dt>
          <dd>{lanUserEntity.lastName}</dd>
          <dt>
            <span id="firstName">First Name</span>
          </dt>
          <dd>{lanUserEntity.firstName}</dd>
        </dl>
        <Button tag={Link} to="/lan-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lan-user/${lanUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default LanUserDetail;
