package skinny.micro.request

import java.io.BufferedReader
import java.security.Principal
import java.text.{ DateFormat, SimpleDateFormat }
import java.util.Locale
import javax.servlet._
import javax.servlet.http._
import skinny.logging.LoggerProvider
import skinny.micro.{ UnstableAccessException, UnstableAccessValidation }

import scala.collection.JavaConverters._
import scala.collection.concurrent.TrieMap

import scala.util.Try

/**
 * Stable HttpServletRequest
 *
 * HttpServletRequest object can be recycled.
 *
 * see also: https://github.com/scalatra/scalatra/pull/514
 * see also: http://jetty.4.x6.nabble.com/jetty-users-getContextPath-returns-null-td4962387.html
 * see also: https://bugs.eclipse.org/bugs/show_bug.cgi?id=433321
 */
class StableHttpServletRequest(
  private val underlying: HttpServletRequest,
  private val unstableAccessValidation: UnstableAccessValidation) extends HttpServletRequestWrapper(underlying) with LoggerProvider {

  private[this] var stableHttpSession: Option[HttpSession] = {
    Option(underlying.getSession(false)) match {
      case Some(_) => Some(MostlyStableHttpSession(underlying))
      case _ => None
    }
  }

  private[this] def ensureStableAccessStrictly(attributeName: String): Unit = {
    if (unstableAccessValidation.enabled) {
      val threadId = Thread.currentThread.getId
      if (unstableAccessValidation.createdThreadId != threadId) {
        throw new UnstableAccessException(attributeName)
      }
    }
  }

  // -------------------------
  // context, fixed request metadata

  override def getServletContext: ServletContext = {
    ensureStableAccessStrictly("getServletContext")
    underlying.getServletContext
  }

  // AsyncContext must not be cached
  override def getAsyncContext: AsyncContext = {
    ensureStableAccessStrictly("getAsyncContext")
    underlying.getAsyncContext
  }

  override def startAsync(): AsyncContext = {
    ensureStableAccessStrictly("startAsync")
    underlying.startAsync()
  }
  override def startAsync(
    servletRequest: ServletRequest,
    servletResponse: ServletResponse): AsyncContext = {
    ensureStableAccessStrictly("startAsync")
    underlying.startAsync(servletRequest, servletResponse)
  }

  private[this] var _getRequest = super.getRequest
  override def getRequest: ServletRequest = _getRequest
  override def setRequest(request: ServletRequest): Unit = {
    _getRequest.synchronized {
      super.setRequest(request)
      _getRequest = request
    }
  }

  override def isWrapperFor(wrapped: ServletRequest): Boolean = super.isWrapperFor(wrapped)
  override def isWrapperFor(wrappedType: Class[_]): Boolean = super.isWrapperFor(wrappedType)

  private[this] val _getDispatcherType = underlying.getDispatcherType
  override def getDispatcherType: DispatcherType = _getDispatcherType

  override def getReader: BufferedReader = underlying.getReader

  override def getInputStream: ServletInputStream = underlying.getInputStream

  private[this] val _getAuthType = underlying.getAuthType
  override def getAuthType: String = _getAuthType

  private[this] val _getMethod = underlying.getMethod
  override def getMethod: String = _getMethod

  private[this] val _getPathInfo = underlying.getPathInfo
  override def getPathInfo: String = _getPathInfo

  private[this] val _getPathTranslated = underlying.getPathTranslated
  override def getPathTranslated: String = _getPathTranslated

  private[this] val _getContextPath = underlying.getContextPath
  override def getContextPath: String = _getContextPath

  private[this] val _getQueryString = underlying.getQueryString
  override def getQueryString: String = _getQueryString

  private[this] val _getRemoteUser = underlying.getRemoteUser
  override def getRemoteUser: String = _getRemoteUser

  private[this] val _getRequestedSessionId = underlying.getRequestedSessionId
  override def getRequestedSessionId: String = _getRequestedSessionId

  private[this] val _getRequestURI = underlying.getRequestURI
  override def getRequestURI: String = _getRequestURI

  private[this] val _getServletPath = underlying.getServletPath
  override def getServletPath: String = _getServletPath

  private[this] val _isRequestedSessionIdValid = underlying.isRequestedSessionIdValid
  override def isRequestedSessionIdValid: Boolean = _isRequestedSessionIdValid

  private[this] val _isRequestedSessionIdFromCookie = underlying.isRequestedSessionIdFromCookie
  override def isRequestedSessionIdFromCookie: Boolean = _isRequestedSessionIdFromCookie

  private[this] val _isRequestedSessionIdFromURL = underlying.isRequestedSessionIdFromURL
  override def isRequestedSessionIdFromURL: Boolean = _isRequestedSessionIdFromURL
  override def isRequestedSessionIdFromUrl: Boolean = isRequestedSessionIdFromURL

  private[this] var _getCharacterEncoding = underlying.getCharacterEncoding
  override def getCharacterEncoding: String = _getCharacterEncoding
  override def setCharacterEncoding(enc: String): Unit = {
    _getCharacterEncoding.synchronized {
      underlying.setCharacterEncoding(enc)
      _getCharacterEncoding = enc
    }
  }

  private[this] val _getContentLength = underlying.getContentLength
  override def getContentLength: Int = _getContentLength

  private[this] val _getContentType = underlying.getContentType
  override def getContentType: String = _getContentType

  private[this] val _getProtocol = underlying.getProtocol
  override def getProtocol: String = _getProtocol

  // java.lang.IllegalStateException: No uri on Jetty when testing
  private[this] val _getServerName = Try(underlying.getServerName).getOrElse(null)
  override def getServerName: String = _getServerName

  private[this] val _getScheme = underlying.getScheme
  override def getScheme: String = _getScheme

  // java.lang.IllegalStateException: No uri on Jetty when testing
  private[this] val _getServerPort = Try(underlying.getServerPort).getOrElse(-1)
  override def getServerPort: Int = _getServerPort

  private[this] val _getRemoteAddr = underlying.getRemoteAddr
  override def getRemoteAddr: String = _getRemoteAddr

  private[this] val _getRemoteHost = underlying.getRemoteHost
  override def getRemoteHost: String = _getRemoteHost

  private[this] val _isSecure = underlying.isSecure
  override def isSecure: Boolean = _isSecure

  private[this] val _getRemotePort = underlying.getRemotePort
  override def getRemotePort: Int = _getRemotePort

  private[this] val _getLocalName = underlying.getLocalName
  override def getLocalName: String = _getLocalName

  private[this] val _getLocalAddr = underlying.getLocalAddr
  override def getLocalAddr: String = _getLocalAddr

  private[this] val _getLocalPort = underlying.getLocalPort
  override def getLocalPort: Int = _getLocalPort

  private[this] val _isAsyncStarted = underlying.isAsyncStarted
  override def isAsyncStarted: Boolean = _isAsyncStarted

  private[this] val _isAsyncSupported = underlying.isAsyncSupported
  override def isAsyncSupported: Boolean = _isAsyncSupported

  // java.lang.IllegalStateException: No uri on Jetty when testing
  private[this] val _getRequestURL = Try(underlying.getRequestURL).getOrElse(new StringBuffer)
  override def getRequestURL: StringBuffer = _getRequestURL

  private[this] val _getCookies = underlying.getCookies
  override def getCookies: Array[Cookie] = _getCookies

  private[this] val _getUserPrincipal = underlying.getUserPrincipal
  override def getUserPrincipal: Principal = _getUserPrincipal

  // should not simply cache the value: java.lang.IllegalStateException: No SessionManager
  private[this] def getOrInitializeSession(create: Boolean): HttpSession = {
    stableHttpSession.synchronized {
      stableHttpSession.getOrElse {
        val session = MostlyStableHttpSession(underlying)
        stableHttpSession = Some(session)
        session
      }
    }
  }
  override def getSession: HttpSession = {
    if (unstableAccessValidation.useMostlyStableHttpSession) {
      getOrInitializeSession(false)
    } else {
      ensureStableAccessStrictly("getSession")
      underlying.getSession
    }
  }
  override def getSession(create: Boolean): HttpSession = {
    if (unstableAccessValidation.useMostlyStableHttpSession) {
      // NOTE: this operation cannot be completely safe
      getOrInitializeSession(create)
    } else {
      ensureStableAccessStrictly("getSession")
      underlying.getSession(create)
    }
  }

  override def authenticate(response: HttpServletResponse): Boolean = {
    ensureStableAccessStrictly("authenticate")
    underlying.authenticate(response)
  }
  override def logout(): Unit = {
    ensureStableAccessStrictly("logout")
    underlying.logout()
  }

  override def isUserInRole(role: String): Boolean = {
    ensureStableAccessStrictly("isUserInRole")
    underlying.isUserInRole(role)
  }
  override def login(username: String, password: String): Unit = {
    ensureStableAccessStrictly("login")
    underlying.login(username, password)
  }

  // Don't override getParts
  // javax.servlet.ServletException: Content-Type != multipart/form-data
  override def getParts: java.util.Collection[Part] = {
    ensureStableAccessStrictly("getParts")
    underlying.getParts
  }
  override def getPart(name: String): Part = {
    ensureStableAccessStrictly("getPart")
    underlying.getPart(name)
  }

  // deprecated
  // override def getRealPath(path: String): String = underlying.getRealPath(path)

  // -------------------------
  // parameters

  private[this] val _getParameterNames = underlying.getParameterNames
  private[this] val _getParameterMap = underlying.getParameterMap

  override def getParameterNames: java.util.Enumeration[String] = _getParameterNames
  override def getParameterMap: java.util.Map[String, Array[String]] = _getParameterMap
  override def getParameter(name: String): String = Option(getParameterMap.get(name)).flatMap(_.headOption).orNull[String]
  override def getParameterValues(name: String): Array[String] = getParameterMap.get(name)

  override def getRequestDispatcher(path: String): RequestDispatcher = underlying.getRequestDispatcher(path)

  // -------------------------
  // locale

  private[this] val _getLocale = underlying.getLocale
  private[this] val _getLocales = underlying.getLocales

  override def getLocale: Locale = _getLocale
  override def getLocales: java.util.Enumeration[Locale] = _getLocales

  // -------------------------
  // attributes

  private[this] def _getAttributeNames: java.util.Enumeration[String] = _attributes.keys.iterator.asJavaEnumeration
  private[this] val _attributes: TrieMap[String, AnyRef] = {
    val result = new TrieMap[String, AnyRef]
    Option(underlying.getAttributeNames).map(_.asScala).foreach { names =>
      names.foreach { name =>
        val value = underlying.getAttribute(name)
        if (value != null) {
          result.put(name, value)
        }
      }
    }
    result
  }

  override def getAttributeNames: java.util.Enumeration[String] = _getAttributeNames
  override def getAttribute(name: String): AnyRef = _attributes.getOrElse(name, null)
  override def setAttribute(name: String, o: scala.Any): Unit = {
    underlying.setAttribute(name, o)
    Option(o).foreach(v => _attributes.put(name, v.asInstanceOf[AnyRef]))
  }
  override def removeAttribute(name: String): Unit = {
    underlying.removeAttribute(name)
    _attributes.remove(name)
  }

  // -------------------------
  // headers

  // request headers are immutable
  private[this] val _getHeaderNames = underlying.getHeaderNames
  private[this] val _cachedGetHeader: Map[String, String] = {
    Option(underlying.getHeaderNames)
      .map(_.asScala.map(name => name -> underlying.getHeader(name)).filterNot { case (_, v) => v == null }.toMap)
      .getOrElse(Map.empty)
  }
  private[this] val _cachedGetHeaders: Map[String, java.util.Enumeration[String]] = {
    Option(underlying.getHeaderNames)
      .map(_.asScala.map(name => name -> underlying.getHeaders(name)).filterNot { case (_, v) => v == null }.toMap)
      .getOrElse(Map.empty)
  }
  private[this] val _cachedGetDateHeaders: Map[String, Option[Long]] = {
    Option(underlying.getHeaderNames)
      .map { names =>
        names.asScala.map { name =>
          // NOTE: Treat "-1" as None to try skinny-micro's parser later - see also: #getDateHeader(String)
          val maybeValue = Try(underlying.getDateHeader(name)).toOption.filter(_ != getDateHeaderDefaultValue)
          name -> maybeValue
        }.toMap
      }.getOrElse(Map.empty)
  }

  override def getHeaderNames: java.util.Enumeration[String] = _getHeaderNames
  override def getHeader(name: String): String = _cachedGetHeader.get(name).orNull[String]
  override def getIntHeader(name: String): Int = {
    // an integer expressing the value of the request header or -1 if the request doesn't have a header of this name
    _cachedGetHeader.get(name).map(_.toInt).getOrElse(-1)
  }
  override def getHeaders(name: String): java.util.Enumeration[String] = {
    // If the request does not have any headers of that name return an empty enumeration
    _cachedGetHeaders.get(name).getOrElse(java.util.Collections.emptyEnumeration[String]())
  }
  override def getDateHeader(name: String): Long = {
    // -1 if the named header was not included with the request
    _cachedGetDateHeaders.get(name) match {
      case Some(Some(found)) => found
      case _ => _cachedGetHeader.get(name).flatMap(parseDateHeader).getOrElse(getDateHeaderDefaultValue)
    }
  }

  private[this] val getDateHeaderDefaultValue: Long = -1L

  // https://github.com/apache/tomcat/blob/TOMCAT_8_0_0/java/org/apache/catalina/connector/Request.java#L178-L180
  // https://github.com/spring-projects/spring-security/blob/master/web/src/main/java/org/springframework/security/web/savedrequest/SavedRequestAwareWrapper.java#L84-L86
  private[this] def dateHeaderFormats: Array[DateFormat] = Array(
    new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US), // RFC1123
    new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
    new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US))

  private[this] def parseDateHeader(headerValue: String): Option[Long] = {
    if (headerValue == null) {
      None
    } else {
      // NOTE: Possibly an IE 10 style value: "Wed, 09 Apr 2014 09:57:42 GMT; length=13774"
      // Thanks to https://github.com/spring-projects/spring-framework/blob/v4.2.4.RELEASE/spring-web/src/main/java/org/springframework/web/context/request/ServletWebRequest.java#L279
      headerValue.split(";").headOption.map(_.trim) match {
        case Some(value) =>
          dateHeaderFormats.foldLeft[Option[Long]](None) {
            case (alreadyParsed: Some[Long], _) => alreadyParsed
            case (_, format) => Try(format.parse(value)).map(_.getTime).toOption
          }
        case _ => None
      }
    }
  }

}

object StableHttpServletRequest {

  def apply(req: HttpServletRequest, unstableAccessValidation: UnstableAccessValidation): StableHttpServletRequest = {
    if (req == null) {
      throw new IllegalStateException("Use AsyncResult { ... } or FutureWithContext { implicit ctx => ... } instead.")
    } else if (req.isInstanceOf[StableHttpServletRequest]) {
      req.asInstanceOf[StableHttpServletRequest]
    } else {
      new StableHttpServletRequest(req, unstableAccessValidation)
    }
  }
}
