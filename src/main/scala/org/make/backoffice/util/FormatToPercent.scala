package org.make.backoffice.util

object FormatToPercent {
  def formatToPercent(count: Int, total: Int): Int = {
    if (count == 0) 0
    else if (total == 0) 100
    else count * 100 / total
  }
}
