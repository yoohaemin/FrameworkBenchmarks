package http4s.techempower.benchmark.endpoints

import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.applicativeError._
import cats.data.NonEmptyList
import cats.effect.Sync
import html.index
import http4s.techempower.benchmark.endpoints.jsonCodecs._
import http4s.techempower.benchmark.models.Fortune
import http4s.techempower.benchmark.services.{ FortuneService, QueryService, RandomService }
import org.http4s.{ HttpRoutes, Response }
import org.http4s.dsl.Http4sDsl
import org.http4s.twirl._

class DBEndpoints[F[_]: Sync](
    queryService: QueryService[F],
    fortuneService: FortuneService[F],
    random: RandomService[F]
) extends Http4sDsl[F] {

  private val range: Range = 1 to 10000

  val handleError: Throwable => F[Response[F]] = ???

  object QueryQPM extends QueryParamDecoderMatcher[Int]("query")

  val routes = HttpRoutes.of[F] {

    case GET -> Root / "db" =>
      random.generate(range)
          .flatMap { r => queryService.multiple(r) }
          .attempt
          .flatMap { res => res.fold(handleError, res => Ok(res.head)) }

    case GET -> Root / "queries" :? QueryQPM(size) =>
      random.generate(range, size)
          .flatMap { r => queryService.multiple(r) }
          .attempt
          .flatMap { res => res.fold(handleError, res => Ok(res.head)) }

    case GET -> Root / "fortunes" =>
      val addedFortuneMessage =
        Fortune(id = 1, message = "Additional fortune added at request time.")

      fortuneService.getAll
          .map { fortunes => (addedFortuneMessage :: fortunes).sorted }
          .attempt
          .flatMap { res => res.fold(handleError, res => Ok(index(res))) }

    case GET -> Root / "updates" =>
      Ok("Hello, World!")

  }

}
