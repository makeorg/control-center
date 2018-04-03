package org.make.backoffice.facade

import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object React {
  def cloneElement(reactClass: ReactClass, props: js.Any): ReactElement =
    NativeReact.cloneElement(reactClass, props)
}

@js.native
@JSImport("react", JSImport.Default)
object NativeReact extends js.Object {
  def cloneElement(reactClass: ReactClass, props: js.Any): ReactElement = js.native
}
