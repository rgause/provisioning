import { IWorkTemplateItem } from 'app/shared/model/work-template-item.model';

export interface IWorkTemplate {
  id?: number;
  name?: string;
  workTemplateItems?: IWorkTemplateItem[] | null;
}

export const defaultValue: Readonly<IWorkTemplate> = {};
