Modern Web Template
===========

**AngularJS - Scala - Play - Guice - PlayReactiveMongo**

A full application stack for a Modern Web application, lets review the components:

* **AngularJS** - client side javascript framework for creating complex MVC applications in Javascript,
fronted with Twitter bootstrap CSS framework, because well, im not a web designer.
  * Take a look at what the google cool kids are upto here : [AngularJS](http://angularjs.org/)

* **Bootstrap** - Bootstrap components written in pure AngularJS
  *  [http://angular-ui.github.io/bootstrap/](http://angular-ui.github.io/bootstrap/)

* **CoffeeScript** - CoffeeScript is an attempt to expose the good parts of JavaScript in a simple way.
  *  [http://coffeescript.org/](http://coffeescript.org/)

* **PlayFramework** - currently using 2.2.1 with the scala API
  *  [PlayFramework Docs](http://www.playframework.com/documentation/2.2.x/Home)

* **Guice** integration for Dependency injection,
  * Special thanks to the typesafehub team for their activator : [Play-Guice](http://www.typesafe.com/activator/template/play-guice)

* **PlayReactiveMongo** gives interaction with MongoDB providing a non-blocking driver as well as some useful additions for handling JSON.
  * Check out their GitHub: [Play-ReactiveMongo](https://github.com/ReactiveMongo/Play-ReactiveMongo)



Getting Started
----------

Your development environment will require:
*  SBT / Play see [here]() for installation instructions.
*  MongoDB see [here]() for installation instructions.

Once the prerequisites have been installed, you will be able to execute the following from a terminal.

```
../modern-web-template >  play run
```

This should fetch all the dependencies and start a Web Server listening on *localhost:9000*

```
[info] Loading project definition from ../modern-web-template/project
[info] Set current project to modern-web-template
[info] Updating modern-web-template...
...
[info] Done updating.

--- (Running the application from SBT, auto-reloading is enabled) ---

[info] play - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

(Server started, use Ctrl+D to stop and go back to the console...)

```

Note: This will create a MongoDB Collection for you automatically, a free-be from the Driver! 