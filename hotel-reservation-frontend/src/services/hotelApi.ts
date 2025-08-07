import { apiService } from './api';
import { Hotel, Room } from '../store/slices/hotelSlice';

export interface CreateHotelRequest {
  name: string;
  address: string;
  city: string;
  country: string;
  rating: number;
  description: string;
}

export interface UpdateHotelRequest extends Partial<CreateHotelRequest> {}

export interface CreateRoomRequest {
  hotelId: number;
  roomNumber: string;
  type: string;
  capacity: number;
  price: number;
}

export interface UpdateRoomRequest extends Partial<CreateRoomRequest> {}

export const hotelApi = {
  // Hotel operations
  getHotels: async (): Promise<Hotel[]> => {
    return apiService.get<Hotel[]>('/api/hotels');
  },

  getHotelById: async (id: number): Promise<Hotel> => {
    return apiService.get<Hotel>(`/api/hotels/${id}`);
  },

  createHotel: async (hotelData: CreateHotelRequest): Promise<Hotel> => {
    return apiService.post<Hotel>('/api/hotels', hotelData);
  },

  updateHotel: async (id: number, hotelData: UpdateHotelRequest): Promise<Hotel> => {
    return apiService.put<Hotel>(`/api/hotels/${id}`, hotelData);
  },

  deleteHotel: async (id: number): Promise<void> => {
    return apiService.delete<void>(`/api/hotels/${id}`);
  },

  // Room operations
  getRoomsByHotelId: async (hotelId: number): Promise<Room[]> => {
    return apiService.get<Room[]>(`/api/hotels/${hotelId}/rooms`);
  },

  getRoomById: async (hotelId: number, roomId: number): Promise<Room> => {
    return apiService.get<Room>(`/api/hotels/${hotelId}/rooms/${roomId}`);
  },

  createRoom: async (hotelId: number, roomData: CreateRoomRequest): Promise<Room> => {
    return apiService.post<Room>(`/api/hotels/${hotelId}/rooms`, roomData);
  },

  updateRoom: async (hotelId: number, roomId: number, roomData: UpdateRoomRequest): Promise<Room> => {
    return apiService.put<Room>(`/api/hotels/${hotelId}/rooms/${roomId}`, roomData);
  },

  deleteRoom: async (hotelId: number, roomId: number): Promise<void> => {
    return apiService.delete<void>(`/api/hotels/${hotelId}/rooms/${roomId}`);
  },

  // Search and filter operations
  searchHotels: async (query: string): Promise<Hotel[]> => {
    return apiService.get<Hotel[]>(`/api/hotels/search?q=${encodeURIComponent(query)}`);
  },

  getHotelsByCity: async (city: string): Promise<Hotel[]> => {
    return apiService.get<Hotel[]>(`/api/hotels/city/${encodeURIComponent(city)}`);
  },

  getAvailableRooms: async (hotelId: number, checkInDate: string, checkOutDate: string): Promise<Room[]> => {
    return apiService.get<Room[]>(`/api/hotels/${hotelId}/rooms/available`, {
      params: { checkInDate, checkOutDate }
    });
  },
}; 