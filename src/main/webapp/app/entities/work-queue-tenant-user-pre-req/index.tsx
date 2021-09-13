import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WorkQueueTenantUserPreReq from './work-queue-tenant-user-pre-req';
import WorkQueueTenantUserPreReqDetail from './work-queue-tenant-user-pre-req-detail';
import WorkQueueTenantUserPreReqUpdate from './work-queue-tenant-user-pre-req-update';
import WorkQueueTenantUserPreReqDeleteDialog from './work-queue-tenant-user-pre-req-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkQueueTenantUserPreReqUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkQueueTenantUserPreReqUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkQueueTenantUserPreReqDetail} />
      <ErrorBoundaryRoute path={match.url} component={WorkQueueTenantUserPreReq} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkQueueTenantUserPreReqDeleteDialog} />
  </>
);

export default Routes;
