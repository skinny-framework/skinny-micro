package rl
package tests

import skinny.micro.rl._
import org.specs2.Specification
import org.specs2.matcher._

class UriPathSpec extends Specification {
  def is =

    "Normalizing windows paths should" ^
      "convert a local path 'C:\\Windows\\Temp\\foo.txt'" ! normalizeLocalWindowsPath ^
      "convert a relative path 'Windows\\Temp\\foo.txt'" ! normalizeRelativeWindowsPath ^
      "convert a UNC path '\\\\theserver\\theshare\\thefile.txt" ! normalizeUncWindowsPath ^
      "Encode the spaces in a path" ! normalizeSpacesInPath ^ end

  def normalizeLocalWindowsPath = {
    UriPath.windowsToUnixPath("C:\\Windows\\Temp\\foo.txt") must_== "file:///C:/Windows/Temp/foo.txt"
  }

  def normalizeRelativeWindowsPath = {
    UriPath.windowsToUnixPath("Windows\\Temp\\foo.txt") must_== "Windows/Temp/foo.txt"
  }

  def normalizeUncWindowsPath = {
    UriPath.windowsToUnixPath("\\\\theserver\\theshare\\thefile.txt") must_== "file://theserver/theshare/thefile.txt"
  }

  def normalizeSpacesInPath = {
    UriPath.windowsToUnixPath("C:\\Windows\\Program Files\\blah.txt") must_== "file:///C:/Windows/Program%20Files/blah.txt"
  }
}

class AboutAbsolutePaths extends Specification with DataTables with ResultMatchers {
  def is =
    "Absolute paths" ^
      "always start and end with a slash unless they are empty" ! {
        val empty: List[String] = List()
        "path segments" | "expected" |>
          empty ! "" |
          List("a") ! "/a/" |
          List("a", "b") ! "/a/b/" |
          { (segments, expected) => AbsolutePath(segments).uriPart must_== expected }
      } ^ end
}
