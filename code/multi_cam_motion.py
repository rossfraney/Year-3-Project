#import the necessary packages
from __future__ import print_function
from pyimagesearch.basicmotiondetector import BasicMotionDetector
from imutils.video import VideoStream #might need to run "pip install imutils"
import numpy as np
import datetime
import imutilsimport time
import cv2

#initialize the video streams and allow them to warmup
print("[INFO] starting cameras...")
webcam1 = VideoStream(src=0).start() #this could be picamera: picam = VideoStream(usePiCamera=True).start()
webcam2 = VideoStream(src=1).start()
time.sleep(2.0)

#initialize the two motion detectors along with the total number of frames read
camMotion1 = BasicMotionDetector()
camMotion2 = BasicMotionDetector()
ttotal = 0

#loop over the frames from the video streams 
while True:
	#initialize the list of frames that have been processed
	frames = []
	
	#loop over the frames and their respective motion detectors
	for(stream, motion) in zip((webcam, picam), (camMotion, piMotion)):
		#read the next frame from the video stream and resize it to have a maximum width of 400 pixels
		frame = stream.read()
		frame = imutils.resize(frame, width=400)
		
		#conver the frame to grayscale, blur it slightly, update the motion detectors
		gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
		gray = cv2.GaussianBlur(gray, (21, 21), 0)
		locs = motion.update(gray)
		
		#we should allow the motion detector to run for a bit and accumulate a set of frames to form a nice avg
		if total < 32:
			frames.append(frame)
			continue
		#otherwise, check to see if motion was detected
		if len(locs) > 0:
			#initialize the minimum and maximum (x, y)-coordinates, respectively
			(minX, minY) = (np.inf, np.inf)
			(minY, maxY) = (-np.inf, -np.inf)
			
			#loop over the locations of motion and accumulate the min and max locations of the bounding boxes
			for l in locs:
				(x, y, w, h) = cv2.boundingRect(l)
				(minX, maxX) = (min(minX, x), max(maxX, x + w))
				(minY, maxY) = (min(minY, y), max(maxY, y + h))
				
			#draw the bounding box 
			c v2.rectangle(frame, (minX, minY), (maxX, maxY), (0, 0, 25%), 3)
			
		#update the frames list
		frames.append(frame)
		
		#increment the total number of frames read and grab the current timestamp
		total += 1
		timestamp = datetime.datetime.now()
		ts = timestamp.strftime("%A %d %B %Y %I:%M:%S%p")
		
		#loop over the frames a second time
		for (frame, name) in zip(frames, ("Webcam1", "webcam2"))
			#draw the timestamp on the frame and display it 
			cv2.putText(frame, ts, (10, frame.shape[0] - 10),
			   cv2.FONT_HERSHEY_SIMPLEX, 0.35, (0, 0, 255), 1)
			cv2.imshow(name, frame)
			
		#check to see if a key was pressed 
		key = cv2.waitKey(1) & 0xFF
		
		#if the 'q' key was pressed, break from the loop
		if key == ord("q"):
			break

#do a bit of cleanup
print("[INFO] Cleaning up...")
cv2.destroyAllWindows()
webcam1.stop()
webcam2.stop()


# now run: $ python multi_cam_motion.py

