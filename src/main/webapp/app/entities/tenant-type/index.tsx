import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TenantType from './tenant-type';
import TenantTypeDetail from './tenant-type-detail';
import TenantTypeUpdate from './tenant-type-update';
import TenantTypeDeleteDialog from './tenant-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TenantTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TenantTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TenantTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={TenantType} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TenantTypeDeleteDialog} />
  </>
);

export default Routes;
