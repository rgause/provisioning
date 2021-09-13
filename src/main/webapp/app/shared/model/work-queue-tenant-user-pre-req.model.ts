import { IWorkQueueTenantUser } from 'app/shared/model/work-queue-tenant-user.model';

export interface IWorkQueueTenantUserPreReq {
  id?: number;
  preRequisiteItem?: number;
  workQueueTenantUser?: IWorkQueueTenantUser | null;
}

export const defaultValue: Readonly<IWorkQueueTenantUserPreReq> = {};
