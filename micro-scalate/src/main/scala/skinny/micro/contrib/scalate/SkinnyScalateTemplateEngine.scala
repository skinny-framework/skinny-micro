package skinny.micro.contrib.scalate

import java.io.File

import org.fusesource.scalate.TemplateEngine

class SkinnyScalateTemplateEngine(
  sourceDirectories: Iterable[File] = Iterable.empty,
  mode: String = sys.props.getOrElse("scalate.mode", "production")) extends TemplateEngine(sourceDirectories, mode)
