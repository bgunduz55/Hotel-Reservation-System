# Hotel Reservation System - Frontend

Modern React-Redux frontend application for the Hotel Reservation System.

## Features

- **Authentication**: Login/Register with JWT
- **Hotel Management**: Browse hotels, view details, search
- **Reservation System**: Create, view, and manage reservations
- **Responsive Design**: Mobile-friendly interface
- **Real-time Updates**: Live data synchronization
- **Modern UI**: Clean and intuitive user interface

## Technology Stack

- **React 18**: Modern React with hooks
- **TypeScript**: Type-safe development
- **Redux Toolkit**: State management
- **React Router**: Client-side routing
- **Axios**: HTTP client for API calls
- **CSS3**: Modern styling with responsive design

## Project Structure

```
src/
├── components/          # Reusable components
│   └── Layout/         # Layout components
├── pages/              # Page components
│   ├── Auth/           # Authentication pages
│   ├── Dashboard/      # Dashboard page
│   ├── Hotels/         # Hotel-related pages
│   ├── Reservations/   # Reservation pages
│   └── Profile/        # User profile page
├── services/           # API services
├── store/              # Redux store
│   └── slices/         # Redux slices
└── types/              # TypeScript types
```

## Getting Started

### Prerequisites

- Node.js 16+ 
- npm or yarn
- Backend services running (see main README)

### Installation

1. Install dependencies:
```bash
cd hotel-reservation-frontend
npm install
```

2. Start development server:
```bash
npm start
```

3. Open [http://localhost:3000](http://localhost:3000) in your browser.

### Docker Development

```bash
# Build and run with Docker
docker-compose up frontend

# Or build the frontend container
docker build -t hotel-reservation-frontend .
docker run -p 3000:80 hotel-reservation-frontend
```

## Available Scripts

- `npm start` - Start development server
- `npm build` - Build for production
- `npm test` - Run tests
- `npm run lint` - Run ESLint
- `npm run lint:fix` - Fix ESLint issues

## API Integration

The frontend communicates with the backend through the API Gateway:

- **Base URL**: `http://localhost:8080`
- **Authentication**: JWT tokens
- **API Endpoints**: RESTful API design

### Key API Endpoints

- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/hotels` - Get all hotels
- `GET /api/hotels/{id}` - Get hotel details
- `GET /api/reservations` - Get user reservations
- `POST /api/reservations` - Create reservation

## State Management

Redux Toolkit is used for state management with the following slices:

- **authSlice**: Authentication state
- **hotelSlice**: Hotel data and operations
- **reservationSlice**: Reservation data and operations

## Routing

Protected routes require authentication:

- `/login` - Login page
- `/register` - Registration page
- `/dashboard` - Main dashboard
- `/hotels` - Hotel listing
- `/hotels/:id` - Hotel details
- `/reservations` - Reservation listing
- `/reservations/create` - Create reservation
- `/reservations/:id` - Reservation details
- `/profile` - User profile

## Styling

- **CSS Modules**: Component-scoped styles
- **Responsive Design**: Mobile-first approach
- **Modern UI**: Clean and intuitive interface
- **Accessibility**: WCAG compliant

## Development

### Code Style

- Follow TypeScript best practices
- Use functional components with hooks
- Implement proper error handling
- Write clean, readable code
- Add proper TypeScript types

### Testing

```bash
# Run tests
npm test

# Run tests with coverage
npm test -- --coverage
```

### Building for Production

```bash
# Build the application
npm run build

# The build folder contains the production files
```

## Docker Deployment

The frontend is containerized using a multi-stage Docker build:

1. **Build Stage**: Node.js environment to build the React app
2. **Production Stage**: Nginx to serve the static files

### Docker Commands

```bash
# Build the image
docker build -t hotel-reservation-frontend .

# Run the container
docker run -p 3000:80 hotel-reservation-frontend

# With Docker Compose
docker-compose up frontend
```

## Environment Variables

Create a `.env` file in the frontend directory:

```env
REACT_APP_API_URL=http://localhost:8080
REACT_APP_ENVIRONMENT=development
```

## Troubleshooting

### Common Issues

1. **API Connection Error**: Ensure backend services are running
2. **Authentication Issues**: Check JWT token in localStorage
3. **Build Errors**: Clear node_modules and reinstall dependencies

### Development Tips

- Use React Developer Tools for debugging
- Check Redux DevTools for state inspection
- Monitor network requests in browser dev tools
- Use TypeScript for better development experience

## Contributing

1. Follow the existing code style
2. Add proper TypeScript types
3. Write tests for new features
4. Update documentation as needed
5. Test on different screen sizes

## License

This project is part of the Hotel Reservation System. 