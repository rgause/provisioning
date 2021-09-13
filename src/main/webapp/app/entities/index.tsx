import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import TenantType from './tenant-type';
import Tenant from './tenant';
import URLType from './url-type';
import URL from './url';
import TenantPropertyKey from './tenant-property-key';
import TenantProperty from './tenant-property';
import LanUser from './lan-user';
import TenantUser from './tenant-user';
import WorkTemplate from './work-template';
import WorkTemplateItem from './work-template-item';
import WorkTemplateItemPreReq from './work-template-item-pre-req';
import WorkQueueTenant from './work-queue-tenant';
import WorkQueueTenantData from './work-queue-tenant-data';
import WorkQueueTenantPreReq from './work-queue-tenant-pre-req';
import WorkQueueTenantUser from './work-queue-tenant-user';
import WorkQueueTenantUserData from './work-queue-tenant-user-data';
import WorkQueueTenantUserPreReq from './work-queue-tenant-user-pre-req';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}tenant-type`} component={TenantType} />
      <ErrorBoundaryRoute path={`${match.url}tenant`} component={Tenant} />
      <ErrorBoundaryRoute path={`${match.url}url-type`} component={URLType} />
      <ErrorBoundaryRoute path={`${match.url}url`} component={URL} />
      <ErrorBoundaryRoute path={`${match.url}tenant-property-key`} component={TenantPropertyKey} />
      <ErrorBoundaryRoute path={`${match.url}tenant-property`} component={TenantProperty} />
      <ErrorBoundaryRoute path={`${match.url}lan-user`} component={LanUser} />
      <ErrorBoundaryRoute path={`${match.url}tenant-user`} component={TenantUser} />
      <ErrorBoundaryRoute path={`${match.url}work-template`} component={WorkTemplate} />
      <ErrorBoundaryRoute path={`${match.url}work-template-item`} component={WorkTemplateItem} />
      <ErrorBoundaryRoute path={`${match.url}work-template-item-pre-req`} component={WorkTemplateItemPreReq} />
      <ErrorBoundaryRoute path={`${match.url}work-queue-tenant`} component={WorkQueueTenant} />
      <ErrorBoundaryRoute path={`${match.url}work-queue-tenant-data`} component={WorkQueueTenantData} />
      <ErrorBoundaryRoute path={`${match.url}work-queue-tenant-pre-req`} component={WorkQueueTenantPreReq} />
      <ErrorBoundaryRoute path={`${match.url}work-queue-tenant-user`} component={WorkQueueTenantUser} />
      <ErrorBoundaryRoute path={`${match.url}work-queue-tenant-user-data`} component={WorkQueueTenantUserData} />
      <ErrorBoundaryRoute path={`${match.url}work-queue-tenant-user-pre-req`} component={WorkQueueTenantUserPreReq} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
