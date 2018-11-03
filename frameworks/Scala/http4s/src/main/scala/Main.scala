package http4s.techempower.benchmark

import cats.effect._
import cats.syntax.functor._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import org.http4s.server.blaze.BlazeServerBuilder

class Server[F[_]: ConcurrentEffect: ContextShift: Timer] {
  def connectDatabase(host: String, poolSize: Int): Resource[F, HikariTransactor[F]] = {
    val driver = "org.postgresql.Driver"
    val url = s"jdbc:postgresql://$host/hello_world"
    val user = "benchmarkdbuser"
    val pass = "benchmarkdbpass"
    val maxPoolSize = poolSize

    for {
      ce <- ExecutionContexts.fixedThreadPool[F](maxPoolSize)
      te <- ExecutionContexts.cachedThreadPool[F]
      xa <- HikariTransactor.newHikariTransactor[F](driver, url, user, pass, connectEC = ce, transactEC = te)
    } yield xa
  }

  val http = connectDatabase("", 999).use { db =>
    BlazeServerBuilder[F]
        .withHttpApp(new Modules[F](db).httpApp)
        .bindLocal(8080)
        .serve
        .compile
        .drain
  }
}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = new Server[IO].http.as(ExitCode.Success)
}
