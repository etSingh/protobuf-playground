package com.example.protobufplayground

import java.time.Instant

import cats.Applicative
import cats.effect.Sync
import io.circe.{Decoder, Encoder, HCursor}
import io.circe.generic.semiauto._
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe._
import cats.implicits._
import io.circe.Decoder.Result

trait PersonService[F[_]]{
  def get(name: String): F[Person]
}

object PersonService {
  def impl[F[_]: Applicative]: PersonService[F] =
    (name: String) =>
      Person(name, Instant.now).pure[F]
}


final case class Person(name: String, createdAt: Instant)


object PersonCodec {

  implicit val encoder: Encoder[Person] = deriveEncoder[Person]
  implicit def entityEncoder[F[_]: Applicative]: EntityEncoder[F, Person] =
    jsonEncoderOf

  implicit val decoder: Decoder[Person] =
    (c: HCursor) =>
      for {
        name <- c.downField("name").as[String]
        createdAt = c.downField("createdAt").as[Instant].getOrElse(Instant.now())
      } yield Person(name, createdAt)


  implicit def entityDecoder[F[_]: Sync]: EntityDecoder[F, Person] =
    jsonOf

}
