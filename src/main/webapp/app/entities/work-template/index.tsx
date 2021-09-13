import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkTemplate from './work-template';
import WorkTemplateDetail from './work-template-detail';
import WorkTemplateUpdate from './work-template-update';
import WorkTemplateDeleteDialog from './work-template-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkTemplateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkTemplateUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkTemplateDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkTemplate} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkTemplateDeleteDialog} />
  </>
);

export default Routes;
