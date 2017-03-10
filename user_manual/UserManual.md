Contents
========================

1 - Raspberry Pi Set – up......................................................2

- Initialize the script and settings...........................................2

2 - Android Application........................................................3

2.1 - Set-up Step 1............................................................3

2.2 - Set-up Step 2............................................................3

2.3 - Setting up a neighbor &quot;Quick-Text&quot; number......................4

2.4 - Arming and Disarming the camera..........................................5

2.5 - Changing the Alarm sound.................................................5

2.6 - Enabling Notifications...................................................6

2.6.1 - Reacting to notifications..............................................6

2.7 - Viewing Captured Files...................................................6

2.7.1 - Deleting Files.........................................................7

Call 999.......................................................................7

1 - Raspberry Pi Set – up
=========================

Initialize the script and settings
----------------------------------

Before running the script the user should ensure all settings are as they desire by opening the “conf.json” file with a text editor. Here the user can choose to enable/disable a live camera feed of the webcam, SMS notifications, automatic authentication and sound functionality. All will be disabled by default. They can also adjust the resolution of the image frame, how often they receive notifications to the app, the fps, and technical configurations like the delta threshold, and how many consecutive frames must be detected before an image is uploaded. This is also where the user supplies their SMS credentials, i.e their phone number, email address, and account hash token. The user must register with “TextLocal” to avail of this service.

The first time the script is run, the user is required to log in to their google account to gain authentication credentials, which will register an authentication key with google services. This key is then stored and check for each time the python program runs, in order to allow the Pi to access their google account. SSH is enabled on the Raspberry Pi, which is accessed through port 22 by default, and as such, requires that port 22 is open on the router from which the system operates. The default username for the Pi is Pi, while the password is Raspberry, though these can be changed by the user if necessary. It is these login details that are used to establish the SSH connection.

Once this is complete, from now on, all that is required is to plug in the Raspberry Pi to a power source, ensure they are connected to the internet, ensure that a camera and speaker is attached (any cheap webcam and portable speaker will do), and download the SecuriPi application.

2 - Android Application
=======================

Installation Guides for the application can be seen in the accompanying technical manual.

### 2.1 - Set-up Step 1: 

The first thing that should be done upon download is logging in to one’s Google account, or creating a new account in order to view files uploaded by the Raspberry Pi system. This will be used in future to log in to the application (which will be handled automatically if an account is already logged in on the android device), and to view the google drive folder.

![](https://gyazo.com/716babda77f0a2fe66ec3a66e3d49237.png)

Successful log in is signaled by the changing of the “welcome” message to “welcome back”.

![](https://gyazo.com/f431c7c3213659670b4c3fdc4ed2b03d.png)

### 2.2 - Set-up Step 2:

The next thing to do is ensure that the application can connect to the Raspberry Pi. To do this, open the options menu in the top right corner of the application, and select “Connect Via SSH”.

Now, enter the IP Address that was taken note of earlier in the Raspberry Pi set up guide, and click

![](https://gyazo.com/3d33cfb0ee91e7df7814bdcad4b50508.png)

Note: Please ensure the Raspberry Pi is powered on and connected to the internet before attempting this step.

### 2.3 - Setting up a neighbor “Quick-Text” number:

![](https://gyazo.com/c1d20c2b2a5feeaa0eeffae69f3ffd47.png)

The next step is to set up a number for your neighbor, to avail of the quick text feature. To do this, open the options menu again, and choose the “Set neighbor number” option. From here, simply enter the number of the neighbor that should be notified when the motion detection has been triggered, and hit save. Now, when the “Quick Text” button is tapped, a pre-written message will be set up, with the recipient set to that particular number.

![](https://gyazo.com/ac4853de6622c94d29cab1e416fa19be.png)

Now that the application is configured to interact with the raspberry pi, the core functionality of the app is unlocked.

### 2.4 - Arming and Disarming the camera:

To start the
camera’s motion detection capabilities, simply hit the “start camera”
button in the bottom left of the application’s main screen. This will
result in a message which will inform the user that the camera has been
armed.

![](https://gyazo.com/08069b18767c88993a381f2926f8d383.png)

To stop the camera’s motion detection, or to shut down the alarm in the
event of detection that has already been deemed to be of no interest or
a false flag, simply tap the “stop camera” button. The camera can be
restarted at any time.

### 

### 2.5 - Changing the Alarm sound:

To change the type of alarm that will be sounded when motion is detected on the SecuriPi system, open the menu and choose the “Change Alarm Sound” option. This will make accessible a dropdown menu with four options to choose from. 

![](https://gyazo.com/1109c7f86a851257b27550325fee061f.png)

The default alarm is the general, all-purpose standard alarm. If the monitored area is outside, the barking dog sound may be more suitable. In the case of monitoring a child, the lullaby option may be selected. Finally, if the camera is being used to monitor an area for security/anti-theft reasons, a police siren can be chosen. 

### 2.6 - Enabling Notifications:

In order to ensure that the device will receive notifications when motion has been detected, make sure the Notification button is set to on as can be seen here: 
![](https://gyazo.com/185153dcdcbc24f725bbbe9ea843aeee.png)

To turn notifications off but continue capturing images when motion is detected, set the button to off:

![](https://gyazo.com/599a42ed95122cdda7c2972ba650bca6.png)

### 

### 2.6.1 - Reacting to notifications:

When a notification is received, it will appear in the dropdown tray at the top of the mobile device’s screen. Simply tap this notification to be brought to the SecuriPi App, or navigate back to it if it is already opened.

### 2.7 - Viewing Captured Files:

As well as a notification, the device will also receive an SMS message with a link to the image that triggered the motion detection. To view the image, one option is to simply click the link provided in the SMS from SecuriPi.

Alternatively, go to the application and tap the “Open Images”.

![](https://gyazo.com/8ef123da68a412da6e2a3ee6659c0146.png)

From here, an option to open in browser or open drive will appear. The drive option will connect to Google drive from within the SecuriPi app and allow for images to be expanded in the main screen of the app, to be inspected more thoroughly

![](https://gyazo.com/d4b80f4b54eaefb04cb234942932b474.png)

The browser option will open Google Drive in the default mobile internet browser, however if the google drive app is installed on the device, this will be opened, to get a thumbnail view of all images uploaded to drive.

### 2.7.1 - Deleting Files:

Files can be deleted through either of the two pathways mentioned above to view the files. Through the menu -> delete images option, Images can be selected just as they are for being opened in-app, and deleted. If the image being displayed on the main screen is deleted, it will be cleared. 
Alternatively, images can be deleted from the browser/app link mentioned above. 


### Call 999:

This button is only to be used in emergencies. If the system has captured evidence of a potential intrusion or criminal act, this button acts as a quick and easy method to call emergency services and report the incident. Photographic evidence has been gathered automatically by the SecuriPi system which may prove useful in identifying the perpetrator or resolving the legal incident.

![](https://gyazo.com/fc6c91582c6aa1052c37432eec5f4f6b.png)

Note from Developers
====================

*The SecuriPi development team would like to thank you for choosing our
system, and are confident that the time spent with SecuriPi home
surveillance system will be an enjoyable one.*

*Regards.*

*-Team SecuriPi*
