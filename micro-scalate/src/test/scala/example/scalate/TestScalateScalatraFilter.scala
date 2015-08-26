package example.scalate

import skinny.micro.{ ScalateSupport, SkinnyMicroFilter }

// The "test" is that this compiles, to avoid repeats of defects like Issue #9.
class TestScalateScalatraFilter extends SkinnyMicroFilter with ScalateSupport
