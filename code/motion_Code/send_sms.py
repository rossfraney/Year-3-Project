import urllib      # URL functions
import urllib2     # URL functions
import g_drive
import conf

conf = conf.config()

# initialise email address of user from conf.json
email = conf["sms_email"]

# sender name must be between 3 and 11 characters.
sender = 'SecuriPi'

# get your accounts unique hash from https://control.txtlocal.co.uk/docs/
# initialise from conf.json
hash = conf["sms_hash"]

# set flag to 1 to simulate sending, 0 for real texts
test_flag = 1

# send the SMS
def sms(phoneNumber):
  # get the link to the image uploaded, to be sent as the text message
  image_content_link = "https://drive.google.com/open?id="+g_drive.get_file_id()
  message = image_content_link

  values = {'test'    : test_flag,
            'uname'   : email,
            'hash'    : hash,
            'message' : message,
            'from'    : sender,
            'selectednums' : phoneNumber }

  # 3rd party enabled text service
  url = 'http://www.txtlocal.com/sendsmspost.php'

  postdata = urllib.urlencode(values)
  req = urllib2.Request(url, postdata)

  print 'Sending SMS'

  try:
    response = urllib2.urlopen(req)
    response_url = response.geturl()
    if response_url==url:
      print 'SMS sent!'
  except urllib2.URLError, e:
    print 'Send failed!'
    print e.reason
