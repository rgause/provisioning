import dayjs from 'dayjs';
import { IWorkQueueTenantUserData } from 'app/shared/model/work-queue-tenant-user-data.model';
import { IWorkQueueTenantUserPreReq } from 'app/shared/model/work-queue-tenant-user-pre-req.model';
import { ITenantUser } from 'app/shared/model/tenant-user.model';

export interface IWorkQueueTenantUser {
  id?: number;
  task?: string;
  requiredToComplete?: boolean;
  dateCreated?: string | null;
  dateCancelled?: string | null;
  dateCompleted?: string | null;
  sequenceOrder?: number;
  workQueueTenantUserData?: IWorkQueueTenantUserData[] | null;
  workQueueTenantUserPreReqs?: IWorkQueueTenantUserPreReq[] | null;
  tenantUser?: ITenantUser | null;
}

export const defaultValue: Readonly<IWorkQueueTenantUser> = {
  requiredToComplete: false,
};
