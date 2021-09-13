import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Tenant from './tenant';
import TenantDetail from './tenant-detail';
import TenantUpdate from './tenant-update';
import TenantDeleteDialog from './tenant-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TenantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TenantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TenantDetail} />
      <ErrorBoundaryRoute path={match.url} component={Tenant} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TenantDeleteDialog} />
  </>
);

export default Routes;
