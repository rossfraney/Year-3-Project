from pyfcm import FCMNotification
import g_drive

# Notifications to the App

def notify():
    #image_content_link = "https://drive.google.com/open?id="+g_drive.get_file_id()
    #print image_content_link
    #data_message = {
    #    "image" : image_content_link
    #}

    push_service = FCMNotification(api_key="AAAAsxrS_UY:APA91bGuW6Ir3I7V4s_p2g6ToQZJO-6XPG-obxiLB2zGnr88mqxa_hkccMJPVJTvjZoRnYYJAtM10oouUjK5T54hN75F33FZdq6eUNMP9GAF3_KV2i-E_e64IzoYSM4shE4GJ-8M4UKV")
    #registration_id ="fZR8lj0Quyg:APA91bH2tKhEfTfz7i8EFq3rTuiZI5dCx9ordwXGdovsjdOy8k4l7kDF99V9dHcLc6BOD8Vf852Z6GQuXfAuzgID3A7N0g-Iat1G8GNJue5bIp0FxX4V5vfjTnXI9PwKwulT-pdqS3_q"
    registration_id = "e-p9EFXIwiM:APA91bEzEhSlcCSvGW_wkt9kuha_74WBreyrVh7hEaR4E0xXhA2lcJT-498D-aSd4awxxbKJXgW9O6wldXe1X72HlVNGrrHyYa9FN4R6-dLG9OD0hLaQ5F8cEL4gwvFWM5METo4sUPTo"
    message_title = "Motion Detected!"
    message_body = "Please open your securiPi app."

    result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)
    #print to test
    print "print print print"
    #print g_drive.get_file_id()


    return result
