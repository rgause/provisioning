import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TenantPropertyKey from './tenant-property-key';
import TenantPropertyKeyDetail from './tenant-property-key-detail';
import TenantPropertyKeyUpdate from './tenant-property-key-update';
import TenantPropertyKeyDeleteDialog from './tenant-property-key-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TenantPropertyKeyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TenantPropertyKeyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TenantPropertyKeyDetail} />
      <ErrorBoundaryRoute path={match.url} component={TenantPropertyKey} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TenantPropertyKeyDeleteDialog} />
  </>
);

export default Routes;
