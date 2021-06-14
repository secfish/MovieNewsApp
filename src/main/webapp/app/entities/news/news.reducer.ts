import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction,
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { INews, defaultValue } from 'app/shared/model/news.model';

export const ACTION_TYPES = {
  FETCH_NEWS_LIST: 'news/FETCH_NEWS_LIST',
  FETCH_NEWS: 'news/FETCH_NEWS',
  CREATE_NEWS: 'news/CREATE_NEWS',
  UPDATE_NEWS: 'news/UPDATE_NEWS',
  PARTIAL_UPDATE_NEWS: 'news/PARTIAL_UPDATE_NEWS',
  DELETE_NEWS: 'news/DELETE_NEWS',
  SET_BLOB: 'news/SET_BLOB',
  RESET: 'news/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<INews>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type NewsState = Readonly<typeof initialState>;

// Reducer

export default (state: NewsState = initialState, action): NewsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_NEWS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_NEWS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_NEWS):
    case REQUEST(ACTION_TYPES.UPDATE_NEWS):
    case REQUEST(ACTION_TYPES.DELETE_NEWS):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_NEWS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_NEWS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_NEWS):
    case FAILURE(ACTION_TYPES.CREATE_NEWS):
    case FAILURE(ACTION_TYPES.UPDATE_NEWS):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_NEWS):
    case FAILURE(ACTION_TYPES.DELETE_NEWS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_NEWS_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_NEWS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_NEWS):
    case SUCCESS(ACTION_TYPES.UPDATE_NEWS):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_NEWS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_NEWS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.SET_BLOB: {
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType,
        },
      };
    }
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/news';

// Actions

export const getEntities: ICrudGetAllAction<INews> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_NEWS_LIST,
    payload: axios.get<INews>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<INews> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_NEWS,
    payload: axios.get<INews>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<INews> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_NEWS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<INews> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_NEWS,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<INews> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_NEWS,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<INews> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_NEWS,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType,
  },
});

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
