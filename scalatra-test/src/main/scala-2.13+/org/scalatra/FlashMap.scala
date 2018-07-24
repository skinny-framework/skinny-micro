package org.scalatra

import util.MutableMapWithIndifferentAccess

import collection.JavaConverters._
import java.util.concurrent.{ ConcurrentHashMap, ConcurrentSkipListSet }

import scala.collection.mutable

/**
 * A FlashMap is the data structure used by org.scalatra.FlashMapSupport
 * to allow passing temporary values between sequential actions.
 *
 * FlashMap behaves like [[org.scalatra.util.MapWithIndifferentAccess]].  By
 * default, anything placed in the map is available to the current request and
 * next request, and is then discarded.
 *
 * @see FlashMapSupport
 */
class FlashMap extends MutableMapWithIndifferentAccess[Any] with Serializable {

  private[this] val m = new ConcurrentHashMap[String, Any]().asScala

  private[this] val flagged = new ConcurrentSkipListSet[String]().asScala

  override protected def mutableMap: mutable.Map[String, Any] = m

  /**
   * Creates a new iterator over the values of the flash map.  These are the
   * values that were added during the last request.
   */
  override def iterator = new Iterator[(String, Any)] {
    private[this] val it = m.iterator

    def hasNext = it.hasNext

    def next = {
      val kv = it.next
      flagged += kv._1
      kv
    }
  }

  /**
   * Returns the value associated with a key and flags it to be swept.
   */
  override def get(key: String) = {
    flagged += key
    m.get(key)
  }

  /**
   * Removes all flagged entries.
   */
  def sweep(): Unit = {
    flagged foreach { key => m -= key }
  }

  /**
   * Clears all flags so no entries are removed on the next sweep.
   */
  def keep(): Unit = {
    flagged.clear()
  }

  /**
   * Clears the flag for the specified key so its entry is not removed on the next sweep.
   */
  def keep(key: String): Unit = {
    flagged -= key
  }

  /**
   * Flags all current keys so the entire map is cleared on the next sweep.
   */
  def flag(): Unit = {
    flagged ++= m.keys
  }

  /**
   * Sets a value for the current request only.  It will be removed before the next request unless explicitly kept.
   * Data put in this object is availble as usual:
   * {{{
   * flash.now("notice") = "logged in succesfully"
   * flash("notice") // "logged in succesfully"
   * }}}
   */
  object now {
    def update(key: String, value: Any) = {
      flagged += key
      m += key -> value
    }
  }

}
