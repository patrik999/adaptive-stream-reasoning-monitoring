# this is the acthex addon for stream reasoning with the hexlite acthex engine

import acthex

# postgresql
import psycopg2

# 2d shape manipulation
import shapely
import shapely.wkt

import logging, sys, re, time, datetime

import websocket

#import asyncio
import threading

#try:
#    import thread
#except ImportError:
#    import _thread as thread



########################################################
# persistence environment, actions, and external atoms #
########################################################
#
# environment = set of tuples of strings (=atoms), initialized with single atom ('init',)
# @persistenceSet(atom) -> remembers atom in environment
# @persistenceUnset(atom) -> forgets atom in environment
# &persistenceByPred[pred](atom) -> true for all atoms with predicate pred that are remembered in environment
#
class PersistenceEnvironment(acthex.Environment):
	def __init__(self):
		self.atoms = set([ ('init',) ])
		self.url = 'ws://localhost:80/ws' # default
		self.ws_connection = None


	def strtuple_of_atom(self, atom):
		return tuple([ x.value() for x in atom.tuple() ])

	def persistence_set_unset(self, atom, set_unset):
		assert(isinstance(atom, acthex.ID))
		assert(set_unset in [True, False])
		t = self.strtuple_of_atom(atom)
		if set_unset:
			self.atoms.add(t)
		else:
			if t in self.atoms:
				self.atoms.remove(t)

def persistenceSet(atom):
	assert(isinstance(atom, acthex.ID))
	assert(isinstance(acthex.environment(), PersistenceEnvironment))
	acthex.environment().persistence_set_unset(atom, True)

def persistenceUnset(atom):
	assert(isinstance(atom, acthex.ID))
	assert(isinstance(acthex.environment(), PersistenceEnvironment))
	acthex.environment().persistence_set_unset(atom, False)

def persistenceByPred(pred):
	#logging.debug('persistenceByPred:'+repr(pred))
	assert(isinstance(pred, acthex.ID))
	assert(isinstance(acthex.environment(), PersistenceEnvironment))
	predstr = pred.value()
	for a in acthex.environment().atoms:
		logging.debug("in environment: "+repr(a))
		if a[0] == predstr:
			# output tuple with single term containing full atom
			term = '{}({})'.format(a[0], ','.join(a[1:]))
			logging.debug('persistenceByPred output:'+repr(term))
			acthex.output( (term,) )

#######################################
# PSQL environment and external atoms #
#######################################
#
# environment = db connection cache (connection details in constructor)
# &sql1[query](const), ..., &sqlN[query](const1,...,constN)
# -> returns all N-ary tuples returned by query on DB
#
rGroundTerm = re.compile(r'^(([0-9]+)|([a-z][a-z0-9_]*))$')
class PSQLEnvironment(acthex.Environment):
	def __init__(self):
		self.dbname = 'ldm_2' # 'vissim_stream'
		self.connstr = "host=localhost port=5433 dbname={0}".format(self.dbname) # 5432
		self.cached_connection = None

	def connection(self):
		if not self.cached_connection:
			self.cached_connection = psycopg2.connect(self.connstr)
			self.cached_connection.set_client_encoding('UTF8')
		if not self.cached_connection:
			raise Exception("could not connect to pipelinedb with connection string "+repr(self.connstr))
		return self.cached_connection

	def sql(self, query, N):
		def safeTerm(x):
			r = str(x)
			# make terms safe for ASP
			m = rGroundTerm.match(r)
			if m:
				#print("passthrough ground term {} / {}".format(repr(r), repr(m)))
				return r
			else:
				r = '"{}"'.format(x)
				#print("quoted {}".format(repr(r)))
				return r
		assert(isinstance(N,int))
		assert(isinstance(query,str))
		cur = self.connection().cursor()
		cur.execute(query)
		for t in cur:
			#logging.warning("got t from cursor: "+repr(t))
			if N != len(t):
				logging.warning("&sql{}[{}] got tuple of size {}".format(N, query, len(t)))
			adaptedt = [ safeTerm(x) for x in list(t)[:N] ]
			while len(adaptedt) < N:
				adaptedt.append('null')
			acthex.output( tuple(adaptedt) )

def sqlN(query, N):
	assert(isinstance(query, acthex.ID))
	assert(isinstance(acthex.environment(), PSQLEnvironment))
	acthex.environment().sql(query.value().strip('"'), N)

# generate functions sql1 ... sqln
MAX_SQL_ARGUMENTS = 3
#for n in range(1,MAX_SQL_ARGUMENTS+1):
#	globals()['sql'+str(n)] = lambda query: sqlN(query, n)
sql1 = lambda query: sqlN(query, 1)
sql2 = lambda query: sqlN(query, 2)
sql3 = lambda query: sqlN(query, 3)

##############################################
#  Websockets environment and external atoms #
##############################################
class WebsocketsEnvironment(acthex.Environment):

	def __init__(self):
		self.ws_url = "" #  "ws://localhost:8082"
		self.ws_connection = None



def on_message(ws, message):
	a = ""
    #print("Msg received: " + message)
	# TODO: collect messages

def on_error(ws, error):
    print(error)

def on_close(ws):
    print("### WS client closed ###")

def on_open(ws):

	print("### WS client opened ###")

def wsConnect(new_url):

	# 	acthex.environment().connectWebsockets(new_url)

	new_url_s = str(new_url).replace('\"','')

	#print("d1: " + new_url_s)

	if len(new_url_s) == 0 or new_url_s == acthex.environment().ws_url:
		return
	else:
		acthex.environment().ws_url = new_url_s

	print("Connecting to: " + new_url_s)

	ws_conn = websocket.WebSocketApp(new_url_s, on_message = on_message, on_error = on_error, on_close = on_close)
	ws_conn.on_open = on_open
	acthex.environment().ws_connection = ws_conn

	wst = threading.Thread(target=ws_conn.run_forever)
	wst.daemon = True
	wst.start()

	#asyncio.get_event_loop().run_until_complete(alive())

	#tasks = [ asyncio.ensure_future(alive()) ]
	#asyncio.get_event_loop().run_until_complete(asyncio.wait(tasks))

	#ws_connection.run_forever()


#def wsSend(self, message):
def wsSend(*inputs):

	message = ""
	for txt in inputs:
		message = message + " " + str(txt).replace('\"','')
	#message = " ".join(inputs)
	#for message in inputs:
	#	ws.send(message)
	#	time.sleep(1)

	#print("Will send: " + message)

	if acthex.environment().ws_connection is None or acthex.environment().ws_connection.sock is None:
		return

	if acthex.environment().ws_connection.sock.connected:
		acthex.environment().ws_connection.send(message)
	#time.sleep(1)

def wsClose(url):

	if acthex.environment().ws_connection is None or acthex.environment().ws_connection.sock is None:
		return

	if acthex.environment().ws_connection.sock.connected:
		acthex.environment().ws_connection.close()

def wsReceive():

	# TODO: collect messages
	acthex.output(())

def wsConnected(url):
	if acthex.environment().ws_connection is not None and acthex.environment().ws_connection.sock.connected:
		acthex.output(())


##################################
# complete composite environment #
##################################
class Environment(PersistenceEnvironment,PSQLEnvironment,WebsocketsEnvironment): #

	def __init__(self):
		PersistenceEnvironment.__init__(self)
		PSQLEnvironment.__init__(self)
		WebsocketsEnvironment.__init__(self)

###########################################################################
# action without environment: print input tuple space-separated to stdout #
###########################################################################
def printLine(*inputs):
	assert(all([isinstance(x, acthex.ID) for x in inputs]))
	sys.stdout.write(' '.join([x.value().strip('"') for x in inputs])+'\n')
	sys.stdout.flush()

def timeStamp(t):

	#x = datetime.datetime.now()
	x = time.time()
	acthex.output(("\"" + str(x) + "\"",))

def sleep(t):
	time.sleep(t.intValue()/1000.0)

###########################################################################
# external atom without environment: check if inside geometry             #
###########################################################################
geomCachePoint = {}
def geomParsePointNocache(point):
	wktpoint = "POINT({})".format(point.value().strip('"'))
	return shapely.wkt.loads(wktpoint)
def geomParsePolyNocache(poly):
	wktpoly = poly.value().strip('"')
	return shapely.wkt.loads(wktpoly)
def geomParsePoint(point):
	global geomCachePoint
	if point not in geomCachePoint:
		geomCachePoint[point] = geomParsePointNocache(point)
	return geomCachePoint[point]
geomCachePoly = {}
def geomParsePoly(poly):
	global geomCachePoly
	if poly not in geomCachePoly:
		geomCachePoly[poly] = geomParsePolyNocache(poly)
	return geomCachePoly[poly]

def geomInside(point,poly):
	#spoint, spoly = geomParsePointNocache(point), geomParsePolyNocache(poly)
	spoint, spoly = geomParsePoint(point), geomParsePoly(poly)
	#print("checking if {} is within {}".format(repr(spoint), repr(spoly)))
	if spoint.within(spoly):
		acthex.output( () )



def register():
	acthex.setEnvironment(Environment())

	acthex.addAction('persistenceSet', (acthex.CONSTANT,))
	acthex.addAction('persistenceUnset', (acthex.CONSTANT,))
	acthex.addAtom('persistenceByPred', (acthex.CONSTANT,), 1)

	#for n in range(1,MAX_SQL_ARGUMENTS+1):
	#	acthex.addAtom('sql'+str(n), (acthex.CONSTANT,), n)
	acthex.addAtom('sql1', (acthex.CONSTANT,), 1)
	acthex.addAtom('sql2', (acthex.CONSTANT,), 2)
	acthex.addAtom('sql3', (acthex.CONSTANT,), 3)

	acthex.addAtom('geomInside', (acthex.CONSTANT,acthex.CONSTANT), 0)

	acthex.addAction('printLine', (acthex.TUPLE,))
	acthex.addAction('sleep', (acthex.CONSTANT,))

	acthex.addAction('wsConnect', (acthex.CONSTANT,))
	acthex.addAction('wsClose', (acthex.CONSTANT,))
	acthex.addAction('wsSend', (acthex.TUPLE,))
	acthex.addAtom('wsConnected', (acthex.CONSTANT,), 0)
	acthex.addAtom('timeStamp', (acthex.CONSTANT, ), 1)
	#acthex.addAction('wsSend', (acthex.CONSTANT,))
