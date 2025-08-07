import { createSlice, createAsyncThunk, PayloadAction } from '@reduxjs/toolkit';
import { hotelApi, CreateHotelRequest } from '../../services/hotelApi';

export interface Hotel {
  id: number;
  name: string;
  address: string;
  city: string;
  country: string;
  rating: number;
  description: string;
  rooms: Room[];
}

export interface Room {
  id: number;
  hotelId: number;
  roomNumber: string;
  type: string;
  capacity: number;
  price: number;
  isAvailable: boolean;
}

export interface HotelState {
  hotels: Hotel[];
  selectedHotel: Hotel | null;
  loading: boolean;
  error: string | null;
}

const initialState: HotelState = {
  hotels: [],
  selectedHotel: null,
  loading: false,
  error: null,
};

export const fetchHotels = createAsyncThunk(
  'hotel/fetchHotels',
  async (_, { rejectWithValue }) => {
    try {
      const response = await hotelApi.getHotels();
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch hotels');
    }
  }
);

export const fetchHotelById = createAsyncThunk(
  'hotel/fetchHotelById',
  async (id: number, { rejectWithValue }) => {
    try {
      const response = await hotelApi.getHotelById(id);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to fetch hotel');
    }
  }
);

export const createHotel = createAsyncThunk(
  'hotel/createHotel',
  async (hotelData: CreateHotelRequest, { rejectWithValue }) => {
    try {
      const response = await hotelApi.createHotel(hotelData);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to create hotel');
    }
  }
);

export const updateHotel = createAsyncThunk(
  'hotel/updateHotel',
  async ({ id, hotelData }: { id: number; hotelData: Partial<CreateHotelRequest> }, { rejectWithValue }) => {
    try {
      const response = await hotelApi.updateHotel(id, hotelData);
      return response;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to update hotel');
    }
  }
);

export const deleteHotel = createAsyncThunk(
  'hotel/deleteHotel',
  async (id: number, { rejectWithValue }) => {
    try {
      await hotelApi.deleteHotel(id);
      return id;
    } catch (error: any) {
      return rejectWithValue(error.response?.data?.message || 'Failed to delete hotel');
    }
  }
);

const hotelSlice = createSlice({
  name: 'hotel',
  initialState,
  reducers: {
    clearError: (state) => {
      state.error = null;
    },
    setSelectedHotel: (state, action: PayloadAction<Hotel | null>) => {
      state.selectedHotel = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      // Fetch Hotels
      .addCase(fetchHotels.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchHotels.fulfilled, (state, action) => {
        state.loading = false;
        state.hotels = action.payload;
      })
      .addCase(fetchHotels.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Fetch Hotel by ID
      .addCase(fetchHotelById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchHotelById.fulfilled, (state, action) => {
        state.loading = false;
        state.selectedHotel = action.payload;
      })
      .addCase(fetchHotelById.rejected, (state, action) => {
        state.loading = false;
        state.error = action.payload as string;
      })
      // Create Hotel
      .addCase(createHotel.fulfilled, (state, action) => {
        state.hotels.push(action.payload);
      })
      // Update Hotel
      .addCase(updateHotel.fulfilled, (state, action) => {
        const index = state.hotels.findIndex(hotel => hotel.id === action.payload.id);
        if (index !== -1) {
          state.hotels[index] = action.payload;
        }
        if (state.selectedHotel?.id === action.payload.id) {
          state.selectedHotel = action.payload;
        }
      })
      // Delete Hotel
      .addCase(deleteHotel.fulfilled, (state, action) => {
        state.hotels = state.hotels.filter(hotel => hotel.id !== action.payload);
        if (state.selectedHotel?.id === action.payload) {
          state.selectedHotel = null;
        }
      });
  },
});

export const { clearError, setSelectedHotel } = hotelSlice.actions;
export default hotelSlice.reducer; 