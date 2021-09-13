import { ITenantProperty } from 'app/shared/model/tenant-property.model';

export interface ITenantPropertyKey {
  id?: number;
  name?: string;
  tenantProperties?: ITenantProperty[] | null;
}

export const defaultValue: Readonly<ITenantPropertyKey> = {};
