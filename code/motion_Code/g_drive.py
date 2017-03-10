from pyimagesearch.tempimage import TempImage
from pydrive.auth import GoogleAuth
from pydrive.drive import GoogleDrive
import conf

import datetime

conf = conf.config()

#GoogleDrive Authentication
gauth = GoogleAuth()
drive = GoogleDrive(gauth)

def get_gauth():
    return gauth
def get_drive():
    return drive

def authentication():
    if conf["auto_auth"]:
            #Try to load saved client credentials
            gauth.LoadCredentialsFile("mycreds.txt")
            if gauth.credentials is None:
                    gauth.LocalWebserverAuth()
            elif gauth.access_token_expired:
                    gauth.Refresh()
            else:
                    gauth.Authorize()
            gauth.SaveCredentialsFile("mycreds.txt")
    else:
            gauth.LocalWebserverAuth()

#Drive Upload
temp = TempImage()
global timestamp
timestamp = datetime.datetime.utcnow()
ts = timestamp.strftime("%A %d %B %Y %I:%M:%S%p")
            
def get_ti():
    return temp

def uploader(ts):
    file = drive.CreateFile()
    file.SetContentFile(temp.path)
    file['title'] = ts
    file.Upload()
    global file_id
    file_id = file.get('id')
    print('Created file%s with mimeType %s' % (file['title'], file['mimeType']))

def get_file_id():
    return file_id
