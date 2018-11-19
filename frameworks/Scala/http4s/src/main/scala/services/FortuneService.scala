package http4s.techempower.benchmark.services

import http4s.techempower.benchmark.models.Fortune

trait FortuneService[F[_]] {

  def getAll: F[List[Fortune]]

}
