import { ITenantUser } from 'app/shared/model/tenant-user.model';

export interface ILanUser {
  id?: number;
  active?: boolean;
  lanId?: string;
  pwpId?: string;
  lastName?: string;
  firstName?: string;
  tenantUsers?: ITenantUser[] | null;
}

export const defaultValue: Readonly<ILanUser> = {
  active: false,
};
