import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';

class ApiService {
  private api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: 'http://localhost:8080',
      timeout: 10000,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Request interceptor for adding auth token
    this.api.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Response interceptor for handling errors
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('token');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  public get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.api.get(url, config).then(response => response.data);
  }

  public post<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    const fullUrl = this.api.defaults.baseURL + url;
    console.log('API POST request:', { 
      url, 
      fullUrl, 
      baseURL: this.api.defaults.baseURL,
      data 
    });
    return this.api.post(url, data, config).then(response => {
      console.log('API POST response:', response.data);
      return response.data;
    }).catch(error => {
      console.error('API POST error:', error);
      throw error;
    });
  }

  public put<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.api.put(url, data, config).then(response => response.data);
  }

  public delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.api.delete(url, config).then(response => response.data);
  }

  public patch<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.api.patch(url, data, config).then(response => response.data);
  }
}

export const apiService = new ApiService(); 