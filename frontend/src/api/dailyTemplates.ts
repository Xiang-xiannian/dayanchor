import { apiRequest } from './client';
import { DailyTaskTemplate } from './types';

export type CreateDailyTemplatePayload = {
  userId: number;
  title: string;
  description?: string | null;
  energyLevel: 'HIGH' | 'LOW';
  active: boolean;
};

export type UpdateDailyTemplatePayload = {
  title?: string | null;
  description?: string | null;
  energyLevel?: 'HIGH' | 'LOW' | null;
  active?: boolean | null;
};

export const createDailyTemplate = (payload: CreateDailyTemplatePayload) =>
  apiRequest<DailyTaskTemplate>('/daily-templates', { method: 'POST', body: payload });

export const updateDailyTemplate = (id: number, payload: UpdateDailyTemplatePayload) =>
  apiRequest<DailyTaskTemplate>(`/daily-templates/${id}`, { method: 'PATCH', body: payload });
