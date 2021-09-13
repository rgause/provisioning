import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkTemplateItem from './work-template-item';
import WorkTemplateItemDetail from './work-template-item-detail';
import WorkTemplateItemUpdate from './work-template-item-update';
import WorkTemplateItemDeleteDialog from './work-template-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkTemplateItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkTemplateItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkTemplateItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkTemplateItem} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkTemplateItemDeleteDialog} />
  </>
);

export default Routes;
