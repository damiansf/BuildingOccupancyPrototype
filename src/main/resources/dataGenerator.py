import csv
from datetime import date

print('Generating Data File...')

with open('./sensorData/DataFile' + "_" +str(date.today()) + '.csv', mode='w') as outputFile:

    outputWriter = csv.writer(outputFile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)

    with open('dataGeneratorConfig.csv') as csv_file:
        next(csv_file)
        csv_reader = csv.reader(csv_file, delimiter=',')
        for row in csv_reader:
            sensorID = row[0]
            times = row[1]

            splitTimes = times.split(' ')

            for timeSlot in splitTimes:
                startTime = timeSlot.split('-')[0]
                endTime = timeSlot.split('-')[1]
                #Handle DS sensors and CS sensors appropriately, generates timestamps in the form 00:00:00
                if 'DS' in sensorID:
                    outputWriter.writerow([sensorID,startTime.rjust(2,'0') + ":00:00"])
                    outputWriter.writerow([sensorID,endTime.rjust(2,'0') + ":00:00"])
                else:
                    hours = int(startTime)
                    minutes = 0
                    seconds = 0
                    outputWriter.writerow([sensorID, str(hours).rjust(2,'0') + ':' + str(minutes).rjust(2,'0') + ':' + str(seconds).rjust(2,'0')])
                    #Loop and generate entries for every 30 seconds in the specified time window
                    while True:
                        seconds += 30
                        if seconds == 60:
                            seconds = 0
                            minutes += 1
                        if minutes == 60:
                            minutes = 0
                            hours += 1
                        outputWriter.writerow([sensorID, str(hours).rjust(2,'0') + ':' + str(minutes).rjust(2,'0') + ':' + str(seconds).rjust(2,'0')])
                        if hours == int(endTime):
                            break





