# Vacation Bot

## Overview

The Vacation-Bot manages the vacation reservation.

## Prerequisites
* [JDK 8](http://www.oracle.com/technetwork/java/index.html) installed and working
* MongoDB running

## Integration with Slack
To integrate **Vacation Bot** with your Slack channel go to 
_Settings -> Add an app -> Manage -> Custom Integrations -> Bots -> Add Configuration_
Choose the name for your bot in channel, e.g. _vacationBot_ and copy generated token.
Add your token to the application.properties:
`slackBotToken=<your-secure-token>`

Start up the application. 

:point_right: Remember not to share your token in public services.

## Building
Type `./gradlew` to build and assemble the service.
    