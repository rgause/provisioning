import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './work-template-item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkTemplateItemDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workTemplateItemEntity = useAppSelector(state => state.workTemplateItem.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workTemplateItemDetailsHeading">WorkTemplateItem</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{workTemplateItemEntity.id}</dd>
          <dt>
            <span id="task">Task</span>
          </dt>
          <dd>{workTemplateItemEntity.task}</dd>
          <dt>
            <span id="requiredToComplete">Required To Complete</span>
          </dt>
          <dd>{workTemplateItemEntity.requiredToComplete ? 'true' : 'false'}</dd>
          <dt>
            <span id="sequenceOrder">Sequence Order</span>
          </dt>
          <dd>{workTemplateItemEntity.sequenceOrder}</dd>
          <dt>Work Template</dt>
          <dd>{workTemplateItemEntity.workTemplate ? workTemplateItemEntity.workTemplate.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/work-template-item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/work-template-item/${workTemplateItemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkTemplateItemDetail;
