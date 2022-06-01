#!/usr/bin/python3
import sys


def genreCount():
    inputFileName = sys.argv[1]
    outputFileName = sys.argv[2]

    f = open(inputFileName, 'r')
    fo = open(outputFileName, 'w')

    genreDict = {}
    data = []
    for line in f:
        line = line.strip('\n')
        data = line.split("::")

        if len(data) < 2:
            return

        genre = data[2]
        genreList = genre.split('|')


        for genre in genreList:
            if genre not in genreDict.keys():
                genreDict[genre] = 1
            else:
                genreDict[genre] += 1

    for k, v in genreDict.items():
        fo.write(k + " " + str(v) + "\n")

    f.close()
    fo.close()

if __name__ == "__main__":
    genreDict = genreCount()





