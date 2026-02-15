import { apiRequest } from './client';
import { DailyPlanResponse } from './types';

export const getDailyPlan = (userId: number, date: string) =>
  apiRequest<DailyPlanResponse>('/daily-plan', { query: { userId, date } });

export const getCompletedCount = (userId: number, date: string) =>
  apiRequest<number>('/daily-plan/completed-count', { query: { userId, date } });
