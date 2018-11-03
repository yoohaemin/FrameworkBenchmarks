package http4s.techempower.benchmark


object models {

  case class Message(message: String)

  case class World(id: Int, randomNumber: Int)

  case class Fortune(id: Int, message: String)

}
