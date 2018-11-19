package http4s.techempower.benchmark

import cats.instances.ordering._
import cats.syntax.contravariant._

object models {

  case class Message(message: String)

  case class QueryResult(id: Int, randomNumber: Int)

  case class Fortune(id: Int, message: String)

  object Fortune {
    implicit val fortuneOrdering: Ordering[Fortune] = Ordering[String].contramap(_.message)
  }

}
