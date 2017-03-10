import cv2
import conf
import time
import datetime

# initialise conf.py file
conf = conf.config()

def init_camera():
    # initialize the camera
    camera = cv2.VideoCapture(0)
    
    # camera width
    camera.set(3,conf["width"])
    # camera height
    camera.set(4,conf["height"])
    # camera FPS
    camera.set(5,conf["fps"])

    return camera

# allow the camera to warm up
print "Camera Warming Up..."
time.sleep(conf["camera_warmup_time"])

# show current date & time for reference
print datetime.datetime.now()

# initialise average frame
# used for measuring frame_delta
def get_avg():
    avg = None
    return avg

# last uploaded timestamp
def get_lu():
    lastUploaded = datetime.datetime.utcnow()
    return lastUploaded

# frame motion counter
def get_mc():
    return 0
