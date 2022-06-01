#!/usr/bin/python3
import datetime
import sys

def uberDate():
    inputFileName = sys.argv[1]
    f = open(inputFileName, 'r')

    dictDate = {0:'MON', 1:'TUE', 2:'WED', 3:'THR', 4:'FRI', 5:'SAT', 6:'SUN'}
    dictVehicles = {}
    for line in f:
        line = line.strip('\n')
        data = line.split(",")

        if len(data) < 2:
            return

        region = data[0]
        date = data[1]
        vehicles = data[2]
        trips = data[3]

        dt = datetime.datetime.strptime(date, "%m/%d/%Y")
        day = dictDate[dt.weekday()]

        li = [region, day]
        key = tuple(li)
        if key not in dictVehicles.keys():
            dictVehicles[key] = [vehicles, trips]
        else:
            dictVehicles[key][0] = int(dictVehicles[key][0]) + int(vehicles)
            dictVehicles[key][1] = int(dictVehicles[key][1]) + int(trips)

    f.close()
    return dictVehicles

if __name__ == "__main__":
    dictAll = uberDate()

    outputFileName = sys.argv[2]
    fo = open(outputFileName, 'w')

    for k, v in dictAll.items():
        fo.write(k[0] + "," + k[1] + " " + str(v[0]) + "," + str(v[1]) + "\n")

    fo.close()



