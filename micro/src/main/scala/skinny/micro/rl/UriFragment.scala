package skinny.micro.rl

object UriFragment {

  def apply(rawValue: String): UriFragment = {
    rawValue.blankOption.map(StringFragment(_)).getOrElse(EmptyFragment)
  }

}

trait UriFragment extends UriNode {

  type Value

  def rawValue: String

  def value: Value

  def normalize: UriFragment = this

  def apply() = value.toString

}

case object EmptyFragment extends UriFragment {

  val uriPart = ""

  val value = ""

  val rawValue = ""

  type Value = String

}

case class StringFragment(rawValue: String) extends UriFragment {

  def uriPart = value.blankOption map { "#" + _ } getOrElse ""

  val value = rawValue

  type Value = String
}
