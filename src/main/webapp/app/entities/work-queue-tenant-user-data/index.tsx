import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkQueueTenantUserData from './work-queue-tenant-user-data';
import WorkQueueTenantUserDataDetail from './work-queue-tenant-user-data-detail';
import WorkQueueTenantUserDataUpdate from './work-queue-tenant-user-data-update';
import WorkQueueTenantUserDataDeleteDialog from './work-queue-tenant-user-data-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkQueueTenantUserDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkQueueTenantUserDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkQueueTenantUserDataDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkQueueTenantUserData} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkQueueTenantUserDataDeleteDialog} />
  </>
);

export default Routes;
