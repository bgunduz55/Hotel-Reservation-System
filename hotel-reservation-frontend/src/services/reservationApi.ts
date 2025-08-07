import { apiService } from './api';
import { Reservation, CreateReservationRequest } from '../store/slices/reservationSlice';

export interface UpdateReservationRequest {
  checkInDate?: string;
  checkOutDate?: string;
  status?: 'PENDING' | 'CONFIRMED' | 'CANCELLED';
}

export interface ReservationFilter {
  status?: string;
  checkInDate?: string;
  checkOutDate?: string;
  hotelId?: number;
}

export const reservationApi = {
  // Reservation operations
  getReservations: async (filters?: ReservationFilter): Promise<Reservation[]> => {
    const params = new URLSearchParams();
    if (filters) {
      Object.entries(filters).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          params.append(key, value.toString());
        }
      });
    }
    
    const url = filters ? `/api/reservations?${params.toString()}` : '/api/reservations';
    return apiService.get<Reservation[]>(url);
  },

  getReservationById: async (id: number): Promise<Reservation> => {
    return apiService.get<Reservation>(`/api/reservations/${id}`);
  },

  createReservation: async (reservationData: CreateReservationRequest): Promise<Reservation> => {
    return apiService.post<Reservation>('/api/reservations', reservationData);
  },

  updateReservation: async (id: number, reservationData: UpdateReservationRequest): Promise<Reservation> => {
    return apiService.put<Reservation>(`/api/reservations/${id}`, reservationData);
  },

  cancelReservation: async (id: number): Promise<Reservation> => {
    return apiService.patch<Reservation>(`/api/reservations/${id}/cancel`);
  },

  deleteReservation: async (id: number): Promise<void> => {
    return apiService.delete<void>(`/api/reservations/${id}`);
  },

  // User reservations
  getUserReservations: async (): Promise<Reservation[]> => {
    return apiService.get<Reservation[]>('/api/reservations/user');
  },

  // Check availability
  checkAvailability: async (hotelId: number, roomId: number, checkInDate: string, checkOutDate: string): Promise<boolean> => {
    return apiService.get<boolean>(`/api/reservations/availability`, {
      params: { hotelId, roomId, checkInDate, checkOutDate }
    });
  },

  // Get reservation statistics
  getReservationStats: async (): Promise<{
    total: number;
    pending: number;
    confirmed: number;
    cancelled: number;
  }> => {
    return apiService.get<{
      total: number;
      pending: number;
      confirmed: number;
      cancelled: number;
    }>('/api/reservations/stats');
  },
}; 