package rl
package tests

import org.specs2.Specification
import java.net.IDN
import org.specs2.matcher.DataTables
import org.specs2.execute.Result

import skinny.micro.rl._

class IDNASpec extends Specification with DataTables {
  def is =

    "To deal with Unicode domains" ^
      "should convert from unicode to ASCII" ! jidnTests.runTests(jidnTests.testFromUnicode _) ^
      "should convert from ASCII to unicode" ! jidnTests.runTests(jidnTests.testFromAscii _) ^ end

  val jidnTests = new IDNATests(IDN.toUnicode _, IDN.toASCII _)
  class IDNATests(fromAscii: String => String, fromUnicode: String => String) {

    def testFromUnicode(toencode: String, encoded: String) = {
      fromUnicode(toencode) must_== encoded
    }

    def testFromAscii(encoded: String, toencode: String) = {
      fromAscii(toencode) must_== encoded
    }

    def runTests(tester: (String, String) => Result) = {
      "raw uri" || "ascii uri" |
        "www.google.com" !! "www.google.com" |
        "www.詹姆斯.com" !! "www.xn--8ws00zhy3a.com" |
        "www.iñtërnâtiônàlizætiøn.com" !! "www.xn--itrntinliztin-vdb0a5exd8ewcye.com" |
        "www.ほんとうにながいわけのわからないどめいんめいのらべるまだながくしないとたりない.w3.mag.keio.ac.jp" !! "www.xn--n8jaaaaai5bhf7as8fsfk3jnknefdde3fg11amb5gzdb4wi9bya3kc6lra.w3.mag.keio.ac.jp" |
        "点心和烤鸭.w3.mag.keio.ac.jp" !! "xn--0trv4xfvn8el34t.w3.mag.keio.ac.jp" |
        "가각갂갃간갅갆갇갈갉힢힣.com" !! "xn--o39acdefghijk5883jma.com" |
        "リ宠퐱卄.com" !! "xn--eek174hoxfpr4k.com" |
        "ᆵ" !! "xn--4ud" |> tester
    }

  }

}