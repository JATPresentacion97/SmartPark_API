# SmartPark REST API

SmartPark is an intelligent parking management system designed to optimize urban parking lot usage and facilitate easy vehicle navigation.

## Features

- Register parking lots
- Register vehicles
- Check-in vehicles to parking lots
- Check-out vehicles from parking lots
- View parking lot occupancy and availability
- List all vehicles currently parked in a lot

## Technologies Used

- Java 11+
- Spring Boot
- Spring Data JPA
- H2 In-Memory Database
- Gradle Build Tool
- JUnit 5 and Mockito for testing
- MockMvc for controller tests

## Getting Started

### Prerequisites

- Java 11 or higher installed
- Gradle installed 

### Build and Run

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/smartpark-api.git
   cd smartpark-api

2. Build the project:
   ```bash
   ./gradlew build

3. Run the application:
   ```bash
   ./gradlew bootRun

The application will start on http://localhost:8080.

## API Endpoints

- **POST /api/parkinglots** — Register a new parking lot  
- **POST /api/vehicles** — Register a new vehicle  
- **POST /api/parkinglots/{lotId}/checkin?licensePlate={licensePlate}** — Check in a vehicle to a parking lot  
- **POST /api/vehicles/{licensePlate}/checkout** — Check out a vehicle from its parking lot  
- **GET  /api/parkinglots/{lotId}** — Get parking lot status (capacity, occupied spaces)  
- **GET  /api/parkinglots/{lotId}/vehicles** — List vehicles currently parked in the lot  

## Sample JSON for Requests

### Register Parking Lot

```json
{
  "lotId": "LOT1",
  "location": "Downtown",
  "capacity": 50
}
```
### Register Parking Lot

```json
{
  "licensePlate": "ABC-123",
  "type": "CAR",
  "ownerName": "John Doe"
}
```
### Testing
Run tests using:

```bash
./gradlew test
```
Tests include unit tests for service layer and controller tests using MockMvc.

## Database

- Uses in-memory H2 database
- Schema and sample data are automatically loaded on startup (if implemented)

## Notes

- Postman Collection is already in REPO you can clone it directly in your postman
- API uses JSON for all input and output
- No authentication required
- Ensure vehicles cannot be parked in multiple lots simultaneously
- Prevent parking in full lots
