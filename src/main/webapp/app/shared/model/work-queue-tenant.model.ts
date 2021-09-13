import dayjs from 'dayjs';
import { IWorkQueueTenantData } from 'app/shared/model/work-queue-tenant-data.model';
import { IWorkQueueTenantPreReq } from 'app/shared/model/work-queue-tenant-pre-req.model';
import { ITenant } from 'app/shared/model/tenant.model';

export interface IWorkQueueTenant {
  id?: number;
  task?: string;
  requiredToComplete?: boolean;
  dateCreated?: string | null;
  dateCancelled?: string | null;
  dateCompleted?: string | null;
  sequenceOrder?: number;
  workQueueTenantData?: IWorkQueueTenantData[] | null;
  workQueueTenantPreReqs?: IWorkQueueTenantPreReq[] | null;
  tenant?: ITenant | null;
}

export const defaultValue: Readonly<IWorkQueueTenant> = {
  requiredToComplete: false,
};
