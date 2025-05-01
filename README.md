# Java Weather API

> Java-based weather API that fetches and returns weather data from a third party API.

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Setup](#setup)
* [Usage](#usage)
* [Project Status](#project-status)
* [Acknowledgements](#acknowledgements)

## General Information
Java Weather APi is a weather API that fetches and returns weather data from [Visual Crossing's API](https://www.visualcrossing.com/weather-api). It uses [Redis](https://redis.io) for in-memory caching and [Jedis](https://github.com/redis/jedis) as the client. This project is designed to explore and practice working with the third party APIs, caching and environment variables in Java.

## Technologies Used
- Java 21.0.6 LTS
- Redis 7.4.3

## Features
- **Real-Time Weather Data**: Fetch up-to-date weather information for any location.
- **Location-Based Queries**: Retrieve weather details by specifying the desired location in the query parameter.
- **Caching for Faster Responses**: Utilizes Redis caching to store weather data temporarily.
- **Rate Limiting**: Implements rate limiting to prevent excessive API calls.
- **JSON Responses**: Returns structured JSON data, making it easy to integrate with front-end applications or other systems.
- **Customizable**: Easily configurable API key and Redis connection via environment variables.

## Setup

To run this API, you’ll need:

* **Java**: Version 21 or higher
* **Maven**: Version 3.x
* **Redis**: Version 7.4 or higher


How to install:

1. Clone the repository

   ```bash
   git clone https://github.com/krisnaajiep/java-weather-api.git
   ```

2. Change the current working directory

   ```bash
   cd java-weather-api
   ```

3. Build the project

   ```bash
   mvn clean package
   ```

4. Copy the JAR file from the target/ directory

   ```bash
   cp target/java-weather-api-1.0-SNAPSHOT.war weather-api.war 
   ```

5. Set environment variables

   ```bash
   # Visual Crossing API Key
   export API_KEY=[your_visualcrossing_api_key]

   # Redis connection settings
   export REDIS_HOST=localhost
   export REDIS_PORT=6379
   export REDIS_USERNAME=default         # optional
   export REDIS_PASSWORD=[your_password]   # optional
   export REDIS_DATABASE=0               # optional
   ```
   
6. Run the JAR file

   ```bash
   java -jar weather-api.jar
   ```

## Usage

Access the endpoint with the location query parameter.

```
http://localhost:8080?location=[location]
```


## Project Status
Project is: _complete_.

## Acknowledgements
This project was inspired by [roadmap.sh](https://roadmap.sh/projects/weather-api-wrapper-service).