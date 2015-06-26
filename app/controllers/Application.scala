package controllers

import java.io.File
import javax.inject.{Singleton, Inject}
import play.api.Play
import play.api.Play.current
import services.UUIDGenerator
import org.slf4j.{LoggerFactory, Logger}
import play.api.mvc._

/**
 * Instead of declaring an object of Application as per the template project, we must declare a class given that
 * the application context is going to be responsible for creating it and wiring it up with the UUID generator service.
 * @param uuidGenerator the UUID generator service we wish to receive.
 */
@Singleton
class Application @Inject() (uuidGenerator: UUIDGenerator) extends Controller {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])

  def index = Action {
    logger.info("Serving index page...")

    val javascripts = {
      if (Play.isDev) {
        // Load all .js and .coffeescript files within app/assets
        Option(Play.getFile("app/assets")).
          filter(_.exists).
          map(findScripts).
          getOrElse(Nil)
      } else {
        // Concated and minified by UglifyJS
        "concat.min.js" :: Nil
      }
    }

    Ok(views.html.index(javascripts))
  }

  private def findScripts(base: File): Seq[String] = {
    val baseUri = base.toURI
    directoryFlatMap(base, scriptMapper).
      map(f => baseUri.relativize(f.toURI).getPath)
  }

  private def scriptMapper(file: File): Option[File] = {
    val name = file.getName
    if (name.endsWith(".js")) Some(file)
    else if (name.endsWith(".coffee")) Some(new File(file.getParent, name.dropRight(6) + "js"))
    else None
  }

  private def directoryFlatMap[A](in: File, fileFun: File => Option[A]): Seq[A] = {
    in.listFiles.flatMap {
      case f if f.isDirectory => directoryFlatMap(f, fileFun)
      case f if f.isFile => fileFun(f)
    }
  }

  def randomUUID = Action {
    logger.info("calling UUIDGenerator...")
    Ok(uuidGenerator.generate.toString)
  }

}
