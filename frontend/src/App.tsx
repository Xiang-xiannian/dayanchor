import { useEffect, useMemo, useState } from 'react';
import {
  Alert,
  AppBar,
  Box,
  BottomNavigation,
  BottomNavigationAction,
  Button,
  Card,
  CardContent,
  Checkbox,
  Chip,
  Container,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Divider,
  FormControl,
  FormControlLabel,
  IconButton,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  Switch,
  TextField,
  Toolbar,
  Typography,
} from '@mui/material';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import PlaylistAddCheckIcon from '@mui/icons-material/PlaylistAddCheck';
import TodayIcon from '@mui/icons-material/Today';
import AddIcon from '@mui/icons-material/Add';
import ListAltIcon from '@mui/icons-material/ListAlt';
import EditIcon from '@mui/icons-material/Edit';
import { getDailyPlan } from './api/dailyPlan';
import { completeDailyLog } from './api/dailyTaskLogs';
import { createDailyTemplate, updateDailyTemplate } from './api/dailyTemplates';
import { completeTask, createTask, deleteTask, getPendingTasks, updateTask } from './api/tasks';
import { getTodayTimer, pauseTodayTimer, startTodayTimer } from './api/workTimer';
import type { DailyPlanResponse, DailyTaskLog, Task, WorkTimerStatusResponse } from './api/types';
import type { ApiError } from './api/client';

const getLocalDateString = (value = new Date()) => {
  const year = value.getFullYear();
  const month = String(value.getMonth() + 1).padStart(2, '0');
  const day = String(value.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const formatSeconds = (totalSeconds: number) => {
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;
  return [hours, minutes, seconds].map((v) => String(v).padStart(2, '0')).join(':');
};

const loadUserId = () => {
  const stored = localStorage.getItem('dayanchor_userId');
  if (!stored) return 1;
  const parsed = Number(stored);
  return Number.isNaN(parsed) ? 1 : parsed;
};

type PageKey = 'timer' | 'plan';
type EnergyLevel = 'HIGH' | 'LOW';

type EditablePlan = {
  log: DailyTaskLog;
  title: string;
  description: string;
  energyLevel: EnergyLevel;
  active: boolean;
};

type EditableTask = {
  task: Task;
  title: string;
  description: string;
  taskDate: string;
  energyLevel: EnergyLevel;
};

export default function App() {
  const [page, setPage] = useState<PageKey>('timer');
  const [userId, setUserId] = useState<number>(loadUserId());
  const [selectedDate, setSelectedDate] = useState<string>(getLocalDateString());

  const [timer, setTimer] = useState<WorkTimerStatusResponse | null>(null);
  const [timerLoading, setTimerLoading] = useState<boolean>(false);
  const [timerError, setTimerError] = useState<string>('');
  const [timerFetchedAt, setTimerFetchedAt] = useState<number | null>(null);
  const [timerTick, setTimerTick] = useState<number>(Date.now());

  const [plan, setPlan] = useState<DailyPlanResponse | null>(null);
  const [planLoading, setPlanLoading] = useState<boolean>(false);
  const [planError, setPlanError] = useState<string>('');

  const [tasks, setTasks] = useState<Task[]>([]);
  const [tasksLoading, setTasksLoading] = useState<boolean>(false);
  const [tasksError, setTasksError] = useState<string>('');

  const [planDialogOpen, setPlanDialogOpen] = useState<boolean>(false);
  const [taskDialogOpen, setTaskDialogOpen] = useState<boolean>(false);

  const [planTitle, setPlanTitle] = useState<string>('');
  const [planDescription, setPlanDescription] = useState<string>('');
  const [planEnergy, setPlanEnergy] = useState<EnergyLevel>('HIGH');
  const [planActive, setPlanActive] = useState<boolean>(true);
  const [planSubmitLoading, setPlanSubmitLoading] = useState<boolean>(false);
  const [planSubmitError, setPlanSubmitError] = useState<string>('');

  const [taskTitle, setTaskTitle] = useState<string>('');
  const [taskDescription, setTaskDescription] = useState<string>('');
  const [taskDate, setTaskDate] = useState<string>(getLocalDateString());
  const [taskEnergy, setTaskEnergy] = useState<EnergyLevel>('HIGH');
  const [taskSubmitLoading, setTaskSubmitLoading] = useState<boolean>(false);
  const [taskSubmitError, setTaskSubmitError] = useState<string>('');

  const [editPlan, setEditPlan] = useState<EditablePlan | null>(null);
  const [editPlanLoading, setEditPlanLoading] = useState<boolean>(false);
  const [editPlanError, setEditPlanError] = useState<string>('');

  const [editTask, setEditTask] = useState<EditableTask | null>(null);
  const [editTaskLoading, setEditTaskLoading] = useState<boolean>(false);
  const [editTaskError, setEditTaskError] = useState<string>('');
  const [deleteTaskConfirmOpen, setDeleteTaskConfirmOpen] = useState<boolean>(false);
  const [deleteTaskLoading, setDeleteTaskLoading] = useState<boolean>(false);
  const [deleteTaskError, setDeleteTaskError] = useState<string>('');

  const todayLabel = useMemo(() => getLocalDateString(), []);

  const handleApiError = (error: unknown) => {
    const apiError = error as ApiError | undefined;
    if (apiError && typeof apiError.message === 'string') {
      return apiError.message;
    }
    return 'Request failed';
  };

  const refreshTimer = async () => {
    setTimerLoading(true);
    setTimerError('');
    try {
      const data = await getTodayTimer(userId);
      setTimer(data);
      setTimerFetchedAt(Date.now());
    } catch (error) {
      setTimerError(handleApiError(error));
    } finally {
      setTimerLoading(false);
    }
  };

  const refreshPlan = async () => {
    setPlanLoading(true);
    setPlanError('');
    try {
      const data = await getDailyPlan(userId, selectedDate);
      setPlan(data);
    } catch (error) {
      setPlanError(handleApiError(error));
    } finally {
      setPlanLoading(false);
    }
  };

  const refreshTasks = async () => {
    setTasksLoading(true);
    setTasksError('');
    try {
      const data = await getPendingTasks(userId);
      setTasks(data);
    } catch (error) {
      setTasksError(handleApiError(error));
    } finally {
      setTasksLoading(false);
    }
  };

  const refreshAll = async () => {
    await Promise.all([refreshTimer(), refreshPlan(), refreshTasks()]);
  };

  useEffect(() => {
    localStorage.setItem('dayanchor_userId', String(userId));
    refreshAll();
    // TODO: confirm how userId should be obtained once login is ready.
  }, [userId]);

  useEffect(() => {
    refreshPlan();
  }, [selectedDate]);

  const handleStart = async () => {
    setTimerLoading(true);
    setTimerError('');
    try {
      const data = await startTodayTimer(userId);
      setTimer(data);
      setTimerFetchedAt(Date.now());
    } catch (error) {
      setTimerError(handleApiError(error));
    } finally {
      setTimerLoading(false);
    }
  };

  const handlePause = async () => {
    setTimerLoading(true);
    setTimerError('');
    try {
      const data = await pauseTodayTimer(userId);
      setTimer(data);
      setTimerFetchedAt(Date.now());
    } catch (error) {
      setTimerError(handleApiError(error));
    } finally {
      setTimerLoading(false);
    }
  };

  useEffect(() => {
    if (!timer?.running) return;
    const id = window.setInterval(() => {
      setTimerTick(Date.now());
    }, 1000);
    return () => window.clearInterval(id);
  }, [timer?.running]);

  const openPlanDialog = () => {
    setPlanSubmitError('');
    setPlanDialogOpen(true);
  };

  const openTaskDialog = () => {
    setTaskSubmitError('');
    setTaskDialogOpen(true);
  };

  const handleCreatePlan = async () => {
    if (!planTitle.trim()) {
      setPlanSubmitError('标题不能为空');
      return;
    }

    setPlanSubmitLoading(true);
    setPlanSubmitError('');
    try {
      await createDailyTemplate({
        userId,
        title: planTitle.trim(),
        description: planDescription.trim() ? planDescription.trim() : null,
        energyLevel: planEnergy,
        active: planActive,
      });
      setPlanTitle('');
      setPlanDescription('');
      setPlanEnergy('HIGH');
      setPlanActive(true);
      setPlanDialogOpen(false);
      await refreshPlan();
    } catch (error) {
      setPlanSubmitError(handleApiError(error));
    } finally {
      setPlanSubmitLoading(false);
    }
  };

  const handleCreateTask = async () => {
    if (!taskTitle.trim()) {
      setTaskSubmitError('标题不能为空');
      return;
    }

    setTaskSubmitLoading(true);
    setTaskSubmitError('');
    try {
      await createTask({
        userId,
        title: taskTitle.trim(),
        description: taskDescription.trim() ? taskDescription.trim() : null,
        taskDate,
        energyLevel: taskEnergy,
      });
      setTaskTitle('');
      setTaskDescription('');
      setTaskDate(getLocalDateString());
      setTaskEnergy('HIGH');
      setTaskDialogOpen(false);
      await refreshTasks();
    } catch (error) {
      setTaskSubmitError(handleApiError(error));
    } finally {
      setTaskSubmitLoading(false);
    }
  };

  const openEditPlanDialog = (log: DailyTaskLog) => {
    setEditPlanError('');
    setEditPlan({
      log,
      title: log.template.title,
      description: log.template.description ?? '',
      energyLevel: log.template.energyLevel,
      active: log.template.active,
    });
  };

  const openEditTaskDialog = (task: Task) => {
    setEditTaskError('');
    setEditTask({
      task,
      title: task.title,
      description: task.description ?? '',
      taskDate: task.taskDate,
      energyLevel: task.energyLevel,
    });
  };

  const handleUpdatePlan = async () => {
    if (!editPlan) return;
    if (!editPlan.title.trim()) {
      setEditPlanError('标题不能为空');
      return;
    }

    setEditPlanLoading(true);
    setEditPlanError('');
    try {
      await updateDailyTemplate(editPlan.log.template.id, {
        title: editPlan.title.trim(),
        description: editPlan.description.trim() ? editPlan.description.trim() : null,
        energyLevel: editPlan.energyLevel,
        active: editPlan.active,
      });
      setEditPlan(null);
      await refreshPlan();
    } catch (error) {
      setEditPlanError(handleApiError(error));
    } finally {
      setEditPlanLoading(false);
    }
  };

  const handleUpdateTask = async () => {
    if (!editTask) return;
    if (!editTask.title.trim()) {
      setEditTaskError('标题不能为空');
      return;
    }

    setEditTaskLoading(true);
    setEditTaskError('');
    try {
      await updateTask(editTask.task.id, {
        title: editTask.title.trim(),
        description: editTask.description.trim() ? editTask.description.trim() : null,
        energyLevel: editTask.energyLevel,
      });
      setEditTask(null);
      await refreshTasks();
    } catch (error) {
      setEditTaskError(handleApiError(error));
    } finally {
      setEditTaskLoading(false);
    }
  };

  const openDeleteTaskConfirm = () => {
    setDeleteTaskError('');
    setDeleteTaskConfirmOpen(true);
  };

  const handleDeleteTask = async () => {
    if (!editTask) return;
    setDeleteTaskLoading(true);
    setDeleteTaskError('');
    try {
      await deleteTask(editTask.task.id);
      setEditTask(null);
      setDeleteTaskConfirmOpen(false);
      await refreshTasks();
    } catch (error) {
      setDeleteTaskError(handleApiError(error));
    } finally {
      setDeleteTaskLoading(false);
    }
  };

  const handleCompleteLog = async (log: DailyTaskLog) => {
    try {
      await completeDailyLog(log.id);
      await refreshPlan();
    } catch (error) {
      setPlanError(handleApiError(error));
    }
  };

  const handleCompleteTask = async (task: Task) => {
    try {
      await completeTask(task.id);
      setTasks((prev) =>
        prev.map((item) => (item.id === task.id ? { ...item, status: 'COMPLETED' } : item))
      );
      // TODO: backend only lists pending tasks; completed tasks will disappear on refresh.
    } catch (error) {
      setTasksError(handleApiError(error));
    }
  };

  const running = timer?.running ?? false;
  const baseSeconds = timer?.totalSeconds ?? 0;
  const liveSeconds =
    running && timerFetchedAt
      ? baseSeconds + Math.max(0, Math.floor((timerTick - timerFetchedAt) / 1000))
      : baseSeconds;

  const planHigh = plan?.dailyLogs.filter((log) => log.template.energyLevel === 'HIGH') ?? [];
  const planLow = plan?.dailyLogs.filter((log) => log.template.energyLevel === 'LOW') ?? [];
  const tasksHigh = tasks.filter((task) => task.energyLevel === 'HIGH');
  const tasksLow = tasks.filter((task) => task.energyLevel === 'LOW');

  return (
    <Box>
      <AppBar position="sticky" color="transparent" elevation={0}>
        <Toolbar>
          <TodayIcon sx={{ mr: 1 }} color="primary" />
          <Typography variant="h6" color="primary">
            DayAnchor
          </Typography>
        </Toolbar>
      </AppBar>

      <Container maxWidth="sm" className="app-shell">
        {page === 'timer' && (
          <Stack spacing={2}>
            <Card className="section-card">
              <CardContent>
                <Stack direction="row" spacing={1} alignItems="center" justifyContent="space-between">
                  <Typography variant="h6">今天 · {todayLabel}</Typography>
                  <Chip label={running ? '计时中' : '已暂停'} color={running ? 'secondary' : 'default'} />
                </Stack>
                <Box mt={2} display="flex" alignItems="center" gap={1}>
                  <AccessTimeIcon color="primary" />
                  <Typography variant="h5">{formatSeconds(liveSeconds)}</Typography>
                </Box>
                <Stack direction="row" spacing={1} mt={2}>
                  <Button variant="contained" onClick={handleStart} disabled={timerLoading} fullWidth>
                    开始
                  </Button>
                  <Button variant="outlined" onClick={handlePause} disabled={timerLoading} fullWidth>
                    暂停
                  </Button>
                </Stack>
                {timerLoading && (
                  <Typography variant="body2" color="text.secondary" mt={1}>
                    计时器更新中...
                  </Typography>
                )}
                {timerError && (
                  <Alert severity="error" sx={{ mt: 1 }}>
                    {timerError}
                  </Alert>
                )}
              </CardContent>
            </Card>
          </Stack>
        )}

        {page === 'plan' && (
          <Stack spacing={2}>
            <Card className="section-card">
              <CardContent>
                <Stack spacing={1.5}>
                  <Stack direction="row" alignItems="center" justifyContent="space-between">
                    <Typography variant="h6">当天计划</Typography>
                    <Stack direction="row" spacing={1} alignItems="center">
                      <TextField
                        type="date"
                        size="small"
                        value={selectedDate}
                        onChange={(event) => setSelectedDate(event.target.value)}
                      />
                      <IconButton color="primary" onClick={openPlanDialog}>
                        <AddIcon />
                      </IconButton>
                    </Stack>
                  </Stack>

                  {planLoading && (
                    <Typography variant="body2" color="text.secondary">
                      加载中...
                    </Typography>
                  )}
                  {planError && <Alert severity="error">{planError}</Alert>}
                  {!planLoading && !planError && plan && plan.dailyLogs.length === 0 && (
                    <Alert severity="info">当天暂无固定任务日志。</Alert>
                  )}

                  {plan && plan.dailyLogs.length > 0 && (
                    <Stack spacing={2}>
                      <Box>
                        <Typography variant="subtitle1">高能量</Typography>
                        {planHigh.length === 0 ? (
                          <Typography variant="body2" color="text.secondary">
                            暂无高能量计划
                          </Typography>
                        ) : (
                          <Stack spacing={1} divider={<Divider flexItem />} mt={1}>
                            {planHigh.map((log) => {
                              const completed = log.status === 'COMPLETED';
                              return (
                                <Box
                                  key={log.id}
                                  display="flex"
                                  justifyContent="space-between"
                                  alignItems="center"
                                  onClick={() => openEditPlanDialog(log)}
                                  sx={{ cursor: 'pointer' }}
                                >
                                  <Stack direction="row" spacing={1} alignItems="center">
                                    <Checkbox
                                      checked={completed}
                                      onChange={() => handleCompleteLog(log)}
                                      onClick={(event) => event.stopPropagation()}
                                      disabled={completed}
                                    />
                                    <Box>
                                      <Typography
                                        variant="subtitle1"
                                        sx={{ textDecoration: completed ? 'line-through' : 'none' }}
                                      >
                                        {log.template.title}
                                      </Typography>
                                      <Typography variant="body2" color="text.secondary">
                                        {completed ? '已完成' : '待完成'}
                                      </Typography>
                                    </Box>
                                  </Stack>
                                  <Stack direction="row" spacing={1} alignItems="center">
                                    <Chip label="高能量" color="secondary" size="small" />
                                    <IconButton
                                      onClick={(event) => {
                                        event.stopPropagation();
                                        openEditPlanDialog(log);
                                      }}
                                    >
                                      <EditIcon fontSize="small" />
                                    </IconButton>
                                  </Stack>
                                </Box>
                              );
                            })}
                          </Stack>
                        )}
                      </Box>

                      <Box>
                        <Typography variant="subtitle1">低能量</Typography>
                        {planLow.length === 0 ? (
                          <Typography variant="body2" color="text.secondary">
                            暂无低能量计划
                          </Typography>
                        ) : (
                          <Stack spacing={1} divider={<Divider flexItem />} mt={1}>
                            {planLow.map((log) => {
                              const completed = log.status === 'COMPLETED';
                              return (
                                <Box
                                  key={log.id}
                                  display="flex"
                                  justifyContent="space-between"
                                  alignItems="center"
                                  onClick={() => openEditPlanDialog(log)}
                                  sx={{ cursor: 'pointer' }}
                                >
                                  <Stack direction="row" spacing={1} alignItems="center">
                                    <Checkbox
                                      checked={completed}
                                      onChange={() => handleCompleteLog(log)}
                                      onClick={(event) => event.stopPropagation()}
                                      disabled={completed}
                                    />
                                    <Box>
                                      <Typography
                                        variant="subtitle1"
                                        sx={{ textDecoration: completed ? 'line-through' : 'none' }}
                                      >
                                        {log.template.title}
                                      </Typography>
                                      <Typography variant="body2" color="text.secondary">
                                        {completed ? '已完成' : '待完成'}
                                      </Typography>
                                    </Box>
                                  </Stack>
                                  <Stack direction="row" spacing={1} alignItems="center">
                                    <Chip label="低能量" size="small" />
                                    <IconButton
                                      onClick={(event) => {
                                        event.stopPropagation();
                                        openEditPlanDialog(log);
                                      }}
                                    >
                                      <EditIcon fontSize="small" />
                                    </IconButton>
                                  </Stack>
                                </Box>
                              );
                            })}
                          </Stack>
                        )}
                      </Box>
                    </Stack>
                  )}
                </Stack>
              </CardContent>
            </Card>

            <Card className="section-card">
              <CardContent>
                <Stack spacing={1.5}>
                  <Stack direction="row" alignItems="center" justifyContent="space-between">
                    <Stack direction="row" alignItems="center" spacing={1}>
                      <PlaylistAddCheckIcon color="primary" />
                      <Typography variant="h6">待办清单</Typography>
                    </Stack>
                    <IconButton color="primary" onClick={openTaskDialog}>
                      <AddIcon />
                    </IconButton>
                  </Stack>

                  {tasksLoading && (
                    <Typography variant="body2" color="text.secondary">
                      加载中...
                    </Typography>
                  )}
                  {tasksError && <Alert severity="error">{tasksError}</Alert>}
                  {!tasksLoading && !tasksError && tasks.length === 0 && (
                    <Alert severity="info">当前没有待办任务。</Alert>
                  )}

                  {tasks.length > 0 && (
                    <Stack spacing={2}>
                      <Box>
                        <Typography variant="subtitle1">高能量</Typography>
                        {tasksHigh.length === 0 ? (
                          <Typography variant="body2" color="text.secondary">
                            暂无高能量任务
                          </Typography>
                        ) : (
                          <Stack spacing={1} divider={<Divider flexItem />} mt={1}>
                            {tasksHigh.map((task) => (
                              <Box
                                key={task.id}
                                display="flex"
                                justifyContent="space-between"
                                alignItems="center"
                                onClick={() => openEditTaskDialog(task)}
                                sx={{ cursor: 'pointer' }}
                              >
                                <Stack direction="row" spacing={1} alignItems="center">
                                  <Checkbox
                                    checked={task.status === 'COMPLETED'}
                                    onChange={() => handleCompleteTask(task)}
                                    onClick={(event) => event.stopPropagation()}
                                    disabled={task.status === 'COMPLETED'}
                                  />
                                  <Box>
                                    <Typography
                                      variant="subtitle1"
                                      sx={{ textDecoration: task.status === 'COMPLETED' ? 'line-through' : 'none' }}
                                    >
                                      {task.title}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                      {task.taskDate}
                                    </Typography>
                                  </Box>
                                </Stack>
                                <Stack direction="row" spacing={1} alignItems="center">
                                  <Chip label="高能量" color="secondary" size="small" />
                                  <IconButton
                                    onClick={(event) => {
                                      event.stopPropagation();
                                      openEditTaskDialog(task);
                                    }}
                                  >
                                    <EditIcon fontSize="small" />
                                  </IconButton>
                                </Stack>
                              </Box>
                            ))}
                          </Stack>
                        )}
                      </Box>

                      <Box>
                        <Typography variant="subtitle1">低能量</Typography>
                        {tasksLow.length === 0 ? (
                          <Typography variant="body2" color="text.secondary">
                            暂无低能量任务
                          </Typography>
                        ) : (
                          <Stack spacing={1} divider={<Divider flexItem />} mt={1}>
                            {tasksLow.map((task) => (
                              <Box
                                key={task.id}
                                display="flex"
                                justifyContent="space-between"
                                alignItems="center"
                                onClick={() => openEditTaskDialog(task)}
                                sx={{ cursor: 'pointer' }}
                              >
                                <Stack direction="row" spacing={1} alignItems="center">
                                  <Checkbox
                                    checked={task.status === 'COMPLETED'}
                                    onChange={() => handleCompleteTask(task)}
                                    onClick={(event) => event.stopPropagation()}
                                    disabled={task.status === 'COMPLETED'}
                                  />
                                  <Box>
                                    <Typography
                                      variant="subtitle1"
                                      sx={{ textDecoration: task.status === 'COMPLETED' ? 'line-through' : 'none' }}
                                    >
                                      {task.title}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                      {task.taskDate}
                                    </Typography>
                                  </Box>
                                </Stack>
                                <Stack direction="row" spacing={1} alignItems="center">
                                  <Chip label="低能量" size="small" />
                                  <IconButton
                                    onClick={(event) => {
                                      event.stopPropagation();
                                      openEditTaskDialog(task);
                                    }}
                                  >
                                    <EditIcon fontSize="small" />
                                  </IconButton>
                                </Stack>
                              </Box>
                            ))}
                          </Stack>
                        )}
                      </Box>
                    </Stack>
                  )}
                </Stack>
              </CardContent>
            </Card>

            <Card className="section-card">
              <CardContent>
                <Stack spacing={1.5}>
                  <Typography variant="h6">基础设置</Typography>
                  <Stack direction="row" spacing={1} alignItems="center">
                    <TextField
                      label="User ID"
                      size="small"
                      type="number"
                      value={userId}
                      onChange={(event) => {
                        const value = Number(event.target.value);
                        setUserId(Number.isNaN(value) ? 1 : value);
                      }}
                      inputProps={{ min: 1 }}
                      fullWidth
                    />
                    <Button variant="contained" onClick={refreshAll}>
                      刷新
                    </Button>
                  </Stack>
                </Stack>
              </CardContent>
            </Card>
          </Stack>
        )}
      </Container>

      <Dialog open={planDialogOpen} onClose={() => setPlanDialogOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>添加当日计划</DialogTitle>
        <DialogContent>
          <Stack spacing={2} mt={1}>
            <TextField
              label="标题"
              value={planTitle}
              onChange={(event) => setPlanTitle(event.target.value)}
              required
              fullWidth
            />
            <TextField
              label="描述"
              value={planDescription}
              onChange={(event) => setPlanDescription(event.target.value)}
              fullWidth
              multiline
              minRows={2}
            />
            <FormControl fullWidth>
              <InputLabel id="plan-energy-label">能量等级</InputLabel>
              <Select
                labelId="plan-energy-label"
                label="能量等级"
                value={planEnergy}
                onChange={(event) => setPlanEnergy(event.target.value as EnergyLevel)}
              >
                <MenuItem value="HIGH">高能量</MenuItem>
                <MenuItem value="LOW">低能量</MenuItem>
              </Select>
            </FormControl>
            <FormControlLabel
              control={
                <Switch
                  checked={planActive}
                  onChange={(event) => setPlanActive(event.target.checked)}
                />
              }
              label="启用（每天重复）"
            />
            {planSubmitError && <Alert severity="error">{planSubmitError}</Alert>}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setPlanDialogOpen(false)} disabled={planSubmitLoading}>
            取消
          </Button>
          <Button variant="contained" onClick={handleCreatePlan} disabled={planSubmitLoading}>
            保存
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={taskDialogOpen} onClose={() => setTaskDialogOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>添加待办任务</DialogTitle>
        <DialogContent>
          <Stack spacing={2} mt={1}>
            <TextField
              label="标题"
              value={taskTitle}
              onChange={(event) => setTaskTitle(event.target.value)}
              required
              fullWidth
            />
            <TextField
              label="描述"
              value={taskDescription}
              onChange={(event) => setTaskDescription(event.target.value)}
              fullWidth
              multiline
              minRows={2}
            />
            <TextField
              label="日期"
              type="date"
              value={taskDate}
              onChange={(event) => setTaskDate(event.target.value)}
              fullWidth
              InputLabelProps={{ shrink: true }}
            />
            <FormControl fullWidth>
              <InputLabel id="task-energy-label">能量等级</InputLabel>
              <Select
                labelId="task-energy-label"
                label="能量等级"
                value={taskEnergy}
                onChange={(event) => setTaskEnergy(event.target.value as EnergyLevel)}
              >
                <MenuItem value="HIGH">高能量</MenuItem>
                <MenuItem value="LOW">低能量</MenuItem>
              </Select>
            </FormControl>
            {taskSubmitError && <Alert severity="error">{taskSubmitError}</Alert>}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setTaskDialogOpen(false)} disabled={taskSubmitLoading}>
            取消
          </Button>
          <Button variant="contained" onClick={handleCreateTask} disabled={taskSubmitLoading}>
            保存
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={Boolean(editPlan)} onClose={() => setEditPlan(null)} fullWidth maxWidth="sm">
        <DialogTitle>编辑当日计划</DialogTitle>
        <DialogContent>
          <Stack spacing={2} mt={1}>
            <TextField
              label="标题"
              value={editPlan?.title ?? ''}
              onChange={(event) =>
                setEditPlan((prev) => (prev ? { ...prev, title: event.target.value } : prev))
              }
              required
              fullWidth
            />
            <TextField
              label="描述"
              value={editPlan?.description ?? ''}
              onChange={(event) =>
                setEditPlan((prev) => (prev ? { ...prev, description: event.target.value } : prev))
              }
              fullWidth
              multiline
              minRows={2}
            />
            <FormControl fullWidth>
              <InputLabel id="edit-plan-energy-label">能量等级</InputLabel>
              <Select
                labelId="edit-plan-energy-label"
                label="能量等级"
                value={editPlan?.energyLevel ?? 'HIGH'}
                onChange={(event) =>
                  setEditPlan((prev) =>
                    prev ? { ...prev, energyLevel: event.target.value as EnergyLevel } : prev
                  )
                }
              >
                <MenuItem value="HIGH">高能量</MenuItem>
                <MenuItem value="LOW">低能量</MenuItem>
              </Select>
            </FormControl>
            <FormControlLabel
              control={
                <Switch
                  checked={editPlan?.active ?? true}
                  onChange={(event) =>
                    setEditPlan((prev) => (prev ? { ...prev, active: event.target.checked } : prev))
                  }
                />
              }
              label="启用（每天重复）"
            />
            {editPlanError && <Alert severity="error">{editPlanError}</Alert>}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setEditPlan(null)} disabled={editPlanLoading}>
            取消
          </Button>
          <Button variant="contained" onClick={handleUpdatePlan} disabled={editPlanLoading}>
            保存
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={Boolean(editTask)} onClose={() => setEditTask(null)} fullWidth maxWidth="sm">
        <DialogTitle>编辑待办任务</DialogTitle>
        <DialogContent>
          <Stack spacing={2} mt={1}>
            <TextField
              label="标题"
              value={editTask?.title ?? ''}
              onChange={(event) =>
                setEditTask((prev) => (prev ? { ...prev, title: event.target.value } : prev))
              }
              required
              fullWidth
            />
            <TextField
              label="描述"
              value={editTask?.description ?? ''}
              onChange={(event) =>
                setEditTask((prev) => (prev ? { ...prev, description: event.target.value } : prev))
              }
              fullWidth
              multiline
              minRows={2}
            />
            <TextField
              label="日期"
              type="date"
              value={editTask?.taskDate ?? ''}
              onChange={(event) =>
                setEditTask((prev) => (prev ? { ...prev, taskDate: event.target.value } : prev))
              }
              fullWidth
              InputLabelProps={{ shrink: true }}
            />
            <FormControl fullWidth>
              <InputLabel id="edit-task-energy-label">能量等级</InputLabel>
              <Select
                labelId="edit-task-energy-label"
                label="能量等级"
                value={editTask?.energyLevel ?? 'HIGH'}
                onChange={(event) =>
                  setEditTask((prev) =>
                    prev ? { ...prev, energyLevel: event.target.value as EnergyLevel } : prev
                  )
                }
              >
                <MenuItem value="HIGH">高能量</MenuItem>
                <MenuItem value="LOW">低能量</MenuItem>
              </Select>
            </FormControl>
            {editTaskError && <Alert severity="error">{editTaskError}</Alert>}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setEditTask(null)} disabled={editTaskLoading}>
            取消
          </Button>
          <Button color="error" onClick={openDeleteTaskConfirm} disabled={editTaskLoading}>
            删除
          </Button>
          <Button variant="contained" onClick={handleUpdateTask} disabled={editTaskLoading}>
            保存
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog
        open={deleteTaskConfirmOpen}
        onClose={() => setDeleteTaskConfirmOpen(false)}
        fullWidth
        maxWidth="xs"
      >
        <DialogTitle>确认删除</DialogTitle>
        <DialogContent>
          <Typography>确定要删除这个待办任务吗？此操作不可撤销。</Typography>
          {deleteTaskError && (
            <Alert severity="error" sx={{ mt: 2 }}>
              {deleteTaskError}
            </Alert>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteTaskConfirmOpen(false)} disabled={deleteTaskLoading}>
            取消
          </Button>
          <Button color="error" variant="contained" onClick={handleDeleteTask} disabled={deleteTaskLoading}>
            确认删除
          </Button>
        </DialogActions>
      </Dialog>

      <Box className="bottom-nav">
        <BottomNavigation value={page} onChange={(_, value) => setPage(value)} showLabels>
          <BottomNavigationAction label="计时" value="timer" icon={<AccessTimeIcon />} />
          <BottomNavigationAction label="计划" value="plan" icon={<ListAltIcon />} />
        </BottomNavigation>
      </Box>
    </Box>
  );
}
