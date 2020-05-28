# Building The Project

To build the project simply run (from the projects top directory):

	gradle build

The projects gradle file specifies that it must be run with Java 11 (Same as Judi's ZUUL).

# Running The Project

To run the project simply run:

	gradle --console plain run

# Testing The Project

To test the project run:
	
	gradle test

To use the linter run:

	gradle lint

# CI/CD Build

You may initiate a CI/CD build on the CI/CD gitlab page for the repo and watch it run.

The CI/CD gitlab build for this project has 3 stages: lint, build and test.

The lint stage has artifacts containing the output from checkstyle, you can download and view these by downloading the lint artifacts.

The test stage has artifacts containg code coverage and test output, you can download and view these by downloading the test artifcats. 
For code coverage open up the file: reports/jacoco/test/html/index.html
For test results open up the file: reports/tests/test/index.html

# Data

The data for this project is mocked and is created by a data generation script.
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
The data gets outputted as a sensorID followed by the sensor's reading timestamp. The outputted data files are stored in the sensorData directory and the actualy program grabs the file from this directory for data files to import on startup.

To run the data generation script your self you can use python3:

    python dataGenerator.py (or python3 dataGenerator.py if you have python2 and python3 installed)

You must be within the resources folder to run the script.

Data can be imported into the running application through the admin menu, but the data file must be in the resources folder.

For the Data Schema, refer to the dataSchema.md file.

Since data is not imported live, all analysis performed through the program assumes the current day is 2020-02-08.

# Implemented User Stories

For implemented user stories from Milestone 1 refer to the Milestone1README.md file

## As a Student, I want to be able to check if a certain room is available on campus so me and my team may have our meeting within it

This user stories implementation can be found in the DataAnalyzer.java file from line 230 to line 304. Tests for it are in the DataAnalyzerTest.java file

### Acceptance Criteria

Given that I am a user who is running the program, when I type "oc" followed by a building and room/area, I am then informed as to if the room/area is occupied or not.

Given that I am a user who is running the program, when I type "oc" followed by a building and room/area combination that the application has no data on, I am informed that my search was invalid.

## As a School Administrator, I want to be able to import more sensor data into our system so that I may see more recent utilization data 

This user stories implementation can be found in the DataStore.java file from line 208 to line 227. Tests for it are in the DataStoreTest.java file

### Acceptance Criteria

Given that I am a admin who is logged in as a admin in the program, when I type "import" followed by a file name, I am then informed that the data from the provided file has been uploaded into the program.

Given that I am a admin who is logged in as a admin in the program, when I type "import" followed by a invalid file name, I am then informed that the file I provided was invalid.

Given that I am a admin who is logged in as a admin in the program, when I import new data succesfully into the program, I am then able to see analysis results on my newly uploaded data.

## As a School Administrator, I want to be able to check the utilization of rooms and floors of campus so that we may determine what rooms and floors of buildings are under utilizied

This user stories implementation can be found in the DataAnalyzer.java file from line 124 to line 228. Tests for it are in the DataAnalyzerTest.java file.

### Acceptance Criteria

Given that I am a admin who is logged in as a admin in the program, when I type "ut" followed by a building name, room/area and a number of days into the program, I am then given a utilization report for said building and room/area over the provided number of days.

Given that I am a admin who is logged in as a admin in the program, when I type "ut" followed by a building name, room/area and a number of days for a room/area with no data in the program, I am then informed that the room/area has no data within the program.

## As a School Administrator, I want to be able to see a utilization report for all buildings and rooms/areas on campus

This user stories implementation can be found in the DataAnalyzer.java file from line 46 to line 122. Tests for it are in the DataAnalyzerTest.java file. Changes were also made to the DataStore.java file from line 77 to line 112 and tests for these changes are in the DataStoreTest.java file

### Acceptance Criteria

Given that I am a admin who is logged in as a admin in the program, when I type "uta" followed by a number of days into the program, I am then able to see a total utilization analysis for all buildings, rooms and areas stored within the program.

# Implementation Analysis

For information on Open/Closed principle and Single Responsibility principle from Milestone 1, refer to the Milestone1README.md file.

## Use of Liskov Substitution Principle

The use of the Liskov Substitution Principle can be seen within the DataImporter Interface and its implementations: CSVSensorLocationFileImporter and CSVSensorDataImporter. The two implementations of this file both contain parseLine methods for parsing lines of files. This method was excluded from the DataImporter interface as it cannont be interchangeably implemented. Its method signature differes between the two implementations therefore it was excluded from the DataImporter interface to satisfy the requirements of the Liskov Substitution Principle. If the method was to be included in the Interface the Interface would not be replaceable with objects of its implementation therefore breaking the Principle.

## Use of Interface Segregation Principle

The use of this Principle can be seen throughout the programs 2 interfaces (DataImporter and TerminalInterface) and their implementations 
(CSVSensorLocationFileImporter and CSVSensorDataImporter for the DataImporter interface and AdminInterface and UserInterface for the TerminalInterface interface).
In these interfaces and their implementations, in no scenario do the clients, aka the implementations, depend on features of the interfaces that they do not use. All aspects of the implementations interfaces are used within the implementations and no extra code is hanging around for certain implementations that is not used by others.

# Testing Analysis

A major focus of this milestone was implementing unit tests. I intially started doing this by adding tests for all existing M1 functionality and then expanded upon it as I wrote new features for M2. In the end, I added 67 test cases that fully, and throughly test the program. Interfaces are not tested since there really is no need, rather their implementations are tested. Same goes for the Constants class as it just contains values.

In terms of coverage, the overall coverage for the application is 88% in terms of instructions and 82% in terms of branches.

Below is the coverage for specific classes and their reasoning (if the coverage is not 100%):

- UserInterface -> 59% Instructions, 66% branches. Given that this is a UI class, it was difficult to cover all aspects of it. The parts that are uncovered are parts that were better suited for manual testing and would require a bunch of code to test them. These parts include displaying the userInterface and interacting with it and multi input commands.
- AdminInterface -> 83% Instructions, 81% branches. Same reasoning as the UserInterface class for lack of coverage.
- DataAnalyzer -> 92% Instructions, 77% branches. Most uncovered code within this file is within catch statements for Date parsing errors that I was unable to test. Some cases are for branches of data analysis methods that depend on the System clock time so these were also missed in testing (technically they get tested but only if the tests are run at certain times)
- MainApplication -> 55% Instructions, N/A branches. The main method can't really be tested since it spins up the whole program and would require a bunch of code to test it. Also this would just end up retesting a large amount of the program. Theres also 2 catch statements that are not tested, they are for errors that are tested in other test classes.
- CSVSensorDataImporter -> 95% Instructions, 100% branches. Only unhandled case within this file is for a unlikely (if not impossible) scenario in which a datafile is in the wrong format. This was left untested as all the data fed into the program is created by a script that cannot produce invalid files. This error also causes the program to completly exit which doesnt swim well with tests.
- Utils -> 82% Instructions, 50% branches. Part of this class exits the entire program, the testing of this was avoided for obvious reasons.
- DataStore -> 100% Instructions, 100% branches. :)
- CSVSensorLocationFileImporter -> 100% Instructions, 100% branches. :)


# M1 Feedback

Interms of TA feedback the only feedback I received was to add more acceptance criteria and to create a interface for the UserInterface and AdminInterface classes. For this Milestone, I created a new TerminalInterface interface and made sure to have atleast 2 acceptance criteria per story, when possible.

The feedback I received from my peers mostly aligned with the TA feedback but also mentioned that MainApplication should be split up more, which I did for this milestone in order to have it truly follow SRP. The length of the class was shortned and code was extracted from it into other, more relevant, classes.

# To Be Implemented User Stories (Rough Draft, These Are WIP)

## User Stories for Student Role: 

- As a Student, I want to see what areas of campus are not heavily occupied so that I can find a place to study
- As a Student, I want to be able to view all campus buildings sorted by occupancy so I can know which places are least occupied

## User Stories for School Administrator Role: 

- As a School Administrator, I want to be able to check the utilization of certain areas within a building so that I may find spots that are under utilizied
- As a School Administrator, I want to be able to determine if the school requires additional study space based on the combined utilization reports of multiple campus buildings so that we may ensure that we have enough space for our students 
- As a School Administrator, I want to be able to see what areas of campus are currently not in use so that I may dispatch janitorial services
- As a School Administrator, I want to be able to export utilization reports to a file so that I may share them
- As a School Administrator, I want to be able to import JSON data files as well as CSV data files so that I may provide the system data from our web endpoint
- As a School Administrator, I want to be able to have multiple days worth of data loaded into the program at startup so I may immedietally see multiple days based usage reports

# References

- Professors notes and sample code, the gitlab build yaml and checkstyle config are heavily adapted from the professors examples in the ZUUL project

