import java.util.concurrent.TimeoutException
import scala.util.control.NoStackTrace

import cats.implicits.*
import cats.effect.{IO, IOApp}
import cats.effect.std.{Console, Random}
import scala.concurrent.duration.*
import cats.Show
import fansi.Color.{Green, Red}

object Lyubava extends IOApp.Simple:
  val run =
    answer(4)
      .mproduct(userAttempt)
      .timeoutAndForget(3.seconds)
      .ensure(new WrongAnswer)(_ == _)
      .productR(Console[IO].println(Green("Yahoo!")))
      .foreverM
      .handleErrorWith {
        case _: TimeoutException => Console[IO].println(Red("\nTimeout!"))
        case _: WrongAnswer => Console[IO].println(Red("Wrong answer!"))
      }
      .void

def answer(length: Int): IO[String] =
  Random.scalaUtilRandom[IO].flatMap { random =>
    random
      .nextAlphaNumeric
      .replicateA(length)
      .map(_.mkString)
    }

def userAttempt(answer: String): IO[String] =
  Console[IO].print(s"Type this: ${answer}\n>") *> Console[IO].readLine

final class WrongAnswer extends Exception with NoStackTrace
