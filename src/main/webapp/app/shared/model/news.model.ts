import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface INews {
  id?: number;
  headerline?: string;
  url?: string;
  pubDate?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<INews> = {};
