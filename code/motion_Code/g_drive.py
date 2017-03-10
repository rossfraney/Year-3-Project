from pyimagesearch.tempimage import TempImage
from pydrive.auth import GoogleAuth
from pydrive.drive import GoogleDrive
import datetime
import conf

# allows use of conf.json settings
conf = conf.config()

# initialise timestamp and temp image
global timestamp
temp = TempImage()
timestamp = datetime.datetime.utcnow()
ts = timestamp.strftime("%A %d %B %Y %I:%M:%S%p")

#GoogleDrive Authentication
gauth = GoogleAuth()
drive = GoogleDrive(gauth)

def get_gauth():
    return gauth

def get_drive():
    return drive

def get_ti():
    return temp

def authentication():
    # if user selects automatic authentication
    if conf["auto_auth"]:
            #Try to load saved client credentials
            gauth.LoadCredentialsFile("mycreds.txt")

            # authenticate through browser if first time running script
            if gauth.credentials is None:
                    gauth.LocalWebserverAuth()

            # refresh the access token if it has expired        
            elif gauth.access_token_expired:
                    gauth.Refresh()

            # authorize the user
            else:
                    gauth.Authorize()
                    
            # save user credentials for next run       
            gauth.SaveCredentialsFile("mycreds.txt")
            
    # if user doesn't want their credentials saving
    else:
            gauth.LocalWebserverAuth()

#Drive Upload         
def uploader(ts):
    global file_id

    file = drive.CreateFile()

    # set the contents of the file to be uploaded
    file.SetContentFile(temp.path)

    # edit the filename to the correct timestamp
    file['title'] = ts

    # upload file
    file.Upload()
    print('Created file %s with mimeType %s' % (file['title'], file['mimeType']))

    # initialise file id to be used for sending link via SMS notification
    file_id = file.get('id')

def get_file_id():
    return file_id
