import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LanUser from './lan-user';
import LanUserDetail from './lan-user-detail';
import LanUserUpdate from './lan-user-update';
import LanUserDeleteDialog from './lan-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LanUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LanUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LanUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={LanUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LanUserDeleteDialog} />
  </>
);

export default Routes;
