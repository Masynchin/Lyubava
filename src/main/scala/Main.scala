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
    replicate(
      withFeedback(
        withTimeout(answer.flatMap(userAttempt), 3.seconds),
        _.fold("Timeout!")(Function.const("Yahoo!"))
      )
    ).void

def userAttempt(answer: String): IO[String] =
  Console[IO].println(s"Type this: ${answer}") *> Console[IO].readLine

def withTimeout[A](origin: IO[A], duration: Duration): IO[Option[A]] =
  origin.timeoutAndForget(duration).option

def withFeedback[A](origin: IO[A], show: Show[A]): IO[A] =
  origin.flatTap(Console[IO].println(_)(using show))

def replicate[A](origin: IO[Option[A]]): IO[List[A]] =
  origin.flatMap {
    case Some(a) => replicate(origin).map(a :: _)
    case None    => IO.pure(Nil)
  }
