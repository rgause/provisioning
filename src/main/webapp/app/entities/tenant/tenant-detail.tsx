import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './tenant.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TenantDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tenantEntity = useAppSelector(state => state.tenant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tenantDetailsHeading">Tenant</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tenantEntity.id}</dd>
          <dt>
            <span id="active">Active</span>
          </dt>
          <dd>{tenantEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="provisioned">Provisioned</span>
          </dt>
          <dd>{tenantEntity.provisioned ? 'true' : 'false'}</dd>
          <dt>
            <span id="frpId">Frp Id</span>
          </dt>
          <dd>{tenantEntity.frpId}</dd>
          <dt>
            <span id="frpName">Frp Name</span>
          </dt>
          <dd>{tenantEntity.frpName}</dd>
          <dt>
            <span id="frpContractTypeCode">Frp Contract Type Code</span>
          </dt>
          <dd>{tenantEntity.frpContractTypeCode}</dd>
          <dt>
            <span id="frpContractTypeIndicator">Frp Contract Type Indicator</span>
          </dt>
          <dd>{tenantEntity.frpContractTypeIndicator}</dd>
          <dt>Tenant Type</dt>
          <dd>{tenantEntity.tenantType ? tenantEntity.tenantType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tenant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tenant/${tenantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TenantDetail;
