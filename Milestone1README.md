# Building The Project

To build the project simply run (from the projects top directory):

	gradle build

The projects gradle file specifies that it must be run with Java 11 (Same as Judi's ZUUL).

# Running The Project

To run the project simply run:

	java -jar build/libs/buildingOccupationAnalyzer.jar

# Testing The Project

Tests will be implemented in milestone 2. For now the folder structure includes a test directory with placeholder files.

# CI/CD Build

You may initiate a CI/CD build on the CI/CD gitlab page for the repo and watch it run.

The CI/CD gitlab build for this project produces a artifacts folder that contains the projects entire build folder.

You can download this folder and navigate to /reports/checkstyle to view the checkstyle report. In the future, the folder
will also contain the JUnit tests report.

# Data

The data for this project is mocked and is created by a data generation prototype script.
All data files as well as the script can be found in src/main/resources.

The buildingSensorMap.csv file is a data file that the actual program takes in inorder to know which sensors
belong to which buildings. In a real application this would be retireved from a server.

The dataGeneratorConfig.csv file is a config file meant for the actual data generation script. This file details sensors (by id)
as well as occupation times. For this project, a simple scenario for door sensors (Sensors with ID's starting in DS)
has been assumed in which a door sensor is only triggered once at the start of a rooms occupation and once at the end.
This is not a real life emulation of a door sensor however, the only data a program really cares about for a door sensor is the first
reading and the last, which is why this approach was taken (We only care about when a room became occupied and when it became unoccupied). For ceiling sensors (Sensors with ID's starting in CS) 
readings are generated for every 30 seconds within the specified time window. For example, a window of 1-3 would generate the following:

    In the form HH:MM:SS, 01:00:00 01:00:30 01:01:00...etc

The decision to create readings for every 30 seconds was made in order to get enough data points without flooding the system. Also, 
in the real world, sensors generally have a 1-2 minute window in which they must respond before a room is considered unoccupied
and lights in said room are turned off, so a 30 second window should mimic real life data in that a real system fed this data
would assume the room is occupied with readings every 30 seconds.

The output file that is generated has the current day attached to it's name as the data generated is assumed to be for a entire day.
The data gets outputted as a sensorID followed by the sensor's reading timestamp. The outputted data files are stored in the sensorData directory and the actualy program grabs the file from this directory for data files to import on startup. In the future the program will scan this directory for multiple files.

To run the data generation script your self you can use python3:

    python dataGenerator.py (or python3 dataGenerator.py if you have python2 and python3 installed)

You must be within the resources folder to run the script.

# Implemented User Stories

## As a School Administrator, I want to be able to login to the application as a admin so that I may complete administrative tasks

This user stories code can be found in the MainApplication.java file from line 82 to line 93 and from line 118 to line 155

### Acceptance Criteria

Given that I am a administrator who is running the program, When I type 'l' and enter my administrator password (password123), Then I am logged in and am informed that I am now able to perform administrative tasks.

## As a Student, I want to be able to see a list of all buildings so that I may be informed of buildings I can search up in the program

This user stories code can be found in the DataAnalyzer.java from line 63 to line 77

### Acceptance Criteria

Given that I am a user who is running the program, When I type 'db' into the console, I am then shown all buildings currently stored in the system

## As a Student, I want to be able to see a list of all rooms/areas on campus so that I may be informed of areas I can search for within buildings

This user stories code can be found in DataAnalyzer.java from line 83 to line 118

### Acceptance Criteria

Given that I am a user who is running the program, When I type 'da' into the console, I am then shown all rooms/areas and their associated buildings

## As a School Administrator, I want to be able to view all current sensors so that I can verify all sensors are present within the programs database

This user stories code can be found in DataAnalyzer.java from line 124 to line 145

### Acceptance Criteria

Given that I am a admin who is logged in as a admin in the program, When I type 'ls' into the admin console, I am then shown all sensors stored within the program

## As a School Administrator, I want to be able to remove a certain sensors data from our system as we believe it is faulty and don't want it's data to polute our results

This user stories code can be found in DataAnalyzer.java from line 152 to 169

### Acceptance Criteria

Given that I am a admin who is logged in as a admin in the program, When I type 'del' into the admin console followed by a sensor name (such as DS107), I am then given feedback stating that the specified sensor has been deleted and if I type 'ls' to see all sensors the sensor no longer appears

Given that I am a admin who is logged in as a admin in the program, When I type 'del' into the admin console followed by a invalid sensor name (such as DAMIAN), I am then given feedback stating that the specified sensor does not exist

# Implementation Analysis

## Use of Open/Closed Principle

The main application of the Open/Closed principle in this code base is the DataImporter interface (src/main/java/buildingoccupationanalyzer/DataImporter.java).
This interface is a generic interface for importing various data types and exporting them into various objects.
The interface actually defines the object as generic, the implementations of the interface define the specific objects they use.
CSVSensorLocationFileImporter and CSVSensorDataImporter (located in the same directory as the interface) both implement this interface
and its generic definition and they both expand upon it for their specific implementations. In the future various other file types
may be required and more classes that implement the generic interface can be easily defined. The interface is open for extension, fully,
and is fully closed off for modification.

## Use of Single Responsibility Principle

The Single Responsibility Principle is used in various places throughout the code base.
We will examine a two implementations of this principle.
The example we will examine will be a method within the MainApplication file (src/main/java/buildingoccupationanalyzer/MainApplication.java)
This file contains 4 methods:

- initializeSensorData
- initializeSensorLocationMap
- userInterface
- adminInterface

We will discuss the initializeSensorData method. This method has a single resposibility of initializing a map of sensor data. The method takes in no parameters and returns a new sensor data map, all it does is deal with initialization.
For actual import and creation of data it lets the data importing classes handle the job and just deals with the return.

Another larger example is within the Utils file (src/main/java/buildingoccupationanalyzer/Utils.java)
In this file there is currently only 1 method, this method deals with handling exceptions interms of informing the user and program.
The method is used in various parts of the program in catch statements and simply prints out exception details to inform the user,
it also informs the program if it should exit or not (in which case it also informs the user the error was fatal). The method allows
other methods to have a single responsibility and has a single responsibility it self, to just deal with exceptions. A call to this method
can be viewed in the MainApplication file on line 41.

# To Be Implemented User Stories (Rough Draft, These Are WIP)

## User Stories for Student Role: 

- As a Student, I want to be able to check if a certain room is available on campus so me and my team may have our meeting within it
- As a Student, I want to see what areas of campus are not heavily occupied so that I can find a place to study
- As a Student, I want to be able to view all campus buildings sorted by occupancy so I can know which places are least occupied

## User Stories for School Administrator Role: 

- As a School Administrator, I want to be able to check the utilization of rooms and floors of campus so that we may determine what rooms and floors of buildings are under utilizied
- As a School Administrator, I want to be able to check the utilization of certain areas within a building so that I may find spots that are under utilizied
- As a School Administrator, I want to be able to import more sensor data into our system so that I may see more recent utilization data 
- As a School Administrator, I want to be able to determine if the school requires additional study space based on the combined utilization reports of multiple campus buildings so that we may ensure that we have enough space for our students 
- As a School Administrator, I want to be able to see what areas of campus are currently not in use so that I may dispatch janitorial services
- As a School Administrator, I want to be able to export utilization reports to a file so that I may share them
- As a School Administrator, I want to be able to import JSON data files as well as CSV data files so that I may provide the system data from our web endpoint
- As a School Administrator, I want to be able to have multiple days worth of data loaded into the program at startup so I may immedietally see multiple days based usage reports

# References

- Professors notes and sample code, the gitlab build yaml and checkstyle config are heavily adapted from the professors examples in the ZUUL project

