
#Repliaction using Raft Consensus Algorithm

#Objective
To implement data replication in a cluster using raft distributed consensus algorithm along with graphical visualization of cluster node states.

#How to
1. Start the RabbitMQ service
2. Start RedisDB
3. Build server code using below maven command<br />
   mvn clean install
4. Execute the generated jar file using below command:<br />
   java -jar raft-consensus-{version}.jar -ip {host} -p {port} <br />
   eg. java -jar raft-consensus-1.0-SNAPSHOT.jar -ip localhost -p 8080 <br />
   You can execute as many servers as you wish they will add themselves to the cluster.
5. Use Pyhton file "ClientTest.py" to perform operation as a client
6. You can run the "index.htm" to get the live status of the nodes


#Demo
[Watch Demo Here](https://www.youtube.com/watch?v=VgWI_JIyu80)

#Requirements
1. [Java](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)
2. [Redis](https://redis.io/)
3. [RabbitMQ](https://www.rabbitmq.com/)
4. [Python](https://www.python.org/downloads/)

#References
1. [Introduction to Raft](https://raft.github.io/)
2. [Raft Visualization](http://thesecretlivesofdata.com/raft/)
3. [In Search of an Understandable Consensus Algorithm](https://raft.github.io/raft.pdf)
