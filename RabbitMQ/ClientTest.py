# this class models the test client in raft implementation
# @author Nitinkumar Gove
# @version 2.0

from rpcclient import RaftRpcClient
from Person import Person
from Request import Request

#TEST01
p = Person(1,"Gedam",24) 
request = Request("PUT",p)

req = request.toJSON()

raft_rpc = RaftRpcClient()
print("[x] Sening Request ")
try:
    response = raft_rpc.call(req)
    print "[.] Got" ,response
except pika.exceptions.ConnectionClosed,msg:
    print msg
    sys.exit();