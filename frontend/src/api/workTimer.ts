import { apiRequest } from './client';
import { WorkTimerStatusResponse } from './types';

export const getTodayTimer = (userId: number) =>
  apiRequest<WorkTimerStatusResponse>('/work-timer/today', { query: { userId } });

export const startTodayTimer = (userId: number) =>
  apiRequest<WorkTimerStatusResponse>('/work-timer/today/start', { method: 'POST', query: { userId } });

export const pauseTodayTimer = (userId: number) =>
  apiRequest<WorkTimerStatusResponse>('/work-timer/today/pause', { method: 'POST', query: { userId } });

export const getTimerByDate = (userId: number, date: string) =>
  apiRequest<WorkTimerStatusResponse>('/work-timer/by-date', { query: { userId, date } });
