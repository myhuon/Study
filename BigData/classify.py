#!/usr/bin/python3
import sys
import operator
from os import listdir
import numpy as np

def createDataSet(dirName):
    trainingFileList = listdir(dirName)
    fileNum = len(trainingFileList)
    group = np.zeros((fileNum, 1024))

    labels = []
    for file in range(fileNum):
        fileName = trainingFileList[file]
        realNum = int(fileName.split('_')[0])

        labels.append(realNum)

        dirFileName = dirName + '/' + fileName
        group[file, :] = calcMatrix(dirFileName)

    return group, labels


def calcMatrix(filename):
    matrix = np.zeros((1, 1024))
    with open(filename) as f:
        for i in range(32):
            line = f.readline()

            for j in range(32):
                matrix[0, i * 32 + j] = int(line[j])

        return matrix


def classify0(inX, dataSet, labels, k):
    dataSetSize = dataSet.shape[0]
    diffMat = np.tile(inX, (dataSetSize, 1)) - dataSet
    sqDiffMat = diffMat ** 2
    sqDistances = sqDiffMat.sum(axis=1)
    distances = sqDistances ** 0.5
    sortedDistIndicies = distances.argsort()
    classCount = {}

    for i in range(k):
        voteIlabel = labels[sortedDistIndicies[i]]
        classCount[voteIlabel] = classCount.get(voteIlabel, 0) + 1
    sortedClassCount = sorted(classCount.items(), key=operator.itemgetter(1), reverse=True)

    return sortedClassCount[0][0]


if __name__=="__main__":
    trainingDirName = sys.argv[1]
    testDirName = sys.argv[2]

    testFileList = listdir(testDirName)
    length = len(testFileList)

    group, labels = createDataSet(trainingDirName)

    for k in range(1, 20, 2):
        cnt = 0; errorCnt = 0

        for i in range(length):
            result = int(testFileList[i].split('_')[0])
            testDirFileName = testDirName + '/' + testFileList[i]
            testData = calcMatrix(testDirFileName)
            classified0Result = classify0(testData, group, labels, k)

            cnt += 1
            if result != classified0Result:
                errorCnt += 1

        print(int(errorCnt / cnt * 100))