import { IWorkQueueTenantUser } from 'app/shared/model/work-queue-tenant-user.model';
import { ILanUser } from 'app/shared/model/lan-user.model';
import { ITenant } from 'app/shared/model/tenant.model';

export interface ITenantUser {
  id?: number;
  active?: boolean;
  provisioned?: boolean;
  workQueueTenantUsers?: IWorkQueueTenantUser[] | null;
  lanUser?: ILanUser | null;
  tenant?: ITenant | null;
}

export const defaultValue: Readonly<ITenantUser> = {
  active: false,
  provisioned: false,
};
