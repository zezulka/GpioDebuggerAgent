Gpio debugger agent
===================

Introduction
------------ 
This agent is the client part of a debugging tool based on *[bulldog library](https://github.com/SilverThings/bulldog "Bulldog")*. To use it, simply install it via Maven. Agent must be run as root (access to `/dev/mem` is required for the agent to work properly).

Application output
------------------
Important actions are recorded via logging. By default, there are two appenders created: console and file output. Log files can be found in the `/logs/` folder relative to project path.
