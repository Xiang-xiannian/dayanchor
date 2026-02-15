import { apiRequest } from './client';
import { DailyTaskLog } from './types';

export const completeDailyLog = (id: number) =>
  apiRequest<DailyTaskLog>(`/daily-task-logs/${id}/complete`, { method: 'PATCH' });
