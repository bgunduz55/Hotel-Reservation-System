import { apiService } from './api';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  token_type: string;
  expires_in: number;
  username: string;
  roles: string[];
  issued_at: string;
  expires_at: string;
}

export const authApi = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    return apiService.post<AuthResponse>('/api/auth/login', credentials);
  },

  register: async (userData: RegisterRequest): Promise<AuthResponse> => {
    return apiService.post<AuthResponse>('/api/auth/register', userData);
  },

  logout: async (): Promise<void> => {
    return apiService.post<void>('/api/auth/logout');
  },

  getCurrentUser: async (): Promise<{ username: string; roles: string[] }> => {
    return apiService.get<{ username: string; roles: string[] }>('/api/auth/me');
  },

  refreshToken: async (): Promise<{ token: string }> => {
    return apiService.post<{ token: string }>('/api/auth/refresh');
  },
}; 