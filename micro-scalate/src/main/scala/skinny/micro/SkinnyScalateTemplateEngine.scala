package skinny.micro

import java.io.File

import org.fusesource.scalate.TemplateEngine

class SkinnyScalateTemplateEngine(
  sourceDirectories: Traversable[File] = None,
  mode: String = sys.props.getOrElse("scalate.mode", "production")) extends TemplateEngine(sourceDirectories, mode)
