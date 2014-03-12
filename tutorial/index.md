
#Modern-web-applictation 

This application shows how to build a 'modern' web application, comprising of a Client-side Javascript App built using
``AngularJS`` wrote in ``CoffeeScript``, served from the ``Play 2 Framework`` and using document persistence with
``Reactive Mongo`` a non-blocking Scala client for ``MongoDB``.

Sounds like a cool stack, well I think so!  In this tutorial, I'm going to cover how to structure an AngularJS app,
expose a Rest Api in Play and Read/Write Json Documents in MongoDB.  By the end we will have created a simple User
management app which gives a thin slice of Entity management, collecting data in the Angular app and persisting in MongoDB.

### AngularJS & CoffeeScript

AngularJs is a client side MVC style framework written in Javascript. The framework adapts and extends traditional HTML to better serve dynamic content through two-way data-binding that allows for the automatic synchronization of models and views. As a result, AngularJS deemphasizes DOM manipulation and improves testability.

Traditionally Angular applications are wrote in Javascript, my main objection to javascript is its clunky Java esque syntax and its darn damand for all those bracaes.  So in steps CoffeeScript, 

>CoffeeScript is a little language that compiles into JavaScript. Underneath that awkward Java-esque patina,
>JavaScript has always had a gorgeous heart. CoffeeScript is an attempt to expose the good parts of 
>JavaScript in a simple way.

read more about [CoffeeScript](http://coffeescript.org/) & [AngularJS](http://angularjs.org/)

### BootstrapUI

Well to be honest any CSS framework would do here, I quite like Bootstrap, its well supported and means I dont need to hand crank CSS.  Becasue well at the end of the day im not a web designer, I like Arial font, primary colours and simple layouts. :-) 

for more reading check out:

[Angular Bootstrap](http://angular-ui.github.io/bootstrap/)
Or 
[Zorb Foundation](http://foundation.zurb.com/)

### MongoDb

MongoDB (from "humongous") is an open-source document database, and the leading NoSQL database.  Its schema free design make it highly scalable, cost effective and more.  MongoDB enables profound developer agility through its flexible data model.  

[MongoDb](http://www.mongodb.com)

### Play 2 Framework & Scala

Play is a high-productivity web-framework with a great Scala api, making it easy to create modern, reactive web applications

[Play 2 Framework](http://www.playframework.com/documentation/2.2.x/Home)


## The Activator
So lets get down to it and allow me to begin, by showing the overall structure of the application we are building.

![Overall structure](https://raw.github.com/lashford/modern-web-template/master/tutorial/overview.png)

So now we know the components lets see how they all fit together:

###MongoDB integration.

I opted to use the Reactive Mongo driver to give me scalability with an asycrhonous, non-blocking interface to the Mongo-cli.
The guys over at [Play-ReactiveMongo](https://github.com/ReactiveMongo/Play-ReactiveMongo) have made this integration even easier with a play 2 plugin.

Adding the plugin to an app is simples,

1.  Add the dependency
```
libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2"
)
```
2. Configure MongoDb in the app config
```
# ReactiveMongo
mongodb.uri = "mongodb://localhost:27017/modern-web-template"
```
3. add to your conf/play.plugins
```
  400:play.modules.reactivemongo.ReactiveMongoPlugin
```
4. mix-in the `MongoController` trait
```
class Users extends Controller with MongoController
```
5. Implement a controller method using the all the helper lovliness of the plugin.
```scala
def createUser = Action.async(parse.json) {
    request =>
    /*
     * request.body is a JsValue.
     * There is an implicit Writes that turns this JsValue as a JsObject,
     * so you can call insert() with this JsValue.
     * (insert() takes a JsObject as parameter, or anything that can be
     * turned into a JsObject using a Writes.)
     */
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

And your done, you have the ability to write JSON documents to Mongo.

### Play REST API

Lets now move on to exposing this method as a `REST` endpoint in play by adding the following line to `\conf\routes`

```
POST    /user   @controllers.Users.createUser
```

At this point you should be able to execute `play run` which would start a http server running on port 9000, this exposes the create user endpoint here:

```
http://localhost:9000/user
```

### Angular App

...TBD



### Serving Angular from a single page.

So now we have the angular app created we need to serve the index page from Play, which will provide the full angular mvc framework to the client browser.

Create an index.scala.html template page where you can define the Angular Directives and include the required javascipt libraries.

```
<!doctype html>
<html lang="en" ng-app="myApp">
<body>
...
    <div ng-view></div>
...
</body>

<script src='//ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular.js' type="text/javascript"></script>
<script src='//ajax.googleapis.com/ajax/libs/angularjs/1.2.13/angular-route.js' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/vendor/ui-bootstrap-tpls-0.10.0.js")' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/main.js")' type="text/javascript"></script>

<!-- Coffee script compilled resources-->
<script src='@routes.Assets.at("javascripts/app.js")' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/common/Config.js")' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/directives/AppVersion.js")' type="text/javascript"></script>

<script src='@routes.Assets.at("javascripts/users/UserService.js")' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/users/UserCtrl.js")' type="text/javascript"></script>
<script src='@routes.Assets.at("javascripts/users/CreateUserCtrl.js")' type="text/javascript"></script>

</html>
```

Now we can add a routes entry and a controller:
```
GET     /       @controllers.Application.index
```
```
def index = Action {
    logger.info("Serving index page...")
    Ok(views.html.index())
  }
```

And there we have it, execute the following from inside the project  
```
> play run
```
and Play will compile the CoffeeScript and launch a websever on localhost:9000, `play run` is wrapping SBT and so gives us "hot compile" of the code for seemless web development.

## Screenshots
Bellow are screenshots of the activator running, showing the Create User form and the List users pages.

create User

![User Creation](https://raw.github.com/lashford/modern-web-template/master/tutorial/create.png)

List Users

![View Users](https://raw.github.com/lashford/modern-web-template/master/tutorial/view.png)

## Developement Setup

To run the activator locally you will need to setup a few things, please see the readme for prerequisites [Instructions](http://github.com/lashford/modern-web-template/blob/master/README.md)

##Summary

The code is available to download or fork [here](http://github.com/lashford/modern-web-template/), please feel free to raise issues and submit enhancements via pull requests.

That's all folks...

#### Alex Lashford