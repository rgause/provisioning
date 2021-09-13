import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import URL from './url';
import URLDetail from './url-detail';
import URLUpdate from './url-update';
import URLDeleteDialog from './url-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={URLUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={URLUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={URLDetail} />
      <ErrorBoundaryRoute path={match.url} component={URL} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={URLDeleteDialog} />
  </>
);

export default Routes;
