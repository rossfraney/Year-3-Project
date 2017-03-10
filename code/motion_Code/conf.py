import json
import argparse

def config():
    ap = argparse.ArgumentParser()
    ap.add_argument("-c", "--conf", required=True, help="path to the JSON configuration file")
    args = vars(ap.parse_args())
    conf = json.load(open(args["conf"]))
    return conf
