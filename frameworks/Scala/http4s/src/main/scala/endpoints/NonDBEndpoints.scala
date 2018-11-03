package http4s.techempower.benchmark.endpoints

import cats.effect.Sync
import http4s.techempower.benchmark.endpoints.jsonCodecs._
import http4s.techempower.benchmark.models.Message
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class NonDBEndpoints[F[_]: Sync] extends Http4sDsl[F] {

  val routes = HttpRoutes.of[F] {

    case GET -> Root / "json" =>
      Ok(Message("Hello, World!"))

    case GET -> Root / "plaintext" =>
      Ok("Hello, World!")

  }

}
