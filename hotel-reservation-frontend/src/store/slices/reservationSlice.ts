import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { reservationApi, UpdateReservationRequest } from '../../services/reservationApi';

export interface Reservation {
  id: number;
  userId: number;
  hotelId: number;
  roomId: number;
  checkInDate: string;
  checkOutDate: string;
  totalPrice: number;
  status: 'PENDING' | 'CONFIRMED' | 'CANCELLED';
  createdAt: string;
  updatedAt: string;
  hotel?: {
    id: number;
    name: string;
    address: string;
  };
  room?: {
    id: number;
    roomNumber: string;
    type: string;
    price: number;
  };
}

export interface CreateReservationRequest {
  hotelId: number;
  roomId: number;
  checkInDate: string;
  checkOutDate: string;
}

export interface ReservationState {
  reservations: Reservation[];
  selectedReservation: Reservation | null;
  loading: boolean;
  error: string | null;
}

const initialState: ReservationState = {
  reservations: [],
  selectedReservation: null,
  loading: false,
  error: null,
};

export const fetchReservations = createAsyncThunk(
  'reservation/fetchReservations',
  async (_, { rejectWithValue }) => {
    try {
      const response = await reservationApi.getReservations();
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch reservations');
    }
  }
);

export const fetchReservationById = createAsyncThunk(
  'reservation/fetchReservationById',
  async (id: number, { rejectWithValue }) => {
    try {
      const response = await reservationApi.getReservationById(id);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch reservation');
    }
  }
);

export const createReservation = createAsyncThunk(
  'reservation/createReservation',
  async (reservationData: CreateReservationRequest, { rejectWithValue }) => {
    try {
      const response = await reservationApi.createReservation(reservationData);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to create reservation');
    }
  }
);

export const updateReservation = createAsyncThunk(
  'reservation/updateReservation',
  async ({ id, reservationData }: { id: number; reservationData: UpdateReservationRequest }, { rejectWithValue }) => {
    try {
      const response = await reservationApi.updateReservation(id, reservationData);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to update reservation');
    }
  }
);

export const cancelReservation = createAsyncThunk(
  'reservation/cancelReservation',
  async (id: number, { rejectWithValue }) => {
    try {
      const response = await reservationApi.cancelReservation(id);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to cancel reservation');
    }
  }
);

export const deleteReservation = createAsyncThunk(
  'reservation/deleteReservation',
  async (id: number, { rejectWithValue }) => {
    try {
      await reservationApi.deleteReservation(id);
      return id;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to delete reservation');
    }
  }
);

const reservationSlice = createSlice({
  name: 'reservation',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    setSelectedReservation: (state, action: PayloadAction<Reservation | null>) => {
      state.selectedReservation = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch Reservations
      .addCase(fetchReservations.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchReservations.fulfilled, (state, action) => {
        state.loading = false;
        state.reservations = action.payload;
      })
      .addCase(fetchReservations.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Fetch Reservation by ID
      .addCase(fetchReservationById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchReservationById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedReservation = action.payload;
      })
      .addCase(fetchReservationById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Create Reservation
      .addCase(createReservation.fulfilled, (state, action) => {
        state.reservations.push(action.payload);
      })
      // Update Reservation
      .addCase(updateReservation.fulfilled, (state, action) => {
        const index = state.reservations.findIndex(reservation => reservation.id === action.payload.id);
        if (index !== -1) {
          state.reservations[index] = action.payload;
        }
        if (state.selectedReservation?.id === action.payload.id) {
          state.selectedReservation = action.payload;
        }
      })
      // Cancel Reservation
      .addCase(cancelReservation.fulfilled, (state, action) => {
        const index = state.reservations.findIndex(reservation => reservation.id === action.payload.id);
        if (index !== -1) {
          state.reservations[index] = action.payload;
        }
        if (state.selectedReservation?.id === action.payload.id) {
          state.selectedReservation = action.payload;
        }
      })
      // Delete Reservation
      .addCase(deleteReservation.fulfilled, (state, action) => {
        state.reservations = state.reservations.filter(reservation => reservation.id !== action.payload);
        if (state.selectedReservation?.id === action.payload) {
          state.selectedReservation = null;
        }
      });
  },
});

export const { clearError, setSelectedReservation } = reservationSlice.actions;
export default reservationSlice.reducer; 