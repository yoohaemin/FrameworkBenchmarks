package http4s.techempower.benchmark.services

import cats.effect.Sync

import scala.util.Random

class RandomService[F[_]: Sync] {

  /** Generate a random Int (range is inclusive at both ends) */
  def generate(range: Range, count: Int = 1): F[List[Int]] =
    Sync[F].delay {
      val size = range.last - range.head + 1
      Array.fill(size)(Random.nextInt(size) - range.head).toList
  }

}
