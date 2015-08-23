package skinny.micro

class SkinnyMicroServerListener extends SkinnyListener {

  override def loadCycleClassName: String = {
    classOf[SkinnyMicroServerBootstrap].getName
  }

}
