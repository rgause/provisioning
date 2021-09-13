import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorkTemplate } from 'app/shared/model/work-template.model';
import { getEntities as getWorkTemplates } from 'app/entities/work-template/work-template.reducer';
import { getEntity, updateEntity, createEntity, reset } from './work-template-item.reducer';
import { IWorkTemplateItem } from 'app/shared/model/work-template-item.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkTemplateItemUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workTemplates = useAppSelector(state => state.workTemplate.entities);
  const workTemplateItemEntity = useAppSelector(state => state.workTemplateItem.entity);
  const loading = useAppSelector(state => state.workTemplateItem.loading);
  const updating = useAppSelector(state => state.workTemplateItem.updating);
  const updateSuccess = useAppSelector(state => state.workTemplateItem.updateSuccess);

  const handleClose = () => {
    props.history.push('/work-template-item');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkTemplates({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...workTemplateItemEntity,
      ...values,
      workTemplate: workTemplates.find(it => it.id.toString() === values.workTemplateId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...workTemplateItemEntity,
          workTemplateId: workTemplateItemEntity?.workTemplate?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="provisioningApp.workTemplateItem.home.createOrEditLabel" data-cy="WorkTemplateItemCreateUpdateHeading">
            Create or edit a WorkTemplateItem
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="work-template-item-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Task"
                id="work-template-item-task"
                name="task"
                data-cy="task"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Required To Complete"
                id="work-template-item-requiredToComplete"
                name="requiredToComplete"
                data-cy="requiredToComplete"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Sequence Order"
                id="work-template-item-sequenceOrder"
                name="sequenceOrder"
                data-cy="sequenceOrder"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField
                id="work-template-item-workTemplate"
                name="workTemplateId"
                data-cy="workTemplate"
                label="Work Template"
                type="select"
              >
                <option value="" key="0" />
                {workTemplates
                  ? workTemplates.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/work-template-item" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default WorkTemplateItemUpdate;
