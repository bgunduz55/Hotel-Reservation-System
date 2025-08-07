-- Hotel Reservation System Database Initialization
-- This script creates the necessary database schema for the hotel reservation system

-- Connect to the database
\c hotel_reservation;

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create hotels table
CREATE TABLE IF NOT EXISTS hotels (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(255),
    rating DECIMAL(2,1) CHECK (rating >= 0 AND rating <= 5),
    description TEXT,
    amenities TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    version INTEGER DEFAULT 0
);

-- Create rooms table
CREATE TABLE IF NOT EXISTS rooms (
    id BIGSERIAL PRIMARY KEY,
    hotel_id BIGINT NOT NULL REFERENCES hotels(id),
    room_number VARCHAR(20) NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    capacity INTEGER NOT NULL CHECK (capacity > 0),
    price_per_night DECIMAL(10,2) NOT NULL CHECK (price_per_night > 0),
    is_available BOOLEAN DEFAULT true,
    amenities TEXT[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    version INTEGER DEFAULT 0,
    UNIQUE(hotel_id, room_number)
);

-- Create reservations table
CREATE TABLE IF NOT EXISTS reservations (
    id BIGSERIAL PRIMARY KEY,
    hotel_id BIGINT NOT NULL REFERENCES hotels(id),
    room_id BIGINT NOT NULL REFERENCES rooms(id),
    guest_name VARCHAR(255) NOT NULL,
    guest_email VARCHAR(255) NOT NULL,
    guest_phone VARCHAR(20),
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    number_of_guests INTEGER NOT NULL CHECK (number_of_guests > 0),
    total_price DECIMAL(10,2) NOT NULL CHECK (total_price > 0),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED')),
    special_requests TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    version INTEGER DEFAULT 0,
    CONSTRAINT check_dates CHECK (check_out_date > check_in_date)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_hotels_city ON hotels(city);
CREATE INDEX IF NOT EXISTS idx_hotels_rating ON hotels(rating);
CREATE INDEX IF NOT EXISTS idx_rooms_hotel_id ON rooms(hotel_id);
CREATE INDEX IF NOT EXISTS idx_rooms_available ON rooms(is_available);
CREATE INDEX IF NOT EXISTS idx_reservations_hotel_id ON reservations(hotel_id);
CREATE INDEX IF NOT EXISTS idx_reservations_room_id ON reservations(room_id);
CREATE INDEX IF NOT EXISTS idx_reservations_dates ON reservations(check_in_date, check_out_date);
CREATE INDEX IF NOT EXISTS idx_reservations_status ON reservations(status);
CREATE INDEX IF NOT EXISTS idx_reservations_guest_email ON reservations(guest_email);

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_hotels_updated_at BEFORE UPDATE ON hotels
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_rooms_updated_at BEFORE UPDATE ON rooms
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_reservations_updated_at BEFORE UPDATE ON reservations
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert sample data
INSERT INTO hotels (name, address, city, country, phone, email, rating, description, amenities) VALUES
('Grand Hotel Istanbul', 'Taksim Square, Istanbul', 'Istanbul', 'Turkey', '+90-212-555-0101', 'info@grandhotelistanbul.com', 4.5, 'Luxury hotel in the heart of Istanbul', ARRAY['WiFi', 'Pool', 'Spa', 'Restaurant', 'Gym']),
('Seaside Resort Antalya', 'Konyaalti Beach, Antalya', 'Antalya', 'Turkey', '+90-242-555-0202', 'info@seasideresortantalya.com', 4.2, 'Beautiful resort on the Mediterranean coast', ARRAY['WiFi', 'Pool', 'Beach', 'Restaurant', 'Spa']),
('Business Hotel Ankara', 'Kizilay Square, Ankara', 'Ankara', 'Turkey', '+90-312-555-0303', 'info@businesshotelankara.com', 4.0, 'Modern business hotel in Ankara', ARRAY['WiFi', 'Conference Room', 'Restaurant', 'Gym']),
('Boutique Hotel Cappadocia', 'Goreme Valley, Cappadocia', 'Cappadocia', 'Turkey', '+90-384-555-0404', 'info@boutiquehotelcappadocia.com', 4.8, 'Unique boutique hotel in Cappadocia', ARRAY['WiFi', 'Hot Air Balloon', 'Restaurant', 'Cave Rooms']),
('City Hotel Izmir', 'Alsancak District, Izmir', 'Izmir', 'Turkey', '+90-232-555-0505', 'info@cityhotelizmir.com', 3.8, 'Comfortable city hotel in Izmir', ARRAY['WiFi', 'Restaurant', 'Bar']);

-- Insert sample rooms
INSERT INTO rooms (hotel_id, room_number, room_type, capacity, price_per_night, amenities) VALUES
(1, '101', 'Standard', 2, 150.00, ARRAY['WiFi', 'TV', 'AC']),
(1, '102', 'Standard', 2, 150.00, ARRAY['WiFi', 'TV', 'AC']),
(1, '201', 'Deluxe', 3, 250.00, ARRAY['WiFi', 'TV', 'AC', 'Balcony']),
(1, '202', 'Deluxe', 3, 250.00, ARRAY['WiFi', 'TV', 'AC', 'Balcony']),
(1, '301', 'Suite', 4, 400.00, ARRAY['WiFi', 'TV', 'AC', 'Balcony', 'Jacuzzi']),
(2, '101', 'Standard', 2, 120.00, ARRAY['WiFi', 'TV', 'AC']),
(2, '102', 'Standard', 2, 120.00, ARRAY['WiFi', 'TV', 'AC']),
(2, '201', 'Sea View', 3, 200.00, ARRAY['WiFi', 'TV', 'AC', 'Sea View']),
(3, '101', 'Business', 2, 180.00, ARRAY['WiFi', 'TV', 'AC', 'Work Desk']),
(3, '102', 'Business', 2, 180.00, ARRAY['WiFi', 'TV', 'AC', 'Work Desk']),
(4, '101', 'Cave Room', 2, 300.00, ARRAY['WiFi', 'TV', 'AC', 'Cave Experience']),
(4, '102', 'Cave Room', 2, 300.00, ARRAY['WiFi', 'TV', 'AC', 'Cave Experience']),
(5, '101', 'Standard', 2, 100.00, ARRAY['WiFi', 'TV', 'AC']),
(5, '102', 'Standard', 2, 100.00, ARRAY['WiFi', 'TV', 'AC']);

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE hotel_reservation TO hotel_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO hotel_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO hotel_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO hotel_user;

-- Grant future permissions
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO hotel_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO hotel_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO hotel_user; 