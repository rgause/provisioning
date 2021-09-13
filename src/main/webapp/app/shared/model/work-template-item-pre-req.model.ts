import { IWorkTemplateItem } from 'app/shared/model/work-template-item.model';

export interface IWorkTemplateItemPreReq {
  id?: number;
  preRequisiteItem?: number;
  workTemplateItem?: IWorkTemplateItem | null;
}

export const defaultValue: Readonly<IWorkTemplateItemPreReq> = {};
