package skinny.micro.data

import scala.collection.{ immutable, mutable }

/**
 * @see MapWithIndifferentAccess
 */
trait MutableMapWithIndifferentAccess[B]
  extends MapWithIndifferentAccess[B]
  with mutable.Map[String, B] {

  def update(key: Symbol, value: B): Unit = { update(key.name, value) }

  protected def mutableMap: mutable.Map[String, B]

  protected def multiMap: immutable.Map[String, Seq[B]] = {
    mutableMap.map { case (k: String, v) => k -> Seq(v) }.toMap[String, Seq[B]]
  }

  override def addOne(elem: (String, B)): this.type = {
    mutableMap.addOne(elem)
    this
  }

  override def clear(): Unit = {
    mutableMap.clear()
  }

  override def subtractOne(elem: String): this.type = {
    mutableMap.subtractOne(elem)
    this
  }

}
