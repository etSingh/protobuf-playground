package com.example.protobufplayground

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

object Main extends IOApp {
  def run(args: List[String]) =
    ProtobufplaygroundServer.stream[IO].compile.drain.as(ExitCode.Success)
}