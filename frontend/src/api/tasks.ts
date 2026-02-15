import { apiRequest } from './client';
import { Task } from './types';

export type CreateTaskPayload = {
  title: string;
  description?: string | null;
  taskDate: string;
  energyLevel: 'HIGH' | 'LOW';
  userId: number;
};

export type UpdateTaskPayload = {
  title?: string | null;
  description?: string | null;
  energyLevel?: 'HIGH' | 'LOW' | null;
};

export const getPendingTasks = (userId: number) =>
  apiRequest<Task[]>('/tasks', { query: { userId } });

export const createTask = (payload: CreateTaskPayload) =>
  apiRequest<Task>('/tasks', { method: 'POST', body: payload });

export const updateTask = (id: number, payload: UpdateTaskPayload) =>
  apiRequest<Task>(`/tasks/${id}`, { method: 'PATCH', body: payload });

export const completeTask = (id: number) =>
  apiRequest<Task>(`/tasks/${id}/complete`, { method: 'PATCH' });

export const deleteTask = (id: number) =>
  apiRequest<void>(`/tasks/${id}`, { method: 'DELETE' });
