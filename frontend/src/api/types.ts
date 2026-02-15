export type EnergyLevel = 'HIGH' | 'LOW';
export type TaskStatus = 'PENDING' | 'COMPLETED';

export type Task = {
  id: number;
  title: string;
  description?: string | null;
  taskDate: string;
  status: TaskStatus;
  energyLevel: EnergyLevel;
  userId: number;
  createdAt: string;
};

export type DailyTaskTemplate = {
  id: number;
  userId: number;
  title: string;
  description?: string | null;
  active: boolean;
  createdAt: string;
  energyLevel: EnergyLevel;
};

export type DailyTaskLog = {
  id: number;
  userId: number;
  template: DailyTaskTemplate;
  taskDate: string;
  status: TaskStatus;
  createdAt: string;
};

export type DailyPlanResponse = {
  dailyLogs: DailyTaskLog[];
  pendingTasks: Task[];
};

export type WorkTimerStatusResponse = {
  date: string;
  totalSeconds: number;
  running: boolean;
  runningStartAt?: string | null;
};

export type RewardItem = {
  id: number;
  userId: number;
  title: string;
  enabled: boolean;
  createdAt: string;
};

export type OpenChestResponse = {
  opened: boolean;
  message: string;
  reward?: RewardItem | null;
  completedCount: number;
  requiredCount: number;
};

export type ErrorResponse = {
  timestamp: string;
  status: number;
  message: string;
};
