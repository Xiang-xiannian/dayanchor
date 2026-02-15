const API_BASE = '/api';

export type ApiError = {
  status: number;
  message: string;
};

type RequestOptions = {
  method?: 'GET' | 'POST' | 'PATCH' | 'PUT' | 'DELETE';
  body?: unknown;
  query?: Record<string, string | number | boolean | undefined | null>;
};

const buildQuery = (query?: RequestOptions['query']) => {
  if (!query) return '';
  const params = new URLSearchParams();
  Object.entries(query).forEach(([key, value]) => {
    if (value === undefined || value === null) return;
    params.append(key, String(value));
  });
  const qs = params.toString();
  return qs ? `?${qs}` : '';
};

const getToken = () => localStorage.getItem('token');

const maybeStoreToken = (data: unknown) => {
  if (!data || typeof data !== 'object') return;
  const record = data as Record<string, unknown>;
  // TODO: confirm backend token field name when login is implemented.
  const token = record.token;
  if (typeof token === 'string' && token.length > 0) {
    localStorage.setItem('token', token);
  }
};

export async function apiRequest<T>(path: string, options: RequestOptions = {}): Promise<T> {
  const { method = 'GET', body, query } = options;
  const url = `${API_BASE}${path}${buildQuery(query)}`;
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
  };

  const token = getToken();
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(url, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });

  const contentType = response.headers.get('content-type') || '';
  const isJson = contentType.includes('application/json');
  const payload = isJson ? await response.json() : await response.text();

  if (!response.ok) {
    const message =
      isJson && payload && typeof payload.message === 'string'
        ? payload.message
        : response.statusText || 'Request failed';
    throw { status: response.status, message } as ApiError;
  }

  if (isJson) {
    maybeStoreToken(payload);
  }

  return payload as T;
}
