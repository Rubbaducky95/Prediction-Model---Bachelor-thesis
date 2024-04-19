import pygame
import time

def read_coordinates(file_path):
    coordinates = []
    with open(file_path, 'r') as file:
        next(file)  # Skip the header line
        for line in file:
            parts = line.strip().split()
            curPos = (float(parts[0]), float(parts[1]))  # Current position coordinates
            predPos = (float(parts[2]), float(parts[3]))  # Predicted position coordinates
            A = (float(parts[4]), float(parts[5]))  # A position coordinates
            B = (float(parts[6]), float(parts[7]))  # B position coordinates
            angle = float(parts[8])
            direction = parts[9]
            coordinates.append((curPos, predPos, A, B, angle, direction))
    return coordinates

def draw_positions(coordinates):
    pygame.init()
    window_size = [1000, 800]  # Window size, can be adjusted
    screen = pygame.display.set_mode(window_size)
    pygame.display.set_caption("Path Visualization")

    # Font setup
    font = pygame.font.Font(None, 36)  # Default font and size

    # Colors
    black = (0, 0, 0)
    white = (255, 255, 255)
    green = (0, 255, 0)
    red = (255, 0, 0)
    blue = (0, 0, 255)

    # Scale factors to adjust the positions to fit the window size
    scale_x = 40
    scale_y = 40

    previous_positions = []  # List to store all positions for drawing
    base_x = base_y = 0  # Initial base for translation

    running = True
    index = 0
    while running:
        if index < len(coordinates):
            curPos, predPos, A, B, angle, direction = coordinates[index]

            # Update base translation to center the current position
            if index == 0:
                base_x, base_y = curPos
            translate_x = window_size[0] // 2 - int(curPos[0] * scale_x)
            translate_y = window_size[1] // 2 - int(curPos[1] * scale_y)

            # Add current positions to the list
            previous_positions.append((curPos, predPos, A, B))

            screen.fill(black)  # Clear the screen for new position

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


            # Render and display the direction text
            direction_text = font.render(f"Predicted direction: {direction}", True, white)
            screen.blit(direction_text, (50, 50))  # Position the text

            pygame.display.flip()  # Update the full display Surface to the screen
            time.sleep(0.5)  # Wait 1 second before the next position
            index += 1  # Move to the next set of coordinates
        else:
            running = False  # Exit the loop when all coordinates are drawn

        for event in pygame.event.get():
            if event.type is pygame.QUIT:
                running = False

    pygame.quit()

coordinates = read_coordinates('/home/pi/Prediction_model/results/APFP_VRU3.txt')
draw_positions(coordinates)
