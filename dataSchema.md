# Data Schema

The program stores all data within 2 data HashMaps. These HashMaps are described below.

## sensorLocationMap

This map stores all the buildings + areas/rooms as well as a list of their sensors.
The data within this map is manually configured (ref /src/main/resources/buildingSensorMap.csv) and is more or less configuration data for the program (the config file is parsed by the CSVSensorLocationFileImporter class).

Each object within the map is stored as a key value pair. The key is a combination of the building name + the room/area name (seperated by tilde) while the value is a List of Strings representing sensors within the specified buildings room/area:

McLaughlin~FirstFloor, [DS108, CS104, CS105, CS106]

To summarize:

sensorLocationMap
- BuildingNameAndRoomOrAreaKey (String, building name + ~ + room/area name)
- ListOfSensors (ArrayList of Strings)

Each BuildingNameAndRoomOrAreaKey corresponds to a building and a room/area.
Each sensor corresponds to a SensorID that belongs to a sensor within that building and room/area.

The data is used by the application in various analysis to ensure all sensors for a given location are properly checked.
The sensorDataMap corresponds to this data as its SensorID's are all specified within this data. 

## sensorDataMap

This map stores all the sensor reading data for all sensors.
The date within this map is the parsed data (parsed by the CSVSensorDataImporter class) from the python data generation script and is described below (ref /src/main/resources/sensorData for example files).

Each object within the map is stored as a key value pair. The key is the SensorID while the value
is a List of Dates representing readings from the sensor:

CS101, [Sun Feb 09 10:00:00 UTC 2020, Sun Feb 09 11:00:00 UTC 2020].

To summarize:

sensorDataMap
- SensorID (String)
- ListOfSensorReadings (ArrayList of Dates)

Each SensorID corresponds to a Sensor specified in the sensorLocationMap.
Each reading represents a timestamp at which the sensor sent a occupied response (aka the sensor detected movement).

This data is used by the application to determine if a room/area
is currently occupied (based on if a room/areas sensors have a reading for the current time).
And it is also used by the application to perform various utilization analysis.
This data is the core data for the application.
