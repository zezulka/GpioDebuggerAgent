Gpio debugger agent
===================

Introduction
------------ 
This agent is the server part of a debugging tool based on *[bulldog library](https://github.com/SilverThings/bulldog "Bulldog")*. Agent must be run as root (access to `/dev/mem` is required for the agent to work properly).

Installation
------------
Releases are placed in the *[releases tab of this Github repository](https://github.com/zezulka/GpioDebuggerAgent/releases "releases")*. 
Otherwise, you can use Maven to compile the whole project which mainly consists of 
two projects: the Bulldog library and the agent itself.
First, install Bulldog to your local repository by issuing `mvn clean install`
in the root folder of the cloned Bulldog repository (you must install required
gcc cross compiler first in order to do that - see more instructions at 
[the official Bulldog repository](https://github.com/SilverThings/bulldog)).
Once you have Bulldog installed in your local repository, you can then install
the agent itself again by launching the command `mvn clean install` in the 
root folder of the agent project.

Bulldog
-----------
All the prerequisites are listed in `https://github.com/SilverThings/bulldog` 
and hold true also for the repository used to store the Bullog for the agent, 
`https://github.com/zezulka/bulldog`. Building the library itself therefore is 
equivalent to the original version.
Due to the limited resources of the Raspberry Pi devices, it is highly 
recommended to compile and build the library outside of a Pi environment and 
then simply copy (via `scp`, for example) the resulting JAR file to the remote 
machine. You can also use the deployment feature present in the client which 
does it for you.

Application output
------------------
Important actions are recorded via logging. By default, there are two appenders
created: console and file output. Log files can be found in the `./logs/ folder.
