#import the necessary packages
import imutils
import cv2
class BasicMotionDetector
	def __init__(self, accumWeight=0.5, deltaThresh=5, minArea=5000):
	
		#deltaThresh when small will detect more motion and larger will detect less. 
		#Could use smaller for the baby monitoring as the movements when the child wakes could be subtle.
		#minArea is the area that will be labelled as "motion". Using this method, background noise will 
		#be minimized.
		
		self.isv2 = imutils.is_cv2()
		self.accumWeight = accumWeight
		self.deltaThresh = deltaThresh
		self.minArea = minArea 
		
		#initialize the average image for motion detection
		self.avg = None 
		
		
    def update(self, image):
		#initialize the list of locations containing motion
		locs = []
		
		#if the average is None, initialize it
		if self.avg is None: 
			self.avg = image.astype("float")
			return locs
		
		#otherwise, accumulate the weighted average between the current frame and the previous frames, then 
		#compute the pixel-wise differences between the current frame and the running average.
		cv2.accumulateWeighted(image, self.avg, self.accumWeight)
		frameDelta = cv2.absdiff(image, cv2.convertScaleAbs(self.avg)
		
		#threshold the delta image and apply a series of dilations to help fill in the holes
		thresh = cv2.threshold(frameDelta, self.deltaThresh, 255, cv2.THRESH_BINARY)[1]
		thresh = cv2.dilate(thresh, None, iterations=2)
		
		#find contours in the threshold image, taking care to use the appropriate version of OpenCV
		cnts = cv2.findContours(thresh, cv2.RET_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
		cnts = cnts[0] if self.isv2 else cnts[1]
		
		#loop over the contours 
		for c in cnts:
			#only add the contour to the locations list if it exceed the minimum area
			if cv2.contourArea(c) > self.minArea:
			locs.append(c)
		
		#return the set of locations
		return locs