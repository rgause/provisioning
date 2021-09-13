import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkQueueTenantData from './work-queue-tenant-data';
import WorkQueueTenantDataDetail from './work-queue-tenant-data-detail';
import WorkQueueTenantDataUpdate from './work-queue-tenant-data-update';
import WorkQueueTenantDataDeleteDialog from './work-queue-tenant-data-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkQueueTenantDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkQueueTenantDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkQueueTenantDataDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkQueueTenantData} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkQueueTenantDataDeleteDialog} />
  </>
);

export default Routes;
