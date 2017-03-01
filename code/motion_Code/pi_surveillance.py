#Instructions for use#
#In working directory, must have the pyimagesearch folder, "clients.json" file(download this from API credentials)
#, conf.json, python file.
#Run by entering command: "python pi_surveillance.py --conf conf.json"





# import the necessary packages

from pyimagesearch.tempimage import TempImage
from imutils.video import VideoStream
import argparse
import warnings
import datetime
import imutils
import json
import time
import cv2

from pyfcm import FCMNotification

from pydrive.auth import GoogleAuth
from pydrive.drive import GoogleDrive

# construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-c", "--conf", required=True,
	help="path to the JSON configuration file")
ap.add_argument("-p", "--picamera", type=int, default=-1,
	help="whether or not the Raspberry Pi camera should be used")
args = vars(ap.parse_args())

#GoogleDrive Authentication
gauth = GoogleAuth()
gauth.LocalWebserverAuth()
drive = GoogleDrive(gauth)


# filter warnings, load the configuration and initialize the Dropbox
# client
warnings.filterwarnings("ignore")
conf = json.load(open(args["conf"]))
#client = None
        
# initialize the camera and grab a reference to the raw camera capture
camera = cv2.VideoCapture(0)
#Camera Width
camera.set(3,conf["Width"])
#Camera Height
camera.set(4,conf["Height"])

camera.set(5,conf["fps"])

##NOT SURE WHETHER TO KEEP IN THE RAWCAPTURE & TRUNCATES...TEST PERFORMANCE DIFFERENCES LOCALLY
##IS THIS ONLY FOR PICAMERA? I THINK SO.
#rawCapture = PiRGBArray(camera, size=tuple(conf["resolution"]))
 
# allow the camera to warmup, then initialize the average frame, last
# uploaded timestamp, and frame motion counter
print "[INFO] warming up..."
time.sleep(conf["camera_warmup_time"])
avg = None
print datetime.datetime.now()
lastUploaded = datetime.datetime.utcnow()
motionCounter = 0
# capture frames from the camera
while True:
        (grabbed, frame) = camera.read()
        text = "No Movement"

        if not grabbed:
                break

	# initialise the timestamp
	timestamp = datetime.datetime.utcnow()
 
	# resize the frame, convert it to grayscale, and blur it
	frame = imutils.resize(frame, width=500)
	gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
	gray = cv2.GaussianBlur(gray, (21, 21), 0)
 
	# if the average frame is None, initialize it
	if avg is None:
		print "[INFO] starting background model..."
		avg = gray.copy().astype("float")
		#rawCapture.truncate(0)
		continue
 
	# accumulate the weighted average between the current frame and
	# previous frames, then compute the difference between the current
	# frame and running average
	cv2.accumulateWeighted(gray, avg, 0.5)
	frameDelta = cv2.absdiff(gray, cv2.convertScaleAbs(avg))
        
	# threshold the delta image, dilate the thresholded image to fill
	# in holes, then find contours on thresholded image
	thresh = cv2.threshold(frameDelta, conf["delta_thresh"], 255,
		cv2.THRESH_BINARY)[1]
	thresh = cv2.dilate(thresh, None, iterations=2)
        cnts = cv2.findContours(thresh.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
        cnts = cnts[0] if imutils.is_cv2() else cnts[1]
 
	# loop over the contours
	for c in cnts:
		# if the contour is too small, ignore it
		if cv2.contourArea(c) < conf["min_area"]:
			continue
 
		# compute the bounding box for the contour, draw it on the frame,
		# and update the text
		(x, y, w, h) = cv2.boundingRect(c)
		cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 0, 0), 2)
		text = "Movement Detected"
 
	# draw the text and timestamp on the frame
	ts = timestamp.strftime("%A %d %B %Y %I:%M:%S%p")
	cv2.putText(frame, "Movement Status: {}".format(text), (10, 20),
		cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
	cv2.putText(frame, ts, (10, frame.shape[0] - 10), cv2.FONT_HERSHEY_SIMPLEX,
		0.35, (0, 0, 255), 1)
# check to see if the room is occupied
	if text == "Movement Detected":
		# check to see if enough time has passed between uploads
		if (timestamp - lastUploaded).seconds >= conf["min_upload_seconds"]:
			# increment the motion counter
			motionCounter += 1
 
			# check to see if the number of frames with consistent motion is
			# high enough
			if motionCounter >= conf["min_motion_frames"]:

				# Notifications to the App
				push_service = FCMNotification(api_key="AAAAsxrS_UY:APA91bGuW6Ir3I7V4s_p2g6ToQZJO-6XPG-obxiLB2zGnr88mqxa_hkccMJPVJTvjZoRnYYJAtM10oouUjK5T54hN75F33FZdq6eUNMP9GAF3_KV2i-E_e64IzoYSM4shE4GJ-8M4UKV")
				registration_id = "fhpisJCj0ek:APA91bHiEqtGefOA6MxZWnwqTo3JjLnjX-TPYW9V5izYJPAfbT_4UnkyiIyoqVjvrIFRH_A418nnta3l0qj4bX4Y-G4KcWM5Wp_iXmcbmjPWADvEBaMYWkcTa-3tJd-yBlkuvxUduRKd"
				message_title = "Motion Detected!"
				message_body = "Your SecuriPi system has detected movement and uploaded the corresponding images."
		
				result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)
		
				# write the image to temporary file
				t = TempImage()
				cv2.imwrite(t.path, frame)

                                #Drive Upload
                                file1 = drive.CreateFile()
                                file1.SetContentFile(t.path)
                                file1.Upload()
                                print('Created file%s with mimeType %s' % (file1['title'],
				  file1['mimeType']))

                                t.cleanup()
				# update the last uploaded timestamp and reset the motion
				# counter

				lastUploaded = timestamp
				motionCounter = 0

	# otherwise, the room is not occupied
	else:
		motionCounter = 0

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
 
	# clear the stream in preparation for the next frame
	#rawCapture.truncate(0)
