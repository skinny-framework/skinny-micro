// The MIT License (MIT)　Copyright (c) 2011 Mojolly Ltd.
package skinny.micro.rl

import java.net.{ URLDecoder, URI, URLEncoder }
import java.util.Date

object Benchmark extends App {

  val toEncode = "I'm a very long $tring with twitter://casualjim in, it"
  val encoded = UrlCodingUtils.urlEncode(toEncode)

  val url = "http://www.詹姆斯.org/path/to/somewhere/?id=45&dskafd=safla&sdkfa=sd#dksd$sdl"

  println("string to encode: " + toEncode)
  println("url to parse: " + url)
  println("java encoded: " + URLEncoder.encode(toEncode, "UTF-8"))
  println("java parsed: " + URI.create(url).toASCIIString)
  println("rl encoded: " + UrlCodingUtils.urlEncode(toEncode))
  println("rl parsed: " + Uri(url).asciiString)

  // warm-up
  (1 to 500) foreach { _ =>
    URLDecoder.decode(URLEncoder.encode(toEncode, "UTF-8"), "UTF-8")
    new URI(url)
  }

  (1 to 500) foreach { _ =>
    UrlCodingUtils.urlDecode(UrlCodingUtils.urlEncode(toEncode))
    Uri(url)
  }

  println("Start url encoding bench")
  val start = new Date

  (1 to 1000000) foreach { _ =>
    URLEncoder.encode(toEncode, "UTF-8")
  }
  val jnetEnd = new Date

  val rlstart = new Date

  (1 to 1000000) foreach { _ =>
    UrlCodingUtils.urlEncode(toEncode)
  }

  val end = new Date

  println("Start url decoding bench")

  val decStart = new Date
  (1 to 1000000) foreach { _ =>
    URLDecoder.decode(encoded, "UTF-8")
  }

  val decJEnd = new Date
  val decRlStart = new Date
  (1 to 1000000) foreach { _ =>
    UrlCodingUtils.urlDecode(encoded)
  }

  val decRlEnd = new Date

  val decJTook = decJEnd.getTime - decStart.getTime
  val decRlTook = decRlEnd.getTime - decRlStart.getTime

  println("Starting uri parsing bench")
  val jurlstart = new Date

  (1 to 100000) foreach { _ =>
    new URI(url).toASCIIString
  }

  val jurl = new Date

  val uristart = new Date

  (1 to 100000) foreach { _ =>
    Uri(url).asciiString
  }

  val uriend = new Date

  println("")
  val javaTook = jnetEnd.getTime - start.getTime
  val rlTook = end.getTime - rlstart.getTime
  val juriTook = jurl.getTime - jurlstart.getTime
  val rluriTook = uriend.getTime - uristart.getTime
  println("Started: %s" format start)
  println("To encode 1000000 uri's")
  println("Java took: %s millis" format javaTook)
  println("RL took: %s millis" format rlTook)
  println("")
  println("To decode 1000000 uri's")
  println("Java took: %s millis" format decJTook)
  println("RL took: %s millis" format decRlTook)
  println("")
  println("To parse 100000 uri's")
  println("java took: %s millis" format juriTook)
  println("rl took: %s millis" format rluriTook)

}
