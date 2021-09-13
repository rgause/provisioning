import { IWorkQueueTenant } from 'app/shared/model/work-queue-tenant.model';

export interface IWorkQueueTenantPreReq {
  id?: number;
  preRequisiteItem?: number;
  workQueueItem?: IWorkQueueTenant | null;
}

export const defaultValue: Readonly<IWorkQueueTenantPreReq> = {};
