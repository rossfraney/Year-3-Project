import cv2
import conf
import time
import datetime

conf = conf.config()

def init_camera():
    # initialize the camera and grab a reference to the raw camera capture
    camera = cv2.VideoCapture(0)
    #Camera Width
    camera.set(3,conf["width"])
    #Camera Height
    camera.set(4,conf["height"])
    #Camera FPS???????????????????
    camera.set(5,conf["fps"])

    return camera

# allow the camera to warm up
print "[INFO] warming up..."
time.sleep(conf["camera_warmup_time"])

# show current date & time
print datetime.datetime.now()

# initialise average frame
def get_avg():
    avg = None
    return avg

# last uploaded timestamp
def get_lu():
    lastUploaded = datetime.datetime.utcnow()
    return lastUploaded

#fr ame motion counter
def get_mc():
    return 0
