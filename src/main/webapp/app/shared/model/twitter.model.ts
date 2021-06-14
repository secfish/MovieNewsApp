import dayjs from 'dayjs';
import { IMovie } from 'app/shared/model/movie.model';

export interface ITwitter {
  id?: number;
  content?: string;
  pubDate?: string | null;
  publisher?: string | null;
  movie?: IMovie | null;
}

export const defaultValue: Readonly<ITwitter> = {};
