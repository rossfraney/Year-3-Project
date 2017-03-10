#!/usr/bin/python

# Import required libraries
import urllib      # URL functions
import urllib2     # URL functions
import g_drive
import conf

conf = conf.config()

# Sender name must alphanumeric and between 3 and 11 characters in length.
email = conf["sms_email"]
sender = 'SecuriPi'

# Your unique hash is available from the docs page https://control.txtlocal.co.uk/docs/
hash = conf["sms_hash"]

# Set flag to 1 to simulate sending
# This saves your credits while you are testing your code.
# To send real message set this flag to 0
test_flag = 1

def sms(phoneNumber):
  image_content_link = "https://drive.google.com/open?id="+g_drive.get_file_id()
  message = image_content_link

  values = {'test'    : test_flag,
            'uname'   : email,
            'hash'    : hash,
            'message' : message,
            'from'    : sender,
            'selectednums' : phoneNumber }

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
