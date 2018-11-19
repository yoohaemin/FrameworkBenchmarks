package http4s.techempower.benchmark

import cats.data.{ Kleisli, OptionT }
import cats.effect.{ Async, Clock, ContextShift, Resource }
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.semigroupk._
import http4s.techempower.benchmark.endpoints._
import java.time.{ Instant, OffsetDateTime, ZoneOffset }
import java.time.format.DateTimeFormatter

import doobie.util.transactor.Transactor
import org.http4s.{ Header, HttpRoutes }
import org.http4s.dsl.Http4sDsl

import scala.concurrent.duration.MILLISECONDS


class Modules[F[_]: Async: ContextShift: Clock](xs: Transactor[F]) extends Http4sDsl[F] {

  // DB
  private val nondbEndpoints = new NonDBEndpoints[F].routes
  private val dbEndpoints = new DBEndpoints[F](???, ???, ???).routes

  private val endpoints = nondbEndpoints <+> dbEndpoints

  // Middleware (Adds "Server" and "Date" header)
  private def addHeader(header: F[Header])(service: HttpRoutes[F]): HttpRoutes[F] =
    Kleisli { req =>
      OptionT(header.flatMap { h => service(req).map(_.putHeaders(h)).value })
    }

  private def formattedCurrentTime(implicit clock: Clock[F]): F[String] =
    clock.realTime(MILLISECONDS)
      .map { milli =>
        val now = OffsetDateTime.ofInstant(Instant.ofEpochMilli(milli), ZoneOffset.UTC)
        now.format(DateTimeFormatter.RFC_1123_DATE_TIME)
      }

  private val serverHeader: F[Header] = (Header("Server", "http4s/0.20.0-M1"): Header).pure
  private val dateHeader: F[Header] = formattedCurrentTime.map(Header("Date", _))

  private val middleware = List(serverHeader, dateHeader).map(header => addHeader(header) _).reduce(_ andThen _)

  //Http Service
  val httpApp = middleware(endpoints).orNotFound
}
