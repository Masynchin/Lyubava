//> using dep org.typelevel::cats-core:2.10.0
//> using dep org.typelevel::cats-effect:3.5.2
//> using dep com.monovore::decline-effect:2.4.1
//> using dep com.lihaoyi::fansi:0.4.0

import java.util.concurrent.TimeoutException
import scala.util.control.NoStackTrace

import cats.implicits.*
import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.std.{Console, Random}
import com.monovore.decline.*
import com.monovore.decline.effect.*
import com.monovore.decline.time.*
import scala.concurrent.duration.*
import cats.Show
import fansi.Color.{Green, Red}

object Lyubava extends CommandIOApp(
  name = "lyubava",
  header = "Lyubava typewriter game",
  version = "0.1.0",
):
  def main: Opts[IO[ExitCode]] =
    (lengthOption, timeoutOption).mapN(runCats)

  val lengthOption =
    Opts.option[Int](
      "length",
      short="n",
      metavar="symbols",
      help="Answer length to be inputted"
    ).withDefault(4)

  val timeoutOption =
    Opts.option[Duration](
      "timeout", short="t", help="Time to answer"
    ).withDefault(3.seconds)
    
def runCats(length: Int, timeout: Duration) =
  answer(length)
    .mproduct(userAttempt)
    .timeoutAndForget(timeout)
    .ensure(new WrongAnswer)(_ == _)
    .productR(Console[IO].println(Green("Yahoo!")))
    .foreverM
    .handleErrorWith {
      case _: TimeoutException => Console[IO].println(Red("\nTimeout!"))
      case _: WrongAnswer => Console[IO].println(Red("Wrong answer!"))
    }
    .as(ExitCode.Success)

def answer(length: Int): IO[String] =
  Random.scalaUtilRandom[IO]
    .flatMap(_.nextAlphaNumeric)
    .replicateA(length)
    .map(_.mkString)

def userAttempt(answer: String): IO[String] =
  Console[IO].print(s"Type this: ${answer}\n>") *> Console[IO].readLine

final class WrongAnswer extends Exception with NoStackTrace
