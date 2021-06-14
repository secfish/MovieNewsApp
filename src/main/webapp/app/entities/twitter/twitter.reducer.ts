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

import { ITwitter, defaultValue } from 'app/shared/model/twitter.model';

export const ACTION_TYPES = {
  FETCH_TWITTER_LIST: 'twitter/FETCH_TWITTER_LIST',
  FETCH_TWITTER: 'twitter/FETCH_TWITTER',
  CREATE_TWITTER: 'twitter/CREATE_TWITTER',
  UPDATE_TWITTER: 'twitter/UPDATE_TWITTER',
  PARTIAL_UPDATE_TWITTER: 'twitter/PARTIAL_UPDATE_TWITTER',
  DELETE_TWITTER: 'twitter/DELETE_TWITTER',
  RESET: 'twitter/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITwitter>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type TwitterState = Readonly<typeof initialState>;

// Reducer

export default (state: TwitterState = initialState, action): TwitterState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TWITTER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TWITTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_TWITTER):
    case REQUEST(ACTION_TYPES.UPDATE_TWITTER):
    case REQUEST(ACTION_TYPES.DELETE_TWITTER):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_TWITTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_TWITTER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TWITTER):
    case FAILURE(ACTION_TYPES.CREATE_TWITTER):
    case FAILURE(ACTION_TYPES.UPDATE_TWITTER):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_TWITTER):
    case FAILURE(ACTION_TYPES.DELETE_TWITTER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TWITTER_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_TWITTER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_TWITTER):
    case SUCCESS(ACTION_TYPES.UPDATE_TWITTER):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_TWITTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_TWITTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/twitters';

// Actions

export const getEntities: ICrudGetAllAction<ITwitter> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TWITTER_LIST,
    payload: axios.get<ITwitter>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ITwitter> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TWITTER,
    payload: axios.get<ITwitter>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ITwitter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TWITTER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<ITwitter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TWITTER,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ITwitter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_TWITTER,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITwitter> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TWITTER,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
