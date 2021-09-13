import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TenantProperty from './tenant-property';
import TenantPropertyDetail from './tenant-property-detail';
import TenantPropertyUpdate from './tenant-property-update';
import TenantPropertyDeleteDialog from './tenant-property-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TenantPropertyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TenantPropertyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TenantPropertyDetail} />
      <ErrorBoundaryRoute path={match.url} component={TenantProperty} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TenantPropertyDeleteDialog} />
  </>
);

export default Routes;
