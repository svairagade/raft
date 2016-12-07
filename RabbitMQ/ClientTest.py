# this class models the test client in raft implementation
# @author Nitinkumar Gove
# @version 3.0

from rpcclient import RaftRpcClient
from Person import Person
from Request import Request
import json
import pika
import sys

def make_person(dct):
    return Person(dct['id'],dct['name'],dct['age'])

#TEST_POST
def getPOSTRequest(id, name, age):
    p = Person(id,name,age)
    request = Request("POST",p)
    req = request.toJSON()
    return req

#TEST_UPDATE
def getPUTRequest(id,name,age):
    p = Person(id,name,age)
    request = Request("PUT",p)
    req = request.toJSON()
    return req

#TEST_DELETE
def getDELETERequest(id,name,age):
    p = Person(id,name,age)
    request = Request("\DELETE",p)
    req = request.toJSON()
    return req

try:
    raft_rpc = RaftRpcClient()
    print("[x] Sending Request\n")
    response = raft_rpc.call(getPUTRequest(7,"Kim",29))
    print "[.] Received Response >> \n" ,response
    person = json.loads(response, object_hook=make_person)
    person.showPersonRecord();
   
except pika.exceptions.ConnectionClosed,msg:
    print msg
    sys.exit();