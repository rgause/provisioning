import { ITenant } from 'app/shared/model/tenant.model';
import { ITenantPropertyKey } from 'app/shared/model/tenant-property-key.model';

export interface ITenantProperty {
  id?: number;
  value?: string;
  tenant?: ITenant | null;
  tenantPropertyKey?: ITenantPropertyKey | null;
}

export const defaultValue: Readonly<ITenantProperty> = {};
