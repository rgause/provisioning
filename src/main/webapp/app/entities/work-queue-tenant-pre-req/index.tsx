import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkQueueTenantPreReq from './work-queue-tenant-pre-req';
import WorkQueueTenantPreReqDetail from './work-queue-tenant-pre-req-detail';
import WorkQueueTenantPreReqUpdate from './work-queue-tenant-pre-req-update';
import WorkQueueTenantPreReqDeleteDialog from './work-queue-tenant-pre-req-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkQueueTenantPreReqUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkQueueTenantPreReqUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkQueueTenantPreReqDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkQueueTenantPreReq} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkQueueTenantPreReqDeleteDialog} />
  </>
);

export default Routes;
