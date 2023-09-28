import java.util.concurrent.TimeoutException
import scala.util.control.NoStackTrace

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
      .mproduct(userAttempt)
      .timeoutAndForget(3.seconds)
      .ensure(new WrongAnswer)(_ == _)
      .productR(Console[IO].println("Yahoo!"))
      .foreverM
      .handleErrorWith {
        case _: TimeoutException => Console[IO].println("\nTimeout!")
        case _: WrongAnswer => Console[IO].println("Wrong answer!")
      }
      .void

def userAttempt(answer: String): IO[String] =
  Console[IO].print(s"Type this: ${answer}\n>") *> Console[IO].readLine

final class WrongAnswer extends Exception with NoStackTrace
