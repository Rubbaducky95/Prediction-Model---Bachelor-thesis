import pygame
import time
from gpiozero import PWMLED #For the ultrabright
import gpiod #For the orange LEDs 
import math
from math import pi

# LED setup
led_left = PWMLED(12)  # LED for left turn indication
led_middle = PWMLED(13)  # LED for middle (usually showing angle or status)
led_right = PWMLED(19)  # LED for right turn indication
LED_PIN = 27  # Left orange
LED_PIN2 = 17  # Right orange

chip = gpiod.Chip('gpiochip4')
led_line_left = chip.get_line(LED_PIN)
led_line_left.request(consumer="LED", type=gpiod.LINE_REQ_DIR_OUT)
led_line_right = chip.get_line(LED_PIN2)
led_line_right.request(consumer="LED", type=gpiod.LINE_REQ_DIR_OUT) 

#Starting values for LEDs
led_middle.value = 0.5
led_left.value = 0.0
led_right.value = 0.0
led_line_left.set_value(0)
led_line_right.set_value(0)

def read_positions_angles(file_path):
    output_data = []
    with open(file_path, 'r') as file:
        next(file)  # Skip the header line
        for line in file:
            parts = line.strip().split()
            curPos = (float(parts[0]), float(parts[1]))  # Current position coordinates
            predPos = (float(parts[2]), float(parts[3]))  # Predicted position coordinates
            A = (float(parts[4]), float(parts[5]))  # A position coordinates
            B = (float(parts[6]), float(parts[7]))  # B position coordinates
            angle = float(parts[8]) # Change in angle of the entity
            direction = parts[9] # Direction (straight/left/right)
            output_data.append((curPos, predPos, A, B, angle, direction))
    return output_data

def update_leds(angle, direction):
    # LED control based change in direction
    
    magnitude = 0.1
        
    if direction == 'Left':
        led_middle.value -= magnitude if(led_middle.value != 0) else 0
        led_left.value += magnitude if(led_left.value != 1) else 0
        led_right.value -= magnitude if(led_right.value !=0) else 0
        led_line_right.set_value(0)
        led_line_left.set_value(1)
    elif direction == 'Right':
        led_middle.value -= magnitude if(led_middle.value != 0) else 0
        led_left.value -= magnitude if(led_left.value != 0) else 0
        led_right.value += magnitude if(led_right.value != 1) else 0
        led_line_right.set_value(1)
        led_line_left.set_value(0)
    elif direction == 'Straight':
        led_middle.value += magnitude if(led_middle.value != 1) else 0
        led_left.value = 0
        led_right.value = 0
        led_line_right.set_value(0)
        led_line_left.set_value(0)

def to_pygame(coordinates, height):
    #Update the coordinates to draw it the "normal" way
    return(coordinates[0], height - coordinates[1])

def error(curPos, predPos):
    #Return the distance between two coordinates
    return ((curPos[0] - predPos[0])**2 + (curPos[1] + predPos[1])**2)**(1/2)

def draw_positions_control_leds(output_data):
    pygame.init()
    width = 1000
    height = 800
    window_size = [width, height]  # Window size, can be adjusted
    screen = pygame.display.set_mode(window_size)
    pygame.display.set_caption("Path Visualization")

    # Font setup
    font = pygame.font.Font(None, 36)  # Default font and size

    # Colors for drawcomponenet
    black = (0, 0, 0)
    white = (255, 255, 255)
    green = (0, 255, 0)
    red = (255, 0, 0)
    blue = (0, 0, 255)

    # Scale factors to adjust the positions to fit the window size
    scale_x = 40
    scale_y = 40

    previous_positions = []  # List to store all positions for drawing
    previousPredPos = (0,0)

    running = True
    index = 0
    while running:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
        
        if index < len(output_data):
            curPos, predPos, A, B, angle, direction = output_data[index]

            #For displaying the coordinates
            realCurPos = curPos
            realPredPos = predPos
            realA = A
            realB = B

            # Update base translation to center the current position and invert coordinates to normal x-y-plane
            curPos = to_pygame(curPos, height)
            predPos = to_pygame(predPos, height)
            A = to_pygame(A, height)
            B = to_pygame(B, height)

            if index == 0:
                base_x, base_y = curPos
            translate_x = window_size[0] // 2 - int(curPos[0] * scale_x)
            translate_y = window_size[1] // 2 - int(curPos[1] * scale_y)
            
            # Add current positions to the list
            previous_positions.append((curPos, predPos, A, B))
            
            # Clear the screen for new position
            screen.fill(black)
            
            # Draw positions
            for pos in previous_positions:
                cur, pre, posA, posB = pos

                # Translate each point based on the initial base position
                cur_screen = (int(cur[0] * scale_x) + translate_x, int(cur[1] * scale_y) + translate_y)
                pre_screen = (int(pre[0] * scale_x) + translate_x, int(pre[1] * scale_y) + translate_y)
                A_screen = (int(posA[0] * scale_x) + translate_x, int(posA[1] * scale_y) + translate_y)
                B_screen = (int(posB[0] * scale_x) + translate_x, int(posB[1] * scale_y) + translate_y)

                # Draw the circles and lines forming the path
                pygame.draw.circle(screen, green, cur_screen, 5)
                pygame.draw.lines(screen, blue, False, [cur_screen, A_screen, pre_screen, B_screen, cur_screen], 2)
                #pygame.draw.arc(screen, blue, [cur_screen, A_screen, math.dist(cur_screen[0],B_screen[0]), math.dist(cur_screen[1],A_screen[1])],pi/2,pi)

            # Render and display the data text
            direction_text = font.render(f"Predicted direction: {direction}", True, white)
            screen.blit(direction_text, (10, 10))  # Position the text
            curPos_text = font.render(f"Current position:     X: {round(realCurPos[0],3)} Y: {round(realCurPos[1],3)}", True, white)
            screen.blit(curPos_text, (10, 40))  # Position the text
            predPos_text = font.render(f"Predicted position:  X: {round(realPredPos[0],3)} Y: {round(realPredPos[1],3)}", True, white)
            screen.blit(predPos_text, (10, 70))  # Position the text
            error_text = font.render(f"Error: {math.dist(realCurPos, previousPredPos)}", True, white)
            screen.blit(error_text, (10, 100))  # Position the text
            angle_text = font.render(f"Theta: {angle}", True, white)
            screen.blit(angle_text, (10, 130))  # Position the text
            
            # Update display
            pygame.display.flip()

            #Update LEDs
            update_leds(angle, direction)

            #Print data
            print("Difference in angle:", angle, "Predicted direction:", direction)

            previousPredPos = realPredPos

            time.sleep(0.5)
            index += 1
        else:
            running = False

    pygame.quit()
    led_left.close()
    led_middle.close()
    led_right.close()
    led_line_right.set_value(0)
    led_line_left.set_value(0)


# Read output data from file
output_data = read_positions_angles('/home/pi/Prediction_model/results/APFP_VRU3.txt')

# Loop through data to draw movement and update LEDs
draw_positions_control_leds(output_data)

#Print for comparison
print(output_data)
