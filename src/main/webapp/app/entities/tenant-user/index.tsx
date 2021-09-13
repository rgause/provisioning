import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TenantUser from './tenant-user';
import TenantUserDetail from './tenant-user-detail';
import TenantUserUpdate from './tenant-user-update';
import TenantUserDeleteDialog from './tenant-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TenantUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TenantUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TenantUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={TenantUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TenantUserDeleteDialog} />
  </>
);

export default Routes;
