import json
import argparse

def config():
    # allows use of conf.json from command line argument
    ap = argparse.ArgumentParser()

    # argument for command line. i.e python file.py "--conf conf.json"
    ap.add_argument("--conf", required=True)
    args = vars(ap.parse_args())
    
    # allows conf to be loaded into files
    conf = json.load(open(args["conf"]))
    return conf
