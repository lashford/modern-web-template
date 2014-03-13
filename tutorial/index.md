
#Modern-web-applictation 

This application shows how to build a 'modern' web application, comprising of a Client-side JavaScript App built using ``AngularJS`` wrote in ``CoffeeScript``, served from the ``Play 2 Framework`` and using document persistence with ``Reactive Mongo`` a non-blocking Scala client for ``MongoDB``.

Sounds like a cool stack, well I think so!  In this tutorial I'm going to cover how to structure an AngularJS app,
expose a Rest Api in Play and read/write JSON Documents in MongoDB.  By the end we will have created a simple User
management app which gives a thin slice of Entity management, collecting data in the AngularJS app and persisting in MongoDB.

So lets get down to it by seeing the overall structure of the application we are building.

![Overall structure](https://raw.github.com/lashford/modern-web-template/master/tutorial/overview.png)

So now we know the components, lets see how they all fit together...

##MongoDB integration

I opted to use the Reactive Mongo driver to give me scalability with an asynchronous, non-blocking interface to MongoDB.  The guys over at [Play-ReactiveMongo](https://github.com/ReactiveMongo/Play-ReactiveMongo) have made this integration even easier with a Play 2 plugin.

Adding the plugin to an app is simples...

First, add the dependency -> see [Build.scala](https://github.com/lashford/modern-web-template/blob/master/project/build.scala)

```scala
libraryDependencies ++= Seq(
   "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2"
)
```

Once that's done, configure MongoDB in the app config -> see [application.conf](https://github.com/lashford/modern-web-template/blob/master/conf/application.conf)

```scala
mongodb.uri = "mongodb://localhost:27017/modern-web-template"
```

Moving on, add the following to your [conf/play.plugins](https://github.com/lashford/modern-web-template/blob/master/conf/play.plugins)

```scala
400:play.modules.reactivemongo.ReactiveMongoPlugin
```

Now that you have the ``RactiveMongoPlugin`` added, you can mix-in the `MongoController` trait to your controllers. In our app, it is just the `Users`:

```scala
class Users extends Controller with MongoController
```

The ``MongoController`` trait provides convenient functions exposed by the ``ReactiveMongoPlugin``; for example providing us with nice handling of the JSON objects and a friendly wrapper to MongoDB.

With all that, we can implement the `createUser` function to write the User JSON object to MongoDB, notice the conversion from raw JSON to the `User` object.

```scala
def createUser = Action.async(parse.json) {
   request =>
     request.body.validate[User].map {
       user =>
         // `user` is an instance of the case class `models.User`
         collection.insert(user).map {
           lastError =>
             logger.debug(s"Successfully inserted with LastError: $lastError")
             Created(s"User Created")
         }
     }.getOrElse(Future.successful(BadRequest("invalid json")))
 }
```

Now that we can create users, we should really implement the `findUsers` function. It unsurprisingly creates a MongoDB query and return a list of ``User``s as JSON.

```scala
def findUsers = Action.async {
    // let's do our query
    val cursor: Cursor[User] = collection.
      // find all
      find(Json.obj("active" -> true)).
      // sort them by creation date
      sort(Json.obj("created" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[User]
    // gather all the JsObjects in a list
    val futureUsersList: Future[List[User]] = cursor.collect[List]()
    // transform the list into a JsArray
    val futurePersonsJsonArray: Future[JsArray] = futureUsersList.map { users =>
       Json.arr(users)
    }
    // everything's ok! Let's reply with the array
    futurePersonsJsonArray.map {
      users =>
        Ok(users(0))
    }
}
```
	
Head over to the [Users Controller](https://github.com/lashford/modern-web-template/blob/master/app/controllers/Users.scala) to see all the other details.

And you're done. At this point you have the ability to write and read JSON documents to MongoDB from the controller.  Although a controller on its own is pretty useless without a wiring in the routes.

## Play REST API

Lets expose these methods as a REST endpoint in Play by adding the following line to [conf/routes](https://github.com/lashford/modern-web-template/blob/master/conf/routes)

```scala
GET     /users                      @controllers.Users.findUsers
POST    /user                       @controllers.Users.createUser
```

At this point you will be able to execute `play run` which would start a http server running on port 9000, this will expose the endpoints for creating and listing users.

Using your favourite Rest Client you can now test the endpoints by posting some JSON.  If your Using [PostMan](http://www.getpostman.com/) I have shared the JSON Collection [here](https://www.getpostman.com/collections/4b4d157ab081de7b828e). 

Create a user:

```json
POST -> http://localhost:9000/user
HEADERS: Content-Type: application/json
BODY: 
{ "age": 44,
 "firstName": "jan",
 "lastName": "joe",
 "active": true}
```
List the users:

```json
GET  -> http://localhost:9000/users
HEADERS: Content-Type: application/json
```
This gives us the back-end to our application, now lets create a UI to consume this API. 

## AngularJS App

AngularJS is pretty awesome but is a bit of a mind shift from a traditional web application. After playing with this Activator, I suggest doing some reading, the docs and learning material are pretty extensive and the community is very active.  I'll run you through the key points of how the code hangs together in CoffeeScript. Let’s start by taking a look at `app.coffee`

```scala
dependencies = [
    'ngRoute',
    'ui.bootstrap',
    'myApp.filters',
    'myApp.services',
    'myApp.controllers',
    'myApp.directives',
    'myApp.common',
    'myApp.routeConfig'
]
app = angular.module('myApp', dependencies)
angular.module('myApp.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/view.html'
            })
            .when('/users/create', {
                templateUrl: '/assets/partials/create.html'
            })
            .otherwise({redirectTo: '/'})
@commonModule = angular.module('myApp.common', [])
@controllersModule = angular.module('myApp.controllers', [])
@servicesModule = angular.module('myApp.services', [])
@modelsModule = angular.module('myApp.models', [])
@directivesModule = angular.module('myApp.directives', [])
@filtersModule = angular.module('myApp.filters', [])

```

This is where we are plugging the different modules together, notice at the bottom of the file we are creating globally scoped variables which gives us access the appropriate modules anywhere in the app. This allows us to register for example a controller see [UserCtrl.coffee](https://github.com/lashford/modern-web-template/blob/master/app/assets/javascripts/users/UserCtrl.coffee)

```scala
class UserCtrl
	constructor: (@$log, @UserService) ->
    	@$log.debug "constructing UserController"
    	@users = []
	...
controllersModule.controller('UserCtrl', UserCtrl)
```

I use the same approach to register a service [UserService.coffee](https://github.com/lashford/modern-web-template/blob/master/app/assets/javascripts/users/UserService.coffee)
 
```scala
class UserService
    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserService"
...
servicesModule.service('UserService', UserService)
```

Defining these classes at global scope allows AngularJS to do its magic with dependency injection. From the `UserCtrl`, I want access to the `UserService` so I can call the Play REST API.  By declaring the ``UserService`` as a constructor dependency, AngularJS will look for the thing registered using the name `UserService` to inject when constructing the controller.  This gives us the benefits of DI with the ability to test and mock out services, testing components in isolation.

Note that the name of the service *DOES* matter here as the name in quotes will be used when looking up the reference in AngularJS.

## Serving AngularJS from a single page.

So now we have the AngularJS app created we need to serve the index page from Play, which will provide the full AngularJS framework to the client browser.

Create an ``index.scala.html`` template page where you can define the AngularJS directives and include the required JavaScript libraries.


```html
<!doctype html>
<html lang="en" ng-app="myApp">

<body>
	<div ng-view></div>
</body>

<script src='//ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular.js' type="text/javascript"></script>
<script src='//ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-route.js' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/vendor/ui-bootstrap-tpls-0.10.0.js")' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/main.js")' type="text/javascript"></script>

<script src='@routes.Assets.at("javascripts/app.js")' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/common/Config.js")' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/directives/AppVersion.js")' type="text/javascript"></script>

<script src='@routes.Assets.at("javascripts/users/UserService.js")' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/users/UserCtrl.js")' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/users/CreateUserCtrl.js")' type="text/javascript"></script>

</html>
```

Notice here we have added the ``ng-app`` directive to the ``html`` tag, this binds the AngularJS app to this page and when loaded will construct the app for us.  Each of the CoffeeScript files are compiled into individual JavaScript files that need adding as resources to the single page.

Now we can add a routes entry and a controller to serve this single page, [Routes](https://github.com/lashford/modern-web-template/blob/master/conf/routes) [Controller](https://github.com/lashford/modern-web-template/blob/master/app/controllers/Application.scala)

```scala
   GET     /       @controllers.Application.index
```

```scala
class Application extends Controller {
   def index = Action {
      logger.info("Serving index page...")
      Ok(views.html.index())
   }
}
```

And there we have the application all wired together.

## Screenshots
Bellow are screenshots of the activator running, showing the *Create User* form and the *List Users* pages.

Create User

![User Creation](https://raw.github.com/lashford/modern-web-template/master/tutorial/create.png)

List Users

![View Users](https://raw.github.com/lashford/modern-web-template/master/tutorial/view.png)

## Developement Setup

To run the activator locally you will need to setup a few things, please see the readme for prerequisite [Instructions](http://github.com/lashford/modern-web-template/blob/master/README.md)

##Summary

So why the tech choices?

### AngularJS & CoffeeScript

AngularJs is a client side MVC style framework written in JavaScript. The framework adapts and extends traditional HTML to better serve dynamic content through two-way data-binding that allows for the automatic synchronisation of models and views. As a result, AngularJS de-emphasises DOM manipulation and improves testability.

Traditionally AngularJS applications are written in JavaScript, my main objection to javascript is its clunky Java-esque syntax and its darn demand for all those braces.  So in steps CoffeeScript. 

*CoffeeScript is a little language that compiles into JavaScript. Underneath that awkward Java-esque patina,
JavaScript has always had a gorgeous heart. CoffeeScript is an attempt to expose the good parts of 
JavaScript in a simple way.*

Read more about [CoffeeScript](http://coffeescript.org/) & [AngularJS](http://angularjs.org/)

### BootstrapUI

Well to be honest any CSS framework would do here, I quite like Bootstrap, its well supported and means I don't need to hand crank CSS.  Because well at the end of the day I'm not a web designer, I like Arial font, primary colours and simple layouts.

For more reading check out:

[AngularJS Bootstrap](http://angular-ui.github.io/bootstrap/)
Or 
[Zorb Foundation](http://foundation.zurb.com/)

### MongoDb

MongoDB (from "humongous") is an open-source document database, and the leading NoSQL database.  Its schema free design make it highly scalable, cost effective and more.  MongoDB enables profound developer agility through its flexible data model.  

[MongoDb](http://www.mongodb.com)

### Play 2 Framework & Scala

Play is a high-productivity web-framework with a great Scala api, making it easy to create modern, reactive web applications

[Play 2 Framework](http://www.playframework.com/documentation/2.2.x/Home)

### Try it out

Download the Activator or clone the project and execute the following in a terminal from inside the project  

```scala
> play run
```

and Play will compile the CoffeeScript and launch a webserver on ``localhost:9000``, ``play run`` is wrapping SBT and so gives us "hot compile" of the code for seamless web development, so saved changes are instantly reflected on the browser.

So what are you waiting for, download this activator and check out the code!

The code is available to download or fork [here](http://github.com/lashford/modern-web-template/), please feel free to raise issues and submit enhancements via pull requests.

That's all folks, enjoy...

#### Alex Lashford