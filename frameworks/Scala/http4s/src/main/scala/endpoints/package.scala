package http4s.techempower.benchmark

import com.github.plokhotnyuk.jsoniter_scala.core.{ JsonValueCodec, writeToArray }
import com.github.plokhotnyuk.jsoniter_scala.macros.{ CodecMakerConfig, JsonCodecMaker }
import http4s.techempower.benchmark.models.Message
import org.http4s.{ Charset, EntityEncoder, MediaType }
import org.http4s.headers.`Content-Type`


package object endpoints {

  implicit def jsonEncoder[F[_], T: JsonValueCodec]: EntityEncoder[F, T] =
    EntityEncoder
      .byteArrayEncoder[F]
      .contramap((data: T) => writeToArray(data))
      .withContentType(`Content-Type`(MediaType.application.json, Some(Charset.`UTF-8`)))

}

package endpoints {

  object jsonCodecs {

    implicit val messageCodec: JsonValueCodec[Message] = JsonCodecMaker.make(CodecMakerConfig())

  }

}
