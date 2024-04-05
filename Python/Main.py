import os
from ReadOutput import ReadOutPut
from ToTxt import ToTxt

# flag 0, spec datapoints
#flag 1, no matter
if __name__ == "__main__":
# Input directory where your data is located
    data_directory = os.path.join("/home/pi/Prediction_model/Scenarios_outputs/Intersection_clone1_24-02-29")
    
    # Define an output directory within your project for the result files
    output_directory = os.path.join("/home/pi/Prediction_model/results")
    
    # Ensure the output directory exists, create if it doesn't
    os.makedirs(output_directory, exist_ok=True)

    # Create a ReadOutPut object with the path to your data
    read_output = ReadOutPut(data_directory)
    
    # Call the method and pass the output directory where the result file should be saved
    #ToTxt.minMaxRMSEforDataPoints(read_output, 0.9, 2, 6, 0, output_directory)

    #ToTxt.minMaxRMSEforVRUsWithSpecDataPoints(read_output, 0.9, 2, 0) # flag1 funkar icke
    #ToTxt.RMSEforVRUsWithSpecDataPoints(read_output, 0.9, 2, 0)
    #ToTxt.minMaxRMSEforDataPoints(read_output,0.9,2,6,0,"/home/pi/Prediction_model/results")

"""
if __name__ == "__main__":
    file_path = os.path.join("/home/pi/Prediction_model/Scenarios_outputs/Intersection_clone1_24-02-29")
    read_output = ReadOutPut(file_path)
    ToTxt.minMaxRMSEforDataPoints(read_output, 0.9, 2, 6, 1)
"""    
