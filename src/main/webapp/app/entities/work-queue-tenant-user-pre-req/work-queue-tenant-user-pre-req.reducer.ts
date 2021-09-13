import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IWorkQueueTenantUserPreReq, defaultValue } from 'app/shared/model/work-queue-tenant-user-pre-req.model';

const initialState: EntityState<IWorkQueueTenantUserPreReq> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/work-queue-tenant-user-pre-reqs';

// Actions

export const getEntities = createAsyncThunk('workQueueTenantUserPreReq/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IWorkQueueTenantUserPreReq[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'workQueueTenantUserPreReq/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IWorkQueueTenantUserPreReq>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'workQueueTenantUserPreReq/create_entity',
  async (entity: IWorkQueueTenantUserPreReq, thunkAPI) => {
    const result = await axios.post<IWorkQueueTenantUserPreReq>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'workQueueTenantUserPreReq/update_entity',
  async (entity: IWorkQueueTenantUserPreReq, thunkAPI) => {
    const result = await axios.put<IWorkQueueTenantUserPreReq>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'workQueueTenantUserPreReq/partial_update_entity',
  async (entity: IWorkQueueTenantUserPreReq, thunkAPI) => {
    const result = await axios.patch<IWorkQueueTenantUserPreReq>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'workQueueTenantUserPreReq/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IWorkQueueTenantUserPreReq>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const WorkQueueTenantUserPreReqSlice = createEntitySlice({
  name: 'workQueueTenantUserPreReq',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = WorkQueueTenantUserPreReqSlice.actions;

// Reducer
export default WorkQueueTenantUserPreReqSlice.reducer;
