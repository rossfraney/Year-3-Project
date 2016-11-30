# **1. Introduction**

## 1.1 Overview of Project
This project is a cost effective and easy to use home surveillance / security system which will enable users to keep track of several cameras positioned in various locations of their choosing, via their smartphone or personal computer, over Wi-Fi. The system will be based around a Raspberry pi, and will include motion detection software which will activate image capturing on each individual camera if and only if there is movement in that camera’s frame. These images will be then taken in a pre-set recurring time frame and stored in a drop-box, in order for the user to see them remotely using a VNC server. This will also mean that there will be images available if there is a breach of security in the owner’s home, which can be handed over to authorities to strengthen any resulting legal case that may arise.

The user will also have the option of setting up a live link with that video camera through video streaming software such as skype or the likes, in order to view, in real time, the video feed of that camera, which would allow not only a security function, but actually allow a user to communicate with whoever may be in the room in question, allowing the system to be used for the purpose of monitoring young children and / or elderly people that may be under the care of the user.

As well as the motion detection camera software, the system could be used with motion sensors placed on doors/windows etc. which also activates the image capturing capabilities of the camera.

The main functions, then, can be summarized as follows:
-	Remote notifications of detected motion to one’s android device, along with images. 
-	Ability to fetch a live stream of cameras over Wi-Fi.
-	Motion detection of varying sensitivity depending on the use of the camera (EG monitoring a sleeping baby vs making use of the camera for security reasons. 
-	Option to set an alarm once motion is triggered. 
-	GPS system that could make use of your phone to determine whether notifications are needed based on whether you are in your home or not. This could be set to automatically arm the system once you are identified as being away from home. 
-	A mobile application that provides the user with the view of current enabled camera(s). 

## 1.2 Project Scope 
This system was developed with the home-user in mind, and as such will provide a convenient, cost effective method of monitoring a home, without unnecessary complexity of use, or cost. A large factor of this system is that it will aim not just to satisfy the security needs of its users, but also be useful in a more general, everyday life sense. The versatility of this product means that the target users span a wide range of demographics. New / busy parents, child minders, people with elderly relatives, small businesses in need of till surveillance, etc. More specific, and detailed use cases can be seen in the respective section in this document. 

## 1.3 Document Outline
**Section 2** of this document has been designed to provide a more in-depth non-technical description of the main functions of this surveillance system. This includes the different interfaces the user will interact with, the different options available to the user in terms of configuration and preferences, and example use cases to display the practicality and simplicity of the system, for a multitude of diverse user demographics.

**Section 3** of this document will give a general overview of the system requirements, i.e. the various technical frameworks it must support to function as planned. This will include software/hardware requirements, security requirements, functionality, usability, and accessibility requirements that this system must adhere to, so that it can benefit the widest spanning user-base possible. 

**Section 4** will focus on the individual components/modules of the system, the design decisions in terms of how each will interact with the other, Including a component Diagram. 

## 1.4 Motivations
Our main motivation for the development of this system was the fact that currently, home security systems are not something that most people consider, as they are perceived as being expensive, almost “luxury” systems, that the average person could not afford. Unfortunately, this is largely true. However, we believe considering how advanced society has become in terms of technology, home security should be established as a concept that all home owners consider adding to their home, a new “societal norm” if you will.   

Our focus is to achieve this goal of introducing the idea that security should be a fundamental concern for all home owners, and should not be overlooked due to fears of complexity/price, and also to add useful features that can help people in other aspects of their life aside from security, as mentioned above. To do this, we needed to choose a medium that is already a cornerstone in the everyday life of the general public. 

Nowadays many people use smartphones to manage a variety of different aspects of life be it nutrition, time keeping, socialising, fitness, music, media/entertainment, etc. As such, this is a comfortable and well-known platform for a huge number of people. The system should not make users feel like they must learn something new, which can be an off-putting idea for many, particularly for people who may not necessarily be tech-literate. Our desire is to deliver a system that has all the functionality of a high-priced industrial security system and more, but packaged in the familiar format, non-intimidating format of a mobile application.

## 1.5 References:

Image Storage:
https://www.dropbox.com/
https://www.raspberrypi.org/

Security checks:
http://searchsecurity.techtarget.com/definition/Secure-Sockets-Layer-SSL
http://info.ssl.com/
https://www.dropbox.com/help/27

Learning Python:
https://www.codecademy.com/learn/python
https://www.python.org/
http://www.learnpython.org/

* * * 
# **2. Description**
## 2.1 Features
The main functions in this system involve the user being able to interact with a variety of web cameras that have been set up around their home/place of work/etc., from a remote location such as a mobile device, in order to monitor a particular area using motion detection and image capture, and to take action based on the images being captured.  Although the following features will be explained in more detail in section 3, below is a general outline of the main features the system should provide:

-	An Android Application 
-	Motion Detection System
-	Image Storing 
-	Triggerable Alarm (Through multiple pathways)
-	Automatic Arming (based on GPS tracking)
-	Ability to respond to security notification with multiple “one-click” options
-	Multiple Alarm Sound options 
-	User Preferences/Settings (camera sensitivity, enable/disable GPS, change alarm sound etc.)

 
## 2.2 Target User Characteristics & Environments
In regards to the expected profile of prospective users, the main categories can be summarized as one, or any combination of the following:

-	New Parents who would benefit from the baby monitoring capabilities of this product.
-	Carers be it for young children or for the elderly who could utilize such a system to give the cared subjects independence while still monitoring their activities, and even having the opportunity to communicate with them at the tap of a button.  
-	General Home owners who are living alone, or who’s house is left unoccupied for large portions of the day. 
-	Anybody interested in adding extra security to a particular asset or number of assets. For example, one may want to add security to the garage/driveway in which they keep their car. Another example may be a business owner interested in monitoring a safe or till.

## 2.3 Use Cases
###### USE CASE 1: Setting up a profile

###### USE CASE 2: Investigating a Triggered Alarm

###### USE CASE 3: Automatic Arming

###### USE CASE 4: 


## 2.4 Example Scenarios
Outlined below are examples of situations in which this system will prove a useful tool for users in a number of scenarios already listed, in order to display the usefulness and functionality of the system in everyday life.

---

###### Scenario 1: Home Security /w Burglary
In this scenario, a user has set up the Raspberry Pi and camera by their living room window. Once they leave the home, a notifciation will be received on their phone and upon accepting, the system will be armed. 
During the time period in which the user is away from home, while there is no motion being detected, the camera simply takes a photo, compares against the last, and discards it. 
When there is motion detected however, the speed of the image capuring picks up substantially and the user receives a notification on their mobile device. Once they open the application, they will see which camera the notification is coming from, and will open the dropbox folder for that camera in order to investigate. Here, they will see live images from in real time being uploaded of an intruder in their home, as he enters through the window.
From this position, the user will have the option to save the images, make a phonecall(contact the police, for example), or open up a live video feed connection with the camera. The user touches the button to call the police and are immediately connected so that they can explain their situation, and can provide all the footage necessary to use as evidence against the intruder.

---

###### Scenario 2: Monitor a Sleeping Baby
In this example, a parent has set up a camera in the room of their young baby in order to give them time to do necessary chores around the house while the baby sleeps, and be notified if the baby wakes up.
Upon receiving a notification on their mobile phone, they open the app and move into the camera in question. From here, they can see that the baby has moved but it appears to be still asleep. To investigate further, the parent opens a live video stream with the camera and finds that the baby is in fact awake and unsettled. From here, the parent triggers the alarm (which in this case is actually a soothing lullaby melody), and can now drop what they were doing in order to tend to the baby's needs. 

---

###### Scenario 3: Monitoring Front Door
Here, the camera has been set up in order to give a view of the outside of the home, namely the front door entrance. The user's phone receives a motion notification (and the door knocks..). The user opens up the camera's folder to investigate. If in this case, the camera shows somebody who is not welcome and/or not expected, the user is now aware without having to expose their presence. In this case however, the user sees a delivery man that had been expected. The user is busy however and as a result, open's up a skype connection and notifies the delivery man that he/she is on the way and will answer the door shortly. 

---

###### Scenario 4: Monitoring Elderly Relative/Care Subject
In this scenario, the user is taking care of their elderly grandmother who is currently sleeping in the living room (where the surveillance camera has been set-up). The user takes a trip to the shop in order to buy groceries for the evening, however on the way a notification is sent to the user's phone and upon investigation, the user finds that their grandparent has fallen and is in need of assistance. The user can now open a skype connection with the camera, comfort and direct the elderly subject, and begin to make their way home immediately in order to assist. 

## 2.5 Constraints
One unfortunate constraint is related to the type of camera that can be used in conjunction with the system. The raspberry pi official camera module is the most obvious choice and can be disguised very well. Although many other cameras can be used, they are, naturally enough, limited to the cameras that are compatible with the Raspberry Pi. There is a list of such cameras on the official Raspberry Pi website. Although the set of all compatible cameras is not limited to this list, these are the only cameras guaranteed to be supported by the Pi, and as such are a safe bet. 

Another constraint to be considered is the number of cameras. The number of cameras on this system will be limited to a maximum of 4, in order to avoid slowing down the system or risking IO problems related to the uploading and comparing of images.

The alarm on this system will be limited to the one output device, rather than being individual to each camera. The reason for this is that multiple cameras will be connected to the one Raspberry Pi, which will also house the audio output system. The same is true for the ability for communicating with the other side via skype as communication would be achieved via this same audio device.
* * * 


# **3. System Requirements**
## 3.1 External Requirements
**Hardware**: For the user to set up this surveillance system at home, a raspberry pi, and at least one camera is required (ideally the raspberry pi official camera module). As well as this, an android device is required to make use of the mobile application. 

**Software**: This system required the Raspberry Pi to be running a version of Raspbeon OS. As well as this, it is required that the user’s phone is running android (preferably 6.0(+) Marshmallow). 

**Environment**: In order for the system to send real-time notifications to the user’s mobile phone, it is necessary that the user has some sort of internet connection outside of the home, in which they are connected to through their android device.
A home internet connection (or wherever the system is stationed), naturally, is also required.
A drop box account is also required as this is where the system will store images, and therefore is the medium through which users will be capable of viewing images from the camera in question once the system has detected motion.


## 3.2 Product Functions
###### The Application 
The system will provide the application downloadable from the google play store, which will feature a main page of all available cameras that the user has set up in a tile format. These cameras will only be activated once there has been motion detected at which point, a notification would be sent to the user’s phone, the app would be opened, and the user will find the corresponding tile highlighted. From here the user would have a multitude of options such as viewing the live feed from the camera, opening the folder where images are being stored in order to view what the motion was, deactivate the alarm triggered by the motion, and also send a text message to authorities in order to alert them of the disturbance if the user feels such action is necessary based on the images they see have been captured. The application would also be connected to the user’s drop box account, so that images being uploaded in real time by the system can be viewed regardless of where the user is. 


###### Motion Detection 
This system is built around the concept of capturing images only when there has been motion detected. In order to achieve this, photographs will be taken every X number of seconds, where X is a predetermined variable decided by the user. These images will be stored and compared against the previous image. If the previous image differs in any way, the camera will begin to take photographs more rapidly and store them, as well as notifying the above-mentioned application. For all the images that are no different to the previous, the new image will simply be discarded. This means the system only needs to store new images when there is a change in scenery. 

###### Image Storing
As well as showing real time images to the user via the mobile application, the system will also store each image which differs from the previous. These images will be stored on drop box in order for the user to be able to access and download/save the images. 

###### Multiple Alarm Triggers
The system will provide two mediums through which the alarm sound may be triggered: a magnetic motion sensor which can be put on doors or windows, and the motion detection software of the camera(s). The alarm can be triggered through both methods. The magnetic sensor is limited to being in the same room as the Raspberry Pi unlike a camera, as it is connected directly to the device. Although not the main security feature of this system, it could be a useful extra precaution for the user, depending on their needs.

###### GPS Tracking & Automatic Arming
The system will also make use of the GPS feature on one’s android device which will allow it to detect whether the user is at home at any given time. The user can configure the system to automatically arm itself when the GPS on their mobile phone knows that they are out of range of the house. The application will automatically send the users phone a notification with the option to arm the system (if it is turned on at home). The user can then choose to accept or deny the offer. If the former is chosen, the user will be brought to the configuration page of the application in order to choose from the preferences/settings mentioned earlier, as well as which cameras they want to actually arm.

###### One-Click responses
If the user finds that the images being captured by one of the cameras are suspicious, and they are not near home to investigate themselves, the app will also have the option to call numbers that the user will define as neighbours, with the simple click of a button. If, for example the images lead the user to believe there is a burglary underway, the user can also tap a single button to call emergency services. The system also features an option to tell the camera to stop waiting for motion, or to stop an alarm that has been triggered.

###### Multiple Alarm Sounds
One important feature of the system is the ability to change the sound clip being played when the alarm is triggered. This is useful for when the alarm is triggered on a camera that is not set up for a security function, but rather to keep watch on a sleeping child for example. In this situation, the alarm could be changed to something akin to a lullaby melody while the user makes his/her way to attend to the child’s needs.  

###### User Preferences
The user would also be given the option to change the settings and preferences of certain cameras. For example, the user can change a camera from a “security” styled setup to a monitoring setup, in which the user would decrease the sensitivity of motion captured by the camera to monitor say, a sleeping baby. The user would not want to capture small movements in sleep, but only larger more obvious movements that would be indicative of a child waking.

 

## 3.3 Usability Goals
###### Scalability
Using multiple cameras on this system is a very important feature, so the user can monitor more than a single area at any one time. The obvious concern here is the trade-off between more cameras and the additional overhead in terms of bandwidth and storage. Thus, while currently the system is planned to cap it at four cameras at any one time, in the future an obvious area of expansion to consider is expanding this limit. 

###### Performance
As this system relies heavily on the user’s internet connection, this will likely be the first limiting factor in terms of the performance of this system. Another factor could be the number of cameras attached to the system which is why, as explained above, the camera limit has been set to four at any one time. Performance will have a strong correlation with the scalability mentioned above. Naturally, if all four cameras have detected motion at the same time, and are all updating their respective drop box folders performance will suffer. However, again, with the cap of four cameras it shouldn’t be a concern.
However, assuming an adequate internet connection, and with the restriction on the number of cameras, the performance of this system should be suitable. This will be achieved by the fact that the camera is neither constantly streaming video, or even constantly storing images, so memory should never be a concern. At any one time, while there is no motion detected, the system should only ever have at most two images stored, one of which will be deleted in absence of motion. 

###### Portability
A big advantage of this system over similar security/surveillance systems is how easy it is to move around. If a user wishes to set up the system in a different location, all that is required is to move a very small raspberry Pi and any cameras necessary for their particular goals. 


###### Setup
It is important this system is easy for any prospective user to set up, and not just those familiar with the Raspberry Pi computer, or technology in general. To achieve this, a simple instruction section will be included in the application for the user to reference if there are ever any confusions in regards to how to use the system. This will include features such as a FAQ section, and step by step guides using images/screen shots of the set-up process. However, there will not be much involved in this process as the Raspberry Pi will be set up in advance to run the security system on start-up, so powering on the system should be all that is required.  
 
###### Maintenance
This system will require almost no maintenance from the user, other than managing drop box folders, removing no longer required images, etc. On behalf of the team, as each segment is broken down into separate distinct classes in code, this should make it easier for any problems that do occur in the future to be identified and fixed immediately. 

###### Security
The primary security concerns for this system are related to dropbox and the integrity of one’s drop box folders where images will be uploaded. This should not be a problem considering drop box files are encrypted using 256-bit Advanced Encryption Standard (AES), Secure Sockets Layer(SSL) and Transport Layer Security (TLS) to, according to their website, “ protect data in transit between Dropbox apps and our servers; it's designed to create a secure tunnel protected by 128-bit or higher Advanced Encryption Standard (AES) encryption.” 
This, in combination with the fact that regular tests are carried out by drop box for security vulnerabilities to protect against attacks, will ensure that user’s images are suitably safe while they are stored online. 
Finally, the application will require the user to grant Dropbox access via the Authorize button in the user’s Dropbox account, adding an extra layer of security. Instructions on exactly how to set this up will be included in the application as mentioned above.

###### User Interface
The UI for this system should be sleek, minimalistic, and clean while still providing the user with all the functionality mentioned above. It is important that the application is not overly cluttered or complex looking as an integral aspect of the advantage of this system over others in the field is the fact that it is accessible both in price and usability, to all home owners, as specified above. 
* * * 



# **4. System Architecture**

## 4.1 System Module/Component Diagram
![](file://C:/Users/Ross/Desktop/Capture.PNG)

## 4.2 Interaction Diagram Explanation 
- Here, the **Local application logic** section covers all of the interactions at a high level between each physical component and the application itself.
The Android application will interact with the GPS in order to satisfy the Automatic arming function. The application will also interact with the Backend system module in order to access the components in that respective section.
Also in the local application, the Raspberry pi will receive data from the camera module and motion sensor in order to determine whether or not images are to be stored. As well as this the VNC server will be set up to view the Raspberry Pi's live feed. 

- The **BackEnd application logic** section represents the different relationships between the more low level components of the system. Here the backend system component acts as a link between the Local and Backend sections. 
The user settings module will allow the user to change preferences in the application itself (as per the functional requirement described above), in the profile management option. 
As well as this, the Authentication module will require the user to sign into their Skype and Dropbox accounts in order to gain full access to the functionality of the system. The notifications to the user's phone will be handled by the Android notification system, which will allow the user's phone ot be notified at any time, and not just when the application is open.

- Finally, the **External services** section refers to the 3rd party components used in this system. 

