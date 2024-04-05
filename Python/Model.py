import numpy as np
from scipy.optimize import curve_fit #might be wrong curve fit / is never used
from Node import Node

class Model: #Handles all functions with lists of positions
    @staticmethod #appropriate for utility methods that don't need to access or modify state of a class instance
    def checkForChange(nList): 
        if len(nList) < 2: #Too few nodes to use in prediction
            return 1
        curPos = nList[-1]
        prevPos = nList[-2]
        if len(nList) < 3: #Not enough with nodes to check angle
            if curPos.v - prevPos.v >= 0.5:
                return 1 #We are accelerating, we are going straight
            elif curPos.v - prevPos.v <= 0.5:
                return 2 #We are de-accelerating, we might turn
            else:
                return 1 #No significant change, we keep going straight
            
        prevPrevPos = nList[-3]
        if curPos.v - prevPos.v >= 0.5 or curPos.angleBetween(prevPos, prevPrevPos) <= 4:
            return 1 #We are accelerating, keep going straight
        elif curPos.v - prevPos.v <= -0.5 or curPos.angleBetween(prevPos, prevPrevPos) > 4:
            if curPos.angleBetween(prevPos, prevPrevPos) > 60:
                return 3
            return 2 #We are de-acclerating, we might turn
        else:
            return 1 #No significant change, we keep going straight.
    
    @staticmethod
    def predictNextPosition(n, t, noDataPoints):
        """
        Input must be a list of all nodes up until the current position,
        what time we simulate with between each timestep,
        and how many datapoints we look at. 
        """
        newPos = np.zeros(2)
        if len(n) < noDataPoints:
            newPos[0] = n[-1].x
            newPos[1] = n[-1].y
            return newPos
        change = Model.checkForChange(n)
        curPos = n[-1]
        prevPos = n[-noDataPoints]
        if change == 1: #We are acclearing or have constant velocity
            newPos[0] = curPos.x + prevPos.getVx(curPos) * t
            newPos[1] = curPos.y + prevPos.getVy(curPos) * t
        elif change == -1: #Do something else with angle...
            newPos[0] = curPos.x + prevPos.getVx(curPos) * t
            newPos[1] = curPos.y + prevPos.getVy(curPos) * t
        return newPos

    @staticmethod
    def predictionPolynomialRegression(n, degree, simTime, useAngleCheck):
        obsX = np.zeros((degree + 1, 2))
        obsY = np.zeros((degree + 1, 2))

        if useAngleCheck == 1:
            degree = Model.checkForChange(n)
        if len(n) < degree + 1: #If we have more data points to look at than there are nodes in the list we go here.
            for i in range(len(n)): #Number of data points to look at is limited to the size of the list
                t = n[i].timestep / simTime 
                x = n[i].x
                y = n[i].y
                obsX[i] = [t, x]
                obsY[i] = [t, y]
            coX = np.polyfit(obsX[:, 0], obsX[:, 1], len(n) - 1)
            coY = np.polyfit(obsY[:, 0], obsY[:, 1], len(n) - 1)
            newX = 0
            newY = 0
            for i in range(len(n)):
                newX += coX[i] * (((n[-1].timestep + 1) / simTime) ** i)
                newY += coY[i] * (((n[-1].timestep + 1) / simTime) ** i)
            return np.array([newX, newY])
        else: # Look at the number of data points we chose, and predict next position.
            for i in range(degree + 1): #Number of data points we look at is "degree" + 1.
                t = n[-degree - 1 + i].timestep / simTime
                x = n[-degree - 1 + i].x
                y = n[-degree - 1 + i].y
                obsX[i] = [t, x]
                obsY[i] = [t, y]
            coX = np.polyfit(obsX[:, 0], obsX[:, 1], degree)
            coY = np.polyfit(obsY[:, 0], obsY[:, 1], degree)
            newX = 0
            newY = 0
            for i in range(degree + 1):
                newX += coX[i] * (((n[-1].timestep + 1) / simTime) ** i)
                newY += coY[i] * (((n[-1].timestep + 1) / simTime) ** i)
            return np.array([newX, newY])

    @staticmethod
    def getPredictionList(nList, t, noOfDataPoints, flag):
        temp = []
        predictionList = []
        predPos = np.zeros(2)
        for n in nList:
            temp.append(n) 
            #if flag == 0:
            #   predPos = Model.predictNextPosition(temp, t, noOfDataPoints)
            #elif flag == 1:
            predPos = Model.predictionPolynomialRegression(temp, noOfDataPoints - 1, t, flag) 
            tempNode = Node(n.timestep + 1, n.id, predPos[0], predPos[1], n.v)
            predictionList.append(tempNode)
        return predictionList
      
    @staticmethod
    def coordinateRMSE(actualPos, predictedPos, t, noDataPoints):
        RMSEx = 0
        RMSEy = 0
        for i in range(1, len(actualPos)):
            RMSEx += (actualPos[i].x - predictedPos[i - 1].x) ** 2
            RMSEy += (actualPos[i].y - predictedPos[i - 1].y) ** 2
        RMSEx = np.sqrt(RMSEx / len(predictedPos))
        RMSEy = np.sqrt(RMSEy / len(predictedPos))
        return np.array([RMSEx, RMSEy])

    @staticmethod
    def positionalRMSE(actualPos, predictedPos):
        tempNode = 0
        RMSE = 0
        timeStep = actualPos[0].timestep
        id = actualPos[0].id
        for i in range(1, len(actualPos)):
            tempNode = Node(timeStep + 1, id, predictedPos[i - 1].x, predictedPos[i - 1].y, actualPos[i].v)
            RMSE += (tempNode.distanceBetween(actualPos[i])) ** 2
        return np.sqrt(RMSE / len(actualPos))

    @staticmethod
    def getDifferenceInDistanceList(actualPos, predictedPos):
        diffList = []
        i = 0
        for n in actualPos:
            diffList.append(n.distanceBetween(predictedPos[i]))
            i += 1
        return diffList
