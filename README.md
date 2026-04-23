
# Smart Campus API 

## 01. Project Overview

The Smart Campus API is a RESTful web service created to manage campus facilities.
It mainly focuses on Room Management and Sensor Monitoring.

This system allows:
    - Register rooms
    - Connect sensors to rooms
    - Track past sensor readings

### Key Architectural Features

#### Java EE 8 & Tomcat 9
The project is built as a Web Application using JAX-RS (Jersey) and is packaged as a WAR (Web Application Archive) for deployment on an Apache Tomcat 9 server.

#### Technical Configuration Details
    - Deployment Descriptor (web.xml): Handles the Jersey Servlet mapping.
    - Application Config (MyApplication.java): Extends JAX-RS Application to programmatically register Resource classes, Filters, and Exception Mappers.
    - Maven Build: Manages dependencies for Jersey containers and Jackson JSON providers.

#### In-Memory Persistence
The system stores data using thread-safe ConcurrentHashMap structures. This means it works without using an external database.

#### Sub-Resource Locator Pattern
This is used to manage related data in levels (for example: Sensors -> Readings).

#### Global Error Handling
A centralized GlobalExceptionMapper handles all runtime errors, returning clean JSON error messages and appropriate HTTP status codes (403, 404, 409, 422, 500) while preventing sensitive stack trace leaks.

#### Observability
Custom ContainerRequestFilter and ContainerResponseFilter implementations log the HTTP method, URI, and response status for every interaction in the Tomcat server logs.


### API Endpoints Overview

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/SmartCampusWeb/api/v1/` | Service Discovery & Metadata (HATEOAS) |
| `POST` | `/SmartCampusWeb/api/v1/rooms` | Register a new campus room |
| `GET` | `/SmartCampusWeb/api/v1/rooms` | List all registered rooms |
| `DELETE`| `/SmartCampusWeb/api/v1/rooms/{id}` | Decommission a room (Blocks if sensors present) |
| `POST` | `/SmartCampusWeb/api/v1/sensors` | Register a sensor (Validates Room ID existence) |
| `GET` | `/SmartCampusWeb/api/v1/sensors` | List all sensors (Supports `?type=` filtering) |
| `POST` | `/SmartCampusWeb/api/v1/sensors/{id}/readings` | Add historical data (Updates parent sensor) |
| `GET` | `/SmartCampusWeb/api/v1/sensors/{id}/readings` | Retrieve reading history for a sensor |



## 02. Setup and Launch Instructions

### Prerequisites
   - Java JDK 21 or higher (This project was developed using JDK 23)
   - Apache Maven (Already included in NetBeans)
   - Apache Tomcat 
   - NetBeans IDE (Recommended to run the project easily)

### Build Instructions

#### 1. Clone the Repository
Run the following commands:
```bash
git clone https://github.com/sewmin999/SmartCampusAPI.git
cd SmartCampusAPI
```
#### 2. Compile the Project
Open the SmartCampusWeb folder in NetBeans.
Then:
1. Right click the project root
2. Select Clean and Build. This generates the .war file in the target folder.
Alternatively, via terminal:
```bash
mvn clean install
```
#### 3. Launching the Server
1. Ensure Apache Tomcat 9 is configured in the NetBeans Services tab.
2. Right-click the project and select Run.
3. NetBeans will deploy the WAR file to Tomcat.
4. The API entry point is: http://localhost:8080/SmartCampusWeb/rest/api/v1/



## 03. Sample API Interactions (curl Commands)

### 1. Service Discovery (HATEOAS)
Retrieve the API entry point and available resources.
```bash    
curl -X GET http://localhost:8080/SmartCampusWeb/api/v1/
```

### 2. Create a New Room
Register a physical location on campus.
```bash    
curl -X POST http://localhost:8080/SmartCampusWeb/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\": \"ROOM-001\", \"name\": \"Computing Lab\", \"capacity\": 30}"
```
This will create:
  - Room ID -> ROOM-001
  - Name -> Computing Lab
  - Capacity -> 30

### 3. Register a Sensor to a Room
Link a temperature sensor to the newly created room.
```bash    
curl -X POST http://localhost:8080/SmartCampusWeb/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\": \"TEMP-01\", \"type\": \"Temperature\", \"status\": \"ACTIVE\", \"roomId\": \"ROOM-001\"}"
```
This will create:
  - Sensor ID -> TEMP-01
  - Type -> Temperature
  - Status -> ACTIVE
  - Linked Room -> ROOM-001

### 4. Post a Historical Reading (Sub-Resource)
Send a data point to a specific sensor. Note how this updates the parent sensor's value.
```bash   
curl -X POST http://localhost:8080/SmartCampusWeb/api/v1/sensors/TEMP-01/readings -H "Content-Type: application/json" -d "{\"id\": \"READ-101\", \"timestamp\": 1713620000, \"value\": 22.5}"
```
This will add:
  - Reading ID -> READ-101
  - Timestamp -> 1713620000
  - Value -> 22.5

### 5. Filter Sensors by Type
Retrieve only the temperature sensors using query parameters.
```bash    
curl -X GET "http://localhost:8080/SmartCampusWeb/api/v1/sensors?type=Temperature"
```


## 04. Expected JSON Response

When retrieving a specific room, the API returns the metadata along with the IDs of sensors currently assigned to it:
```json
{
    "id": "ROOM-001",
    "name": "Computing Lab",
    "capacity": 30,
    "sensorIds": ["TEMP-01"]
}
```



## 05. Error Handling Examples

The API is designed to be "leak proof".
1. 409 Conflict: Attempting to delete a room that still has sensors.
2. 422 Unprocessable Entity: Registering a sensor to a non existent Room ID.
3. 403 Forbidden: Attempting to post readings to a sensor in MAINTENANCE mode.
4. 500 Internal Server Error: A global "Safety Net" catches any unexpected runtime errors to prevent the exposure of internal Java stack traces.