package skinny.micro

/**
 * Unstable access validation configuration.
 */
case class UnstableAccessValidation(
  enabled: Boolean,
  useMostlyStableHttpSession: Boolean,
  createdThreadId: Long = Thread.currentThread.getId)
