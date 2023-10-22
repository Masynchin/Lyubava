# Любава

Lyubava - is a console game where you type displayed words in certain amount
of time.

## Running

You should have `git` and either `sbt` or `scala-cli` installed.

Clone this repo:

~~~shell
git clone https://github.com/Masynchin/Lyubava.git .
cd Lyubava
~~~

And run with `sbt`:

~~~shell
sbt run
~~~

Or with `scala-cli`:

~~~shell
scala-cli run src/main/scala/Main.scala
~~~

## Options

You can specify amount of symbols in answer to be typed with `-l`/`--length`
option, and time to type with option `-t`/`--timeout`:

~~~shell
scala-cli run src/main/scala/Main.scala -- --length 10 --timeout 7s
~~~
