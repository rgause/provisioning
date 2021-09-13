import { IURLType } from 'app/shared/model/url-type.model';
import { ITenant } from 'app/shared/model/tenant.model';

export interface IURL {
  id?: number;
  url?: string;
  urlType?: IURLType | null;
  tenant?: ITenant | null;
}

export const defaultValue: Readonly<IURL> = {};
