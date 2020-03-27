#!/usr/bin/env python
# -*- coding: utf-8 -*-

import getopt, sys
import optparse
import psycopg2
import shlex, subprocess
import time
from random import randint
import json

connstr_pipedb = "host=localhost port=5433 dbname={0}"


def main(argv):

    parser = optparse.OptionParser()
    parser.add_option('--db', action="store", dest="namedb", default="ldm_2")
    parser.add_option('--sleep', action="store", dest="sleep", default=0.01) # 500ms
    parser.add_option('--start', action="store", dest="start", default=0)
    parser.add_option('--stop', action="store", dest="stop", default=500000)
    parser.add_option('--init', action="store", dest="init", default=0)
    parser.add_option('--file', action="store", dest="tracefile", default="")
    parser.add_option('--out', action="store", dest="out", default=0)

    (options, args) = parser.parse_args()

    trace_file = options.tracefile

    debug = False
    if(int(options.out) > 0):
    	debug = True


    if not debug:
    	# Connection string
    	conn_1 =  connstr_pipedb.format(options.namedb)
    	# Connect to a PostGIS database and open a cursor to perform database operations
    	conn_pipedb = psycopg2.connect(conn_1)
    	# Set DB parameter
    	conn_pipedb.set_client_encoding('UTF8')

    	cur1 = conn_pipedb.cursor()


    # Value ranges
    val_stop = int(options.stop)
    val_start = int(options.start)
    val_sleep = float(options.sleep)
    start_ids = 300


    if int(options.init) > 0 and not debug:
    	cur1.execute("TRUNCATE CONTINUOUS VIEW v_pos;")
    	cur1.execute("TRUNCATE CONTINUOUS VIEW v_signalstate;")
    	cur1.execute("TRUNCATE CONTINUOUS VIEW v_speed;")
    	conn_pipedb.commit()

    sqL_insert = "INSERT INTO {0} VALUES ({1},{2});"
    sqL_insert2 = "INSERT INTO {0} VALUES ({1},'{2}');"
    count = 0

    count_vehicles = 0

    # Open JSON file

    json_file = open(trace_file)
    json_str = json_file.read()
    json_data = json.loads(json_str)

    #for json_ in vehicles:
    json_traces = json_data['traces']

    print("Start: " + str(len(json_traces)))

    # traffic light changes every 4 steps
    tlChange = 4 # 4 # (2 / (val_sleep * 80))
    tlCount = tlChange
    print("TL Change Frequency: " + str(tlChange))

    #print "Start Movement"

    for json_trace in json_traces:

    	count += 1
    	ind_id = 0

    	if count < val_start:
    		continue

    	start = time.time()

    	time.sleep(val_sleep)

    	rows_speed = []
    	rows_pos = []

    	json_step = int(json_trace["step"])
    	json_vehicles = json_trace["vehicles"] #

    	print("Round: " + str(json_step) + " / " + str(len(json_vehicles)))  # count


    	for json_veh in json_vehicles:
    		ind_id = int(json_veh["VehicleID"])
    		veh_type = int(json_veh["VehicleType"])
    		mv_speed = json_veh["Speed"]
    		mv_long =  json_veh["Position_X"]
    		mv_lat =  json_veh["Position_Y"]
    		mv_heading = json_veh["Orient_Heading"]

    		if ind_id==0:
    			continue

    		veh_type_str = "car_"
    		#if veh_type == 300:
    		#	veh_type_str = "bus_"

    		#print ind_id, mv_speed, mv_long, mv_lat

    		ind_point = "" + str(mv_long) + " " +  str(mv_lat)
    		rows_speed.append({'iid': ind_id, 'x': mv_speed})
    		rows_pos.append({'iid': ind_id, 'x': ind_point})

    		if debug:
    			print("speed(" + veh_type_str + str(ind_id) + ",\"" + str(mv_speed) + "\"," + str(json_step) + ").")
    			print("pos(" + veh_type_str + str(ind_id) + ",\"POINT(" + ind_point + ")\"," + str(json_step) + ").")
    			print("heading(" + veh_type_str + str(ind_id) + ",\"" + str(mv_heading) + "\"," + str(json_step) + ").")


    	if not debug:
    		cur1.executemany('INSERT INTO stream_speed (iid, x) VALUES (%(iid)s,%(x)s)', rows_speed)
    		cur1.executemany('INSERT INTO stream_pos (iid, x) VALUES (%(iid)s,%(x)s)', rows_pos)


    	rows_sig = []

    	json_signals= json_trace["signals"]

    	if tlCount >= tlChange:
    		tlCount = 0

    		for json_sig in json_signals:
    			sig_id1 = int(json_sig["ControllerId"])  # "_" +
    			sig_id2 = int(json_sig["SignalGroupID"]) # "_" +

    			if sig_id1 == 0:
    				continue

    			sig_id_nr = 0
    			sig_id = str(sig_id1) + str(sig_id2)
    			sig_state = int(json_sig["SignalState"])

    			# Convert CtrlID and SignalGroup to internal ids
    			if sig_id == "11":
    				sig_id_nr = 36
    			elif sig_id == "12":
    				sig_id_nr = 37
    			elif sig_id == "13":
    				sig_id_nr = 38
    			elif sig_id == "14":
    				sig_id_nr = 39
    			elif sig_id == "111":
    				sig_id_nr = 86
    			elif sig_id == "112":
    				sig_id_nr = 87
    			elif sig_id == "113":
    				sig_id_nr = 88
    			elif sig_id == "114":
    				sig_id_nr = 89
    			elif sig_id == "1011":
    				sig_id_nr = 136
    			elif sig_id == "1012":
    				sig_id_nr = 137
    			elif sig_id == "1013":
    				sig_id_nr = 138
    			elif sig_id == "1014":
    				sig_id_nr = 139
    			elif sig_id == "10011":
    				sig_id_nr = 186
    			elif sig_id == "10012":
    				sig_id_nr = 187
    			elif sig_id == "10013":
    				sig_id_nr = 188
    			elif sig_id == "10014":
    				sig_id_nr = 189

    			sig_state_txt = "0" # "R"
    			# Red+Amber, Green, Amber, Off, Flashing Green
    			if sig_state == 2 or sig_state == 3 or sig_state == 4 or sig_state == 5 or sig_state == 9:
    				sig_state_txt = "1" # "G"

    			if sig_id_nr > 0:
    				rows_sig.append({'iid': sig_id_nr, 'x': sig_state_txt})

    				if debug:
    					print("signalState(" + str(sig_id_nr) + "," + sig_state_txt + "," + str(json_step) + ").")

    		if not debug:
    			cur1.executemany('INSERT INTO stream_signalState (iid, x) VALUES (%(iid)s,%(x)s)', rows_sig)

    	tlCount += 1


    end = time.time()
    print("Finished: " + str((end - start) * 1000 ))



if __name__ == "__main__":
    import sys
    main(sys.argv[1:])
