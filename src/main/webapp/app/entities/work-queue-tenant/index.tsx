import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkQueueTenant from './work-queue-tenant';
import WorkQueueTenantDetail from './work-queue-tenant-detail';
import WorkQueueTenantUpdate from './work-queue-tenant-update';
import WorkQueueTenantDeleteDialog from './work-queue-tenant-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkQueueTenantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkQueueTenantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkQueueTenantDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkQueueTenant} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkQueueTenantDeleteDialog} />
  </>
);

export default Routes;
