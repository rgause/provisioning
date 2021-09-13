import { IWorkQueueTenantUser } from 'app/shared/model/work-queue-tenant-user.model';

export interface IWorkQueueTenantUserData {
  id?: number;
  data?: string;
  type?: string;
  workQueueTenantUser?: IWorkQueueTenantUser | null;
}

export const defaultValue: Readonly<IWorkQueueTenantUserData> = {};
