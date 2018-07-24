package org.scalatra.util

import scala.collection.immutable.Map

object MultiMapHeadView {

  def empty[A, B]: MultiMapHeadView[A, B] = {
    new MultiMapHeadView[A, B] {
      override protected val multiMap = Map.empty[A, Seq[B]]
    }
  }

  def emptyIndifferent[B]: MultiMapHeadView[String, B] with MapWithIndifferentAccess[B] = {
    new MultiMapHeadView[String, B] with MapWithIndifferentAccess[B] {
      override protected val multiMap = Map.empty[String, Seq[B]]
    }
  }

}

trait MultiMapHeadView[A, B] extends Map[A, B] {

  protected def multiMap: Map[A, Seq[B]]

  override def get(key: A): Option[B] = multiMap.get(key) flatMap { _.headOption }

  override def size: Int = multiMap.size

  override def iterator: Iterator[(A, B)] = {
    multiMap.filterNot(_._2.isEmpty).map { case (k, v: Seq[B]) => (k, v.head) }.iterator
  }

  override def +[B1 >: B](kv: (A, B1)): Map[A, B1] = Map() ++ this + kv

  override def remove(key: A): Map[A, B] = {
    (multiMap - key).filterNot(_._2.isEmpty).map { case (k, vs) => k -> vs.head }
  }

  override def updated[V1 >: B](key: A, value: V1): Map[A, V1] = {
    multiMap
      .filterNot(_._2.isEmpty)
      .map { case (k, vs: Seq[V1]) => k -> vs.head }
      .updated(key, value)
  }

}

