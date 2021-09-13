import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import URLType from './url-type';
import URLTypeDetail from './url-type-detail';
import URLTypeUpdate from './url-type-update';
import URLTypeDeleteDialog from './url-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={URLTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={URLTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={URLTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={URLType} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={URLTypeDeleteDialog} />
  </>
);

export default Routes;
