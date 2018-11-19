package http4s.techempower.benchmark.services

import http4s.techempower.benchmark.models.QueryResult

trait QueryService[F[_]] {

  def multiple(ids: List[Int]): F[List[QueryResult]]

}
