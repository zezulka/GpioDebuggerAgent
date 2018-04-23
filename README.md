Gpio debugger agent
===================

Introduction
------------ 
This agent is the server part of a debugging tool based on *[bulldog library](https://github.com/SilverThings/bulldog "Bulldog")*. Agent must be run as root (access to `/dev/mem` is required for the agent to work properly).

Installation
------------
Releases are placed in the *[releases tab of this Github repository](https://github.com/zezulka/GpioDebuggerAgent/releases "releases")*. Otherwise, you can use Maven to compile the whole project which consists of two projects: bulldog library and the agent itself.
First, install Bulldog to your local repository by issuing `mvn clean install`
in the root folder of Bulldog (you must install required gcc cross compiler first in order to do that).
Once you have Bulldog installed in your local repository, you can then install
the agent itself again by `mvn clean install` in the root folder of the agent project.

Bulldog
-----------
All the prerequisites are listed in `https://github.com/SilverThings/bulldog` and hold true also for the repository used to store the Bullog for the agent, `https://github.com/zezulka/bulldog`. Building the library itself should also be equivalent to what is mentioned on the same page.
Due to the limited resources of the Raspberry Pi devices, it is highly recommended to compile and build the library outside of a Pi and then simply copy the resulting .jar file to the remote machine. You can also use the deployment feature present in the client which does it for you.

Agent
----------


Application output
------------------
Important actions are recorded via logging. By default, there are two appenders created: console and file output. Log files can be found in the `./logs/ folder.
