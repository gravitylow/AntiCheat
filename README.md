AntiCheat
=============

AntiCheat helps server admins easily identify and block malicious users by monitoring and analyzing the behaviour of their players.

[![Build Status](https://travis-ci.org/gravitylow/AntiCheat.png?branch=master)](https://travis-ci.org/gravitylow/AntiCheat)

Usage
-------
Simply drag and drop the plugin into your Plugins folder. Optional configuration and permissions are available:

http://dev.bukkit.org/server-mods/anticheat/pages/configuration/

http://dev.bukkit.org/server-mods/anticheat/pages/permissions/

Developer Resources
-------
API Usage: http://dev.bukkit.org/server-mods/anticheat/pages/api-documentation/

JavaDocs: https://gravitydevelopment.net/docs/anticheat/

Bug Reporting/Support/Questions
------------

Issues should be filed by hitting the [issues](https://github.com/h31ix/AntiCheat/issues?state=open) tab above. Please make sure you search your issue before opening a new ticket.

All opened tickets that are bug reports require a debug log that includes logs of the issue in question. To obtain this, type `/anticheat debug` on your server. Note that for the debug output to contain valid information and help us diagnose your issue, you need to issue this command AFTER the issue occurs at least once and BEFORE you reload or restart it.

Resources
-------
BukkitDev: http://dev.bukkit.org/server-mods/anticheat/

Development Builds: http://ci.gravitydevelopment.net/job/AntiCheat/

Latest release download: http://api.bukget.org/2/bukkit/plugin/anticheat/latest/download

Latest dev download: http://ci.gravitydevelopment.net/job/AntiCheat/lastSuccessfulBuild/artifact/target/AntiCheat.jar

Compiling
-------

AntiCheat builds on Java JDK 1.6 and uses [Maven 3](http://maven.apache.org/download.cgi) to manage dependencies.

To download the source code, first make sure [Git](http://git-scm.com/) is installed, then (from the command line) use
```git clone git@github.com:h31ix/AntiCheat.git```

The source code will be downloaded into a new directory 'AntiCheat'.

Make any changes you desire and then, to compile AntiCheat, ensure you are inside the AntiCheat directory (cd AntiCheat) and use
```mvn clean install```

If all is successful, AntiCheat.jar will be compiled to the 'target' folder and will be ready for use. If you encounter a problem or error, please google search it first and, if you are unable to find an answer, ask a developer.
