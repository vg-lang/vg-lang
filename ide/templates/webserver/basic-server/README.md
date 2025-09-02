# Web Server Application

This is a VG Language web server application template for building web applications and APIs.

## Getting Started

1. Open `server.vg` in the VG Language IDE
2. Run the server application
3. Open your browser to `http://localhost:8080`

## Files Structure

- `server.vg` - Main server setup and initialization
- `routes.vg` - Route definitions and request handlers
- `static/index.html` - Web interface and API testing page
- `README.md` - This documentation file

## Features

- HTTP server with routing
- Static file serving
- JSON API endpoints
- Request/response handling
- 404 error handling

## API Endpoints

- **GET /** - Serves the main HTML page
- **GET /api/hello** - Returns a JSON greeting
- **POST /api/echo** - Echoes back the request body
- **GET /static/** - Serves static files

## Next Steps

- Add database connectivity
- Implement authentication
- Create additional API endpoints
- Add middleware for logging/security
- Deploy to a production server
