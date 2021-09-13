import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkQueueTenantUser from './work-queue-tenant-user';
import WorkQueueTenantUserDetail from './work-queue-tenant-user-detail';
import WorkQueueTenantUserUpdate from './work-queue-tenant-user-update';
import WorkQueueTenantUserDeleteDialog from './work-queue-tenant-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkQueueTenantUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkQueueTenantUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkQueueTenantUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkQueueTenantUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkQueueTenantUserDeleteDialog} />
  </>
);

export default Routes;
