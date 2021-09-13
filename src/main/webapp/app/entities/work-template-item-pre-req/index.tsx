import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkTemplateItemPreReq from './work-template-item-pre-req';
import WorkTemplateItemPreReqDetail from './work-template-item-pre-req-detail';
import WorkTemplateItemPreReqUpdate from './work-template-item-pre-req-update';
import WorkTemplateItemPreReqDeleteDialog from './work-template-item-pre-req-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkTemplateItemPreReqUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkTemplateItemPreReqUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkTemplateItemPreReqDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkTemplateItemPreReq} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkTemplateItemPreReqDeleteDialog} />
  </>
);

export default Routes;
