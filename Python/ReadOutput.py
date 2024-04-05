import os 
import math
from Node import Node

class ReadOutPut:
        def __init__(self, outputPath):
                self.timepassed = 0
                self.id = 0
                self.v = 0.0
                self.a = 0.0
                self.xpos = 0.0
                self.ypos = 0.0
                self.vsc = None 
                self.psc = None
                self.csc = None
                self.vDataOutput = os.path.join(outputPath, "velocity.txt")
                self.pDataOutput = os.path.join(outputPath, "position.txt")
                self.cDataOutput = os.path.join(outputPath, "noOfEntities.txt")
                #self.eDataOutput = os.path.join(outputPath, "evacTime.txt") 

        def totalNrOfVRUs(self):
            unique_vrus = set()  # A set to store unique VRU IDs
            with open(self.pDataOutput, "r") as csc:
                next(csc)  # Skip the header line
                for line in csc:
                    parts = line.split()
                    if parts:  # Check if the line is not empty
                     # Assuming the second part of the line is the pedestrian ID
                     pedestrianId = parts[1]
                     unique_vrus.add(pedestrianId)  # Add to the set of unique VRU IDs
            return len(unique_vrus)
        
        """
        def totalNrOfVRUs(self):
            with open(self.pDataOutput, "r") as csc:
                   next(csc)
                   for line in csc:
                       parts = line.split()
                       if parts:
                           timeStep = int (parts[0])
                   csc.readline()
                   #self.timepassed = int(csc.readline()) 
                   currentNr = 0
                   previousNr = 0
                   nrOfVRUs = previousNr
            while True:
                line = csc.readline()
                if not line:
                    break
                self.timePassed = int(line)
                currentNr = int(csc.readline())
                if previousNr < currentNr:
                    nrOfVRUs += currentNr - previousNr
                previousNr = currentNr
            return nrOfVRUs
            """
        
        def timeStepsFor(self, VRUid):
            with open(self.vDataOutput, 'r') as vsc:
                vsc.readline()
            startTime = int(vsc.readline())
            while True:
                line = vsc.readline()
                if not line:
                    break
                if int(line.split()[0]) != VRUid:
                    startTime = int(vsc.readline())
                else:
                    break
            noOftimeSteps = 0
            while True:
                line = vsc.readline()
                if not line:
                    break
                self.timePassed = int(line.split()[0])
                if int(line.split()[0]) == VRUid:
                    noOftimeSteps += 1
                else:
                    vsc.readline()
            return [startTime, noOftimeSteps]
        def meanVelocity(self, VRUid):
            with open(self.vDataOutput, 'r') as vsc:
                vsc.readline()
            vsum = 0.0
            count = 0
            while True:
                line = vsc.readline()
                if not line:
                    break
                self.timePassed = int(line.split()[0])
                if VRUid == int(line.split()[0]):
                    vsum += float(line.split()[1])
                    count += 1
                vsc.readline()
            return vsum / count

        def maxVelocity(self, VRUid):
            with open(self.vDataOutput, 'r') as vsc:
               vsc.readline()
            maxV = 0.0
            currentV = 0.0
            while True:
                line = vsc.readline()
                if not line:
                    break
                self.timePassed = int(line.split()[0])
                if VRUid == int(line.split()[0]):
                    currentV = float(line.split()[1])
                    if currentV > maxV:
                        maxV = currentV
                vsc.readline()
            return maxV

        def velocityAtGivenTime(self, VRUid, timeStep):
            with open(self.vDataOutput, 'r') as vsc:
                 next(vsc)  # Skip the header line if there is one
                 for line in vsc:  # This will automatically read lines until the end of file
                   parts = line.split()
                   if parts:  # Check if the line is not empty
                      currentStep, currentVRUid, velocity = int(parts[0]), int(parts[1]), float(parts[2])
                      if timeStep == currentStep and VRUid == currentVRUid:
                         return velocity  # Return immediately once the right line is found
            return 0.0  # Default return value if not found
        """
            with open(self.vDataOutput, 'r') as vsc:
               vsc.readline()
            while True:
                line = vsc.readline()
                if not line:
                    break
                if timeStep == int(line.split()[0]):
                    if VRUid == int(line.split()[0]):
                        self.v = float(line.split()[1])
                vsc.readline()
            return self.v
        """

        def meanAcceleration(self, VRUid):
            with open(self.vDataOutput, 'r') as vsc:
               vsc.readline()
            count = 0
            previousV = 0.0
            currentV = 0.0
            asum = 0.0
            while True:
                line = vsc.readline()
                if not line:
                    break
                self.timePassed = int(line.split()[0])
                if VRUid == int(line.split()[0]):
                    break
                vsc.readline()
            vsc.readline()
            while True:
                line = vsc.readline()
                if not line:
                    break
                self.timePassed = int(line.split()[0])
                if VRUid == int(line.split()[0]):
                    currentV = float(line.split()[1])
                    asum += (currentV - previousV) / 0.1
                    previousV = currentV
                    count += 1
                vsc.readline()
            return asum / count

        def maxAcceleration(self, VRUid):
            with open(self.vDataOutput, 'r') as vsc:
               vsc.readline()
            maxA = 0.0
            previousV = 0.0
            currentV = 0.0
            currentA = 0.0
            while True:
                line = vsc.readline()
                if not line:
                    break
                self.timePassed = int(line.split()[0])
                if VRUid == int(line.split()[0]):
                    currentV = float(line.split()[1])
                    currentA = (currentV - previousV) / 0.1
                    if currentA > maxA:
                        maxA = currentA
                    previousV = currentV
                vsc.readline()
            return maxA

        def accelerationAtGivenTime(self, VRUid, timeStep):
            with open(self.vDataOutput, 'r') as vsc:
               vsc.readline()
            previousV = 0.0
            while True:
                line = vsc.readline()
                if not line:
                    break
                self.timePassed = int(line.split()[0])
                if timeStep - 1 == self.timePassed:
                    if VRUid == int(line.split()[0]):
                        previousV = float(line.split()[1])
                if timeStep == self.timePassed:
                    if VRUid == int(line.split()[0]):
                        self.a = (float(line.split()[1]) - previousV) / 0.1
                vsc.readline()
            return self.a

        def getDataAt(self, timeStamp, VRUid):
            with open(self.pDataOutput, 'r') as psc:
                next(psc)  # Skip header line, if it exists.
                for line in psc:
                    parts = line.split()
                    if parts:  # Make sure the line is not empty
                        currentStep, currentVRUid = int(parts[0]), int(parts[1])
                        if currentStep == timeStamp and currentVRUid == VRUid:
                            self.xpos, self.ypos, self.v = float(parts[2]), float(parts[3]), self.velocityAtGivenTime(VRUid, timeStamp)
                            return Node(timeStamp, VRUid, self.xpos, self.ypos, self.v)
            return None  # If no matching line is found, you might return None or handle it differently.
            """
            with open(self.pDataOutput, 'r') as psc:
               psc.readline()
            self.v = self.velocityAtGivenTime(VRUid, timeStamp)
            while True:
                line = psc.readline()
                if not line:
                    break
                if int(line.split()[0]) == timeStamp:
                    if int(line.split()[0]) == VRUid:
                        self.xpos = float(line.split()[1])
                        self.ypos = float(line.split()[2])
                        break
                psc.readline()
            return Node(timeStamp, VRUid, self.xpos, self.ypos, self.v)
            """

        def getDataFor(self, VRUid):
            nodeList = []
            with open(self.pDataOutput, 'r') as psc:
             next(psc)  # Skip the header line if there is one
             for line in psc:  # This automatically reads lines until the end of the file
                 parts = line.split()
                 if parts:  # Check if the line is not empty
                    timePassed = int(parts[0])
                    if int(parts[1]) == VRUid:  # Assuming the VRU ID is the second element
                    # Now call getDataAt for this line's timePassed and VRUid
                    # Ensure getDataAt does not attempt to open and read from the file again
                    # It should only process the data passed to it, not read from the file
                         nodeList.append(self.getDataAt(timePassed, VRUid))
            return nodeList
            """
            with open(self.pDataOutput, 'r') as psc:
               psc.readline()
            nodeList = []
            while True:
                line = psc.readline() 
                if not line:
                    break
                self.timePassed = int(line.split()[0])
                if int(line.split()[0]) == VRUid:
                    nodeList.append(self.getDataAt(self.timePassed, VRUid))
                psc.readline()
            return nodeList
            """
        def getXDataAt(self, timeStep, noSteps, VRUid):
            dataAtEachStep = []
            for i in range(noSteps):
                if timeStep != 0:
                    dataAtEachStep.append(self.getDataAt(timeStep, VRUid))
                    timeStep -= 1
                else:
                    break
            return dataAtEachStep

        def distanceBetween(self, n1, n2):
            return math.sqrt((n2.x - n1.x) ** 2 + (n2.y - n1.y) ** 2)

        def changeInAngle(self, n):
            return 0

        def getAPFP(self, VRUid, timeStep, possibleTurningRadius):
            return 0
