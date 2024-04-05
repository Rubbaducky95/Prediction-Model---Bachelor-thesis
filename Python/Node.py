import math 

class Node:
    def __init__(self, timeStep, id, x, y, v):
        self.timestep = timeStep
        self.id = id
        self.x = x
        self.y = y
        self.v = v

    def __lt__(self, other):
        return self.timestep < other.timestep
    
    def getId(self):
        return self.id
    
    @property
    def timeStep(self):
        return self.timeStep
    
    def getX(self):
        return self.x
    
    def getY(self):
        return self.y

    def getV(self):
        return self.v
    
    def distanceBetween(self, n):
        return math.sqrt((self.x - n.x) ** 2 + (self.y -n.y) ** 2)

    def lengthOf(self):
        return math.sqrt(self.x ** 2 + self.y ** 2)
    
    def directionOfVector(self, n):
        vectorX = n.x - self.x
        vectorY = n.y -self.y
        angle = math.atan2(vectorX, vectorY) * 180 /math.pi
        return angle
    
    def angleBetween(self, n1, n2):
        return abs(self.directionOfVector(n2) - self.directionOfVector(n1)) 
        #Orignally: return abs(n1.directionOfVector(n2) - n2.directionOfVector(self))
    
    # @property, is dat nödvändigt maybe
    def getVx(self, n):
        return n.v * math.cos(math.radians(self.directionOfVector(n)))

    def getVy(self, n):
        return n.v * math.sin(math.radians(self.directionOfVector(n)))

    def __str__(self):
        return f"\nTime step: {self.timeStep()} ID: {self.getId()}\nX: {self.getX()}, Y: {self.getY()}\nVelocity: {self.getV()}"
