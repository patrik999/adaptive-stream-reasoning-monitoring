# Foundations and Resources

* PipelineDB is used as an interface for streams
  see https://www.pipelinedb.com/

* Patrik's work on Siemens Traffic Data is used as DB schema
	see http://www.kr.tuwien.ac.at/research/projects/loctrafflog/iswc2018/

# What we do here

* stream Vissim data into pipelinedb
* use hexlite as a library to implement a stream reasoner
	- stream reasoner periodically checks if traffic jam started
	- stream reasoner periodically provides information about traffic amounts

# [DO ONCE] Setup

* PipelineDB Build

	see http://docs.pipelinedb.com/installation.html

* PipelineDB Init

  $ . environment.sh
	$ pipeline-init -D `pwd`/datadir/

* Hexlite/Acthex Setup

	- make sure you have also cloned and checked out the submodule

	$ git submodule init
	$ git submodule update 

	Now in directory engine/ there should be a checkout of the complete public hexlite.

	- Install miniconda 
	- Create conda environment with python 3 and potassco clingo

	$ conda create -n py3clingo python=3
	$ conda activate py3clingo
	$ conda install shapely
	$ conda install -c potassco clingo

	- Test python: the following command line

	$ python -V

	should give a result similar to
	Python 3.6.4 :: Anaconda, Inc.

	- Test clingo:

	$ python
	> import clingo
	>

	should give no error

	- setup hexlite in development mode

  $ cd engine/
	$ python setup.py develop --user
	IMPORTANT: do not use python2 and do not use python3! Just use "python" so that conda can do its magic.

	- put ~/.local/bin into PATH
	- Test hexlite/acthex

	$ cd engine/tests
	$ ./run-tests.sh
	$ python run-acthex-tests.py

# [DO ALWAYS] PipelineDB Startup

$ pipelinedb -D `pwd`/datadir/
if this produces a "file name too long" issue:
	$ ln -s `pwd`/datadir/ /tmp/datadir
	$ pipelinedb -D /tmp/datadir/

activate/test via
$ psql -h localhost -p 5432 -d pipeline -c "show all"

# [DO ONCE] PipelineDB Schema Init

* Experiment with Patrik's work from dump obtained from http://www.kr.tuwien.ac.at/research/projects/loctrafflog/iswc2018/

* The last dump file is pipeline_dump/pipeline_ldm2_20200327.bak which already has data of vehicles loaded. 

	$ psql -h localhost -p 5432 -d pipeline -c "CREATE DATABASE ldm_2;"
	$ pipeline -d ldm_2 -f pipeline_dump/pipeline_ldm2_20200327.bak
	(ignore errors about role "patrik", we run pipelinedb locally, we are always root)

# Running Hexlite-based stream reasoner


* We need two components to run acthex as a stream reasoner (SR):

  - stream-reasoning-plugin: Is a set of Python functions that extend acthex with DB access, websocket client, and auxilliary functions such a timestamping, printline, etc.
  
  -  encoding_stream_ws.hex: The actual acthex programm that encodes the SR
  
 Important: The websockets server URL that is used by the client is set in the programm, i.e., server_adr("ws://localhost:8082"). 
  
 More programms for other tasks will follow!

* Run on shell: acthex --flpcheck=none --pluginpath=./stream-reasoner/plugins/ --plugin=stream-reasoning-plugin ./stream-reasoner/pgms/encoding_stream_ws.hex


# Streaming Data into PipelineDB

* script to run stream data generation:

python stream_play_vissim_dyc.py --out 0 --file feeder/Vissim_Trace_T1.json --sleep 0.1

--out 0 (writes to DB and not stdout), --sleep 0.1 (100ms delay between inserts)

* data in directory feeder/
	- Vissim_Trace_T1.json in Vissim_Trace_T1.json.bz2

# Testing Websockets

ws_client.py, resp., ws_server.py are simple testing tools that act as a 
Websocket client, resp., Websocket server


# Stream Reasoner Design

* Syntax
	- execution is triggered via events like in ASAP framework with &event() external atom
	- language is acthex with externals and actions
	- external atom &sqlN[query](X_1,...,X_N) to get results of queries on pipelinedb
	- action #timer(name,milliseconds) to trigger next event

* SQL Queries used in &sql externals:
	- Query latest unique car positions/speeds
	  of cars seen in last window of interest (1 sec)

		SELECT iid, speed, pos FROM window_car
		GROUP BY iid, speed, pos, arrival_timestamp
		HAVING (iid, arrival_timestamp) IN
			(SELECT iid, max(arrival_timestamp) FROM window_car GROUP BY iid)
		ORDER BY iid;

