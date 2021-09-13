import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
import sessions, { SessionsState } from 'app/modules/account/sessions/sessions.reducer';
// prettier-ignore
import tenantType from 'app/entities/tenant-type/tenant-type.reducer';
// prettier-ignore
import tenant from 'app/entities/tenant/tenant.reducer';
// prettier-ignore
import uRLType from 'app/entities/url-type/url-type.reducer';
// prettier-ignore
import uRL from 'app/entities/url/url.reducer';
// prettier-ignore
import tenantPropertyKey from 'app/entities/tenant-property-key/tenant-property-key.reducer';
// prettier-ignore
import tenantProperty from 'app/entities/tenant-property/tenant-property.reducer';
// prettier-ignore
import lanUser from 'app/entities/lan-user/lan-user.reducer';
// prettier-ignore
import tenantUser from 'app/entities/tenant-user/tenant-user.reducer';
// prettier-ignore
import workTemplate from 'app/entities/work-template/work-template.reducer';
// prettier-ignore
import workTemplateItem from 'app/entities/work-template-item/work-template-item.reducer';
// prettier-ignore
import workTemplateItemPreReq from 'app/entities/work-template-item-pre-req/work-template-item-pre-req.reducer';
// prettier-ignore
import workQueueTenant from 'app/entities/work-queue-tenant/work-queue-tenant.reducer';
// prettier-ignore
import workQueueTenantData from 'app/entities/work-queue-tenant-data/work-queue-tenant-data.reducer';
// prettier-ignore
import workQueueTenantPreReq from 'app/entities/work-queue-tenant-pre-req/work-queue-tenant-pre-req.reducer';
// prettier-ignore
import workQueueTenantUser from 'app/entities/work-queue-tenant-user/work-queue-tenant-user.reducer';
// prettier-ignore
import workQueueTenantUserData from 'app/entities/work-queue-tenant-user-data/work-queue-tenant-user-data.reducer';
// prettier-ignore
import workQueueTenantUserPreReq from 'app/entities/work-queue-tenant-user-pre-req/work-queue-tenant-user-pre-req.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  sessions,
  tenantType,
  tenant,
  uRLType,
  uRL,
  tenantPropertyKey,
  tenantProperty,
  lanUser,
  tenantUser,
  workTemplate,
  workTemplateItem,
  workTemplateItemPreReq,
  workQueueTenant,
  workQueueTenantData,
  workQueueTenantPreReq,
  workQueueTenantUser,
  workQueueTenantUserData,
  workQueueTenantUserPreReq,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
