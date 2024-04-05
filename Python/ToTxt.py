import os
from ReadOutput import ReadOutPut
from Model import Model

#Need to configure main to test the model
class ToTxt:
    @staticmethod
    def minRMSEforVRUs(vadereOutput,t,flag): #flag1 = gamla modellen, flag0 = nya modellen
        output = "ID dataPoints minRMSE\n"
        totNrVRUs = vadereOutput.totalNrOfVRUs()
        RMSE = []
        for i in range(1, totNrVRUs + 1):
            VRU = vadereOutput.getDatafor(i)
            output += str(i) + " "
            for j in range(1,10):
                RMSE.append(Model.positionalRMSE(VRU, Model.getPredictionList(VRU, t, j, flag)))
        minRMSE = min(RMSE)
        output += str(1+RMSE.index(minRMSE)) + " "
        output += str(minRMSE) + "\n"
        RMSE.clear()
        VRU.clear()
        try:
            with open("result_minRMSE.txt", "w") as writer: 
                writer.write(output)
        except IOError as e:
            print(e)

    @staticmethod
    def maxRMSEforVRUs(vadereOutput, t, flag): 
        output = "ID dataPoints maxRMSE\n"
        totNrVRUs = vadereOutput.totalNrOfVRUs()
        RMSE = []
        for i in range(1, totNrVRUs+1):
            VRU = vadereOutput.getDataFor(i)
            output += str(i) + " "
            for j in range(1, 10):
                 RMSE.append(Model.positionalRMSE(VRU, Model.getPredictionList(VRU, t, j, flag)))
            maxRMSE = max(RMSE)
            output += str(1+RMSE.index(maxRMSE)) + " "
            output += str(maxRMSE) + "\n"
            RMSE.clear()
            VRU.clear()
        try:
            with open("result_maxRMSE.txt", "w") as writer:
                writer.write(output)
        except IOError as e:
            print(e)

    @staticmethod
    def RMSEforVRUsWithSpecDataPoints(vadereOutput, t, noDataPoints, flag):
        output = "ID no.DataPoints RMSE\n"
        totNrVRUs = vadereOutput.totalNrOfVRUs()
        for i in range(1, totNrVRUs+1):
            VRU = vadereOutput.getDataFor(i)
            output += str(i) + " "
            RMSE = Model.positionalRMSE(VRU, Model.getPredictionList(VRU, t, noDataPoints, flag))
            output += str(noDataPoints) + " "
            output += str(RMSE) + "\n"
            VRU.clear()
        directoryPath = ""
        if not directoryPath.endswith(os.path.sep):
            directoryPath += os.path.sep
        directory = os.path.dirname(directoryPath)
        if not os.path.exists(directory):
            os.makedirs(directory)
        filePath = directoryPath + "result_RMSE_allVRUs_dataPoint.txt"
        try:
            with open(filePath, "w") as writer:
                writer.write(output)
        except IOError as e:
            print(e)

    @staticmethod
    def minMaxRMSEforDataPoints(vadereOutput, t, lowerLimitDP, upperLimitDP, flag, output_dir):
        output = "no.DataPoints minRMSE maxRMSE\n"
        totNrVRUs = vadereOutput.totalNrOfVRUs()
        RMSE = []
        for i in range(lowerLimitDP, upperLimitDP + 1):
            for j in range(1, totNrVRUs + 1):
                VRU = vadereOutput.getDataFor(j)
                RMSE.append(Model.positionalRMSE(VRU, Model.getPredictionList(VRU, t, i, flag)))
            output += f"{i} {min(RMSE)} {max(RMSE)}\n"
            RMSE.clear()

    # Use the output_dir to construct the file path
        filePath = os.path.join(output_dir, "result_minMax_RMSE_dataPoints.txt")

    # Ensure the output directory exists
        os.makedirs(output_dir, exist_ok=True)

        try:
            with open(filePath, "w") as writer:
                writer.write(output)
        except IOError as e:
            print(e)

    @staticmethod
    def minMaxRMSEforVRUsWithSpecDataPoints(vadereOutput, t, noDataPoints, flag):
        output = "SpecDataPoints minRMSE maxRMSE\n"
        totNrVRUs = vadereOutput.totalNrOfVRUs()
        RMSE = []

        for i in range(1, totNrVRUs + 1):
            VRU = vadereOutput.getDataFor(i)
            RMSE.append(Model.positionalRMSE(VRU, Model.getPredictionList(VRU, t, noDataPoints, flag)))
            VRU.clear()

        output += f"{min(RMSE)} {max(RMSE)}\n"

        directoryPath = "/home/pi/Prediction_model/Scenarios_outputs/Intersection_clone1_24-02-29"
        directoryPath = os.path.join(directoryPath, "")  # Ensure the directory path ends with a file separator

        # Create the directory if it does not exist
        os.makedirs(directoryPath, exist_ok=True)

        if flag == 1:
           filePath = os.path.join(directoryPath, "result_minMax_RMSE_allVRUs_AutoDPs.txt")
        else:
           filePath = os.path.join(directoryPath, f"result_minMax_RMSE_allVRUs_{noDataPoints}DPs.txt")

        try:
            with open(filePath, "w") as writer:
                writer.write(output)
        except IOError as e:
            print(f"Error writing to file: {e}")

   
    @staticmethod
    def predictedPositionsForAllVRUs(vadereOutput, t, noDataPoints):
        output = "timeStep VRUID x-position y-position\n"
        totNrVRUs = vadereOutput.totalNrOfVRUs()
        predictedPositions = []
        for i in range(1, totNrVRUs+1):
            predictedPositions = Model.getPredictionList(vadereOutput.getDataFor(i), t, noDataPoints, 1)
            for n in predictedPositions:
                output += str(n.timeStep) + " "
                output += str(n.id) + " "
                output += str(n.x) + " "
                output += str(n.y) + "\n"
            predictedPositions.clear()
        directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\"
        if not directoryPath.endswith(os.path.sep):
            directoryPath += os.path.sep
        directory = os.path.dirname(directoryPath)
        if not os.path.exists(directory):
            os.makedirs(directory)
        filePath = directoryPath + "result_predPos_allVRUs.txt"
        try:
            with open(filePath, "w") as writer:
                writer.write(output)
        except IOError as e:
            print(e)
    
    @staticmethod
    def predictedPositionsForVRU(vadereOutput, t, noDataPoints, VRUid, flag):
        output = "timeStep VRUID x-position y-position\n"
        predictedPositions = Model.getPredictionList(vadereOutput.getDataFor(VRUid), t, noDataPoints, flag)
        for n in predictedPositions:
            output += str(n.timeStep) + " "
            output += str(n.id) + " "
            output += str(n.x) + " "
            output += str(n.y) + "\n"
        directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\"
        if not directoryPath.endswith(os.path.sep):
            directoryPath += os.path.sep
        directory = os.path.dirname(directoryPath)
        if not os.path.exists(directory):
            os.makedirs(directory)
        filePath = directoryPath + "result_predPos_" + str(noDataPoints) + "DPs_" + "VRU" + str(VRUid) + ".txt"
        try:
            with open(filePath, "w") as writer:
                writer.write(output)
        except IOError as e:
            print(e)
    
    @staticmethod
    def actualPositionsForVRU(vadereOutput, VRUid):
        output = "timeStep VRUID x-position y-position\n"
        actualPositions = vadereOutput.getDataFor(VRUid)
        for n in actualPositions:
            output += str(n.timeStep) + " "
            output += str(n.id) + " "
            output += str(n.x) + " "
            output += str(n.y) + "\n"
        directoryPath = "C:\\Users\\ruben\\OneDrive\\Documents\\Datateknik VT 24\\EXAMENSARBETE\\MatLab\\"
        if not directoryPath.endswith(os.path.sep):
            directoryPath += os.path.sep
        directory = os.path.dirname(directoryPath)
        if not os.path.exists(directory):
            os.makedirs(directory)
        filePath = directoryPath + "result_actualPos_VRU" + str(VRUid) + ".txt"
        try:
            with open(filePath, "w") as writer:
                writer.write(output)
        except IOError as e:
            print(e)
    
    @staticmethod
    def differenceListForAllVRUs(vadereOutput, t, noDataPoints, flag): #Change accordingly to match
        output = "Differences in distance:\n"
        totNrVRUs = vadereOutput.totalNrOfVRUs()
        for i in range(totNrVRUs):
            actualPos = vadereOutput.getDataFor(i)
            predictedPos = Model.getPredictionList(actualPos, t, noDataPoints, flag)
            for j in range(len(actualPos)):
                distanceBetween = actualPos[j].distanceBetween(predictedPos[j])
                if distanceBetween != 0:
                    output += str(distanceBetween) + "\n"
        directoryPath = ""
        if not directoryPath.endswith(os.path.sep):
            directoryPath += os.path.sep
        directory = os.path.dirname(directoryPath)
        if not os.path.exists(directory):
            os.makedirs(directory)
        filePath = directoryPath + "differenceInPos.txt"
        try:
            with open(filePath, "w") as writer:
                writer.write(output)
        except IOError as e:
            print(e)
    
