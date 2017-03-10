from pyfcm import FCMNotification
import g_drive

# Notifications to the App via firebase server
def notify():
    # api key to allow notifications to be pushed to the firebase server
    push_service = FCMNotification(api_key="AAAAsxrS_UY:APA91bGuW6Ir3I7V4s_p2g6ToQZJO-6XPG-obxiLB2zGnr88mqxa_hkccMJPVJTvjZoRnYYJAtM10oouUjK5T54hN75F33FZdq6eUNMP9GAF3_KV2i-E_e64IzoYSM4shE4GJ-8M4UKV")

    registration_id = "e-p9EFXIwiM:APA91bEzEhSlcCSvGW_wkt9kuha_74WBreyrVh7hEaR4E0xXhA2lcJT-498D-aSd4awxxbKJXgW9O6wldXe1X72HlVNGrrHyYa9FN4R6-dLG9OD0hLaQ5F8cEL4gwvFWM5METo4sUPTo"

    # main title on notification
    message_title = "Motion Detected!"

    # main message of notification
    message_body = "Please open your securiPi app."

    # result is to be pushed
    result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)

    return result
