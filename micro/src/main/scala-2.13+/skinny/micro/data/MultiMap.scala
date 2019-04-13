package skinny.micro.data

import scala.collection.immutable.Map

import scala.language.implicitConversions

object MultiMap {

  def apply() = new MultiMap

  def apply[SeqType <: Seq[String]](wrapped: Map[String, SeqType]) = new MultiMap(wrapped)

  def empty = apply()

  implicit def map2MultiMap(map: Map[String, Seq[String]]) = new MultiMap(map)

}

class MultiMap(wrapped: Map[String, Seq[String]] = Map.empty) extends Map[String, Seq[String]] {

  def get(key: String): Option[Seq[String]] = {
    (wrapped.get(key) orElse wrapped.get(key + "[]"))
  }

  def get(key: Symbol): Option[Seq[String]] = get(key.name)

  override def +[B1 >: Seq[String]](kv: (String, B1)) = new MultiMap(wrapped + kv.asInstanceOf[(String, Seq[String])])

  def iterator = wrapped.iterator

  override def default(a: String): Seq[String] = wrapped.default(a)

  override def removed(key: String): Map[String, Seq[String]] = {
    new MultiMap(wrapped - key)
  }

  override def updated[V1 >: Seq[String]](key: String, value: V1): Map[String, V1] = {
    value match {
      case vs: Seq[_] => new MultiMap(wrapped + (key -> vs.map(_.toString)))
      case _ => new MultiMap(wrapped + (key -> Seq(value.toString))) // NOTE: this is not a correct behavior
    }
  }

}
