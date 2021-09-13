import { IWorkTemplateItemPreReq } from 'app/shared/model/work-template-item-pre-req.model';
import { IWorkTemplate } from 'app/shared/model/work-template.model';

export interface IWorkTemplateItem {
  id?: number;
  task?: string;
  requiredToComplete?: boolean;
  sequenceOrder?: number;
  workTemplateItemPreReqs?: IWorkTemplateItemPreReq[] | null;
  workTemplate?: IWorkTemplate | null;
}

export const defaultValue: Readonly<IWorkTemplateItem> = {
  requiredToComplete: false,
};
