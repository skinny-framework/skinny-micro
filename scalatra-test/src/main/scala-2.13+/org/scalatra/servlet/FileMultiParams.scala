package org.scalatra.servlet

class FileMultiParams(wrapped: Map[String, Seq[FileItem]] = Map.empty) extends Map[String, Seq[FileItem]] {

  def get(key: String): Option[Seq[FileItem]] = {
    (wrapped.get(key) orElse wrapped.get(key + "[]"))
  }

  def get(key: Symbol): Option[Seq[FileItem]] = get(key.name)

  def iterator = wrapped.iterator

  override def default(a: String): Seq[FileItem] = wrapped.default(a)

  override def removed(key: String): Map[String, Seq[FileItem]] = {
    wrapped - key
  }

  override def updated[V1 >: Seq[FileItem]](key: String, value: V1): Map[String, V1] = {
    wrapped + (key -> value)
  }

}

object FileMultiParams {

  def apply() = new FileMultiParams

  def apply[SeqType <: Seq[FileItem]](wrapped: Map[String, Seq[FileItem]]) =
    new FileMultiParams(wrapped)
}
