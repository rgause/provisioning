import { IWorkQueueTenant } from 'app/shared/model/work-queue-tenant.model';

export interface IWorkQueueTenantData {
  id?: number;
  data?: string;
  type?: string;
  workQueueTenant?: IWorkQueueTenant | null;
}

export const defaultValue: Readonly<IWorkQueueTenantData> = {};
