import dayjs from 'dayjs';
import { ITwitter } from 'app/shared/model/twitter.model';
import { IUser } from 'app/shared/model/user.model';

export interface IMovie {
  id?: number;
  name?: string;
  director?: string | null;
  synopsis?: string | null;
  comment?: string | null;
  startDate?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  twitters?: ITwitter[] | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IMovie> = {};
