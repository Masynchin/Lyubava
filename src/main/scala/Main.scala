import java.util.concurrent.TimeoutException

import cats.implicits.*
import cats.effect.{IO, IOApp}
import cats.effect.std.{Console, Random}
import scala.concurrent.duration.*
import cats.Show

object Lyubava extends IOApp.Simple:
  val answer = Random.scalaUtilRandom[IO].flatMap { random =>
    random
      .nextAlphaNumeric
      .replicateA(4)
      .map(_.mkString)
    }

  val run =
    answer
      .flatMap(userAttempt)
      .timeoutAndForget(3.seconds)
      .productR(Console[IO].println("Yahoo!"))
      .foreverM
      .handleErrorWith {
        case _: TimeoutException => Console[IO].println("Timeout!")
      }
      .void

def userAttempt(answer: String): IO[String] =
  Console[IO].println(s"Type this: ${answer}") *> Console[IO].readLine
