#client_secrets.json from your Drive API needed in same directory
#Get this from the credentials part of your API project
#pypi.python.org/pypi/PyDrive (see documentation to implement properly)

from pydrive.auth import GoogleAuth
from pydrive.drive import GoogleDrive

gauth = GoogleAuth()
gauth.LocalWebserverAuth()

drive = GoogleDrive(gauth)


file1 = drive.CreateFile({'title': 'Hello.txt'})
file1.SetContentString('HelloWorld!')
file1.Upload()

file2 = drive.CreateFile()
file2.SetContentFile('image.jpg')
file2.Upload()
print('Created file%s with mimeType %s' % (file2['title'],
file2['mimeType']))

http = drive.auth.Get_Http_Object()

file_obj = drive.CreateFile()
file_obj['title'] = "file name"


file_obj.Upload(param={"http": http})