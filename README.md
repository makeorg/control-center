Make back-office
================

Back office made with love and passion in scalaJs

Configure NPM Registry
----------------------

Edit your `~/.npmrc` file with content:

```
registry=https://nexus.prod.makeorg.tech/repository/npm
strict-ssl=false
always-auth=true
```

Then login with

```
npm login
```

**Requirement:** You must have a nexus account.

Launch process
--------------
- `sbt`
- `fastOptJS::startWebpackDevServer`
- `~fastOptJS::webpack`

In your browser access URL
[http://localhost:4242/webpack-dev-server/](http://localhost:4242/webpack-dev-server/)

Stop the server with:

- `fastOptJS::stopWebpackDevServer`

Known Issues
------------
**Module WebpackDevServer not found**

*Fix*: 
- remove `~/node_modules`
- restart launch process