# import the necessary packages
from imutils.video import VideoStream
import warnings
import datetime
import imutils
import time
import cv2
from threading import Thread
import vlc

import send_sms
import g_drive
import conf
import camera_conf
import notify

#GoogleDrive Authentication
g_drive.authentication()
gauth = g_drive.get_gauth()
drive = g_drive.get_drive()

#Initialise config file
conf = conf.config()

# filter warnings <<Check what this does
warnings.filterwarnings("ignore")

#Initialise camera info
camera = camera_conf.init_camera()
lastUploaded = camera_conf.get_lu()
avg = camera_conf.get_avg()
motionCounter = camera_conf.get_mc()

p = vlc.MediaPlayer("file:///home/pi/securiPi/soundFiles/Siren.mp3")

def threaded_music():
	#os.system("omxplayer -o local /home/pi/securiPi/soundFiles/Siren.mp3")	
        print "before play"
        p.play()
        print "after play"
        time.sleep(2)
        print "slept"
        p.stop()

def grayscale():
        # resize the frame, convert it to grayscale, and blur it
        global gray
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        gray = cv2.GaussianBlur(gray, (21, 21), 0)

def threshold_image():
	# threshold the delta image, dilate the thresholded image to fill in holes
	global thresh
	thresh = cv2.threshold(frameDelta, conf["delta_thresh"], 255, cv2.THRESH_BINARY)[1]
	thresh = cv2.dilate(thresh, None, iterations=2)
	
def contour_image():
        # find contours on thresholded image
        global cnts
        cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        cnts = cnts[0] if imutils.is_cv2() else cnts[1]

def draw_contour_box():
        (x, y, w, h) = cv2.boundingRect(c)
        cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 0, 0), 2)

def draw_frame_text():
        global ts
	ts = timestamp.strftime("%A %d %B %Y %I:%M:%S%p")
	cv2.putText(frame, "Movement Status: {}".format(text), (10, 20),
		cv2.FONT_HERSHEY_COMPLEX, 0.5, (0, 0, 255), 2)
	cv2.putText(frame, ts, (10, frame.shape[0] - 10), cv2.FONT_HERSHEY_COMPLEX,
		1, (0, 0, 255), 1)
	
def check_uploadable():
		# check to see if enough time has passed between uploads
		global lastUploaded, motionCounter
		if (timestamp - lastUploaded).seconds >= conf["min_upload_seconds"]:
			# increment the motion counter
			motionCounter += 1
 
			# check to see if the number of frames with consistent motion is high enough
			if motionCounter >= conf["min_motion_frames"]:       
				# write the image to temporary image
				temp = g_drive.get_ti()
				cv2.imwrite(temp.path, frame)
                                
                                #Drive Upload
                                g_drive.uploader(ts)                                
                                if(timestamp - lastUploaded).seconds >= conf["notification_period"]:
                                        #notify.notify()
                                        if conf["enable_sms"]:
                                                #Send an SMS
                                                send_sms.sms(conf["phone_number"])
                                        if conf["enable_audio"]:
                                                try:    
                                                        thread = Thread(target = threaded_music, args = ())
                                                        thread.start()
                                                        thread.join()
                                                        print "thread finished...exiting"
                                                except:
                                                       print "Thread dont work"
                                temp.cleanup()

				# update the last uploaded timestamp and reset the motion counter
				lastUploaded = timestamp
				motionCounter = 0     

# capture frames from the camera
while True:
        (grabbed, frame) = camera.read()
        text = "No Movement"

        if not grabbed:
                break

	# initialise the timestamp
	timestamp = datetime.datetime.utcnow()

        grayscale()
        
        # if the average frame is None, initialize it
        if avg is None:
                print "[INFO] starting background model..."
                avg = gray.copy().astype("float")
                continue
         
	# accumulate the weighted average between the current frame and
	# previous frames, then compute the difference between the current
	# frame and running average
	cv2.accumulateWeighted(gray, avg, 0.5)
	frameDelta = cv2.absdiff(gray, cv2.convertScaleAbs(avg))

        threshold_image()
        contour_image()
        
	# loop over the contours
	for c in cnts:
		# if the contour is too small, ignore it
		if cv2.contourArea(c) < conf["min_area"]:
			continue
 
		# compute the bounding box for the contour, draw it on the frame,
		# and update the text
		draw_contour_box()
		text = "Movement Detected"
 
	# draw the text and timestamp on the frame
	draw_frame_text()
        # check to see if the room is occupied
	if text == "Movement Detected":        
                check_uploadable()

	# otherwise, the room is not occupied
	else:
		motionCounter = 0
        #p.stop()
        # check to see if the frames should be displayed to screen
	if conf["show_video"]:
		# display the security feed
		cv2.imshow("Security Feed", frame)
             	#cv2.imshow("Thresh", thresh)
                #cv2.imshow("Frame Delta", frameDelta)   
		key = cv2.waitKey(1) & 0xFF
 
		# if the `q` key is pressed, break from the loop
		if key == ord("q"):
			break
