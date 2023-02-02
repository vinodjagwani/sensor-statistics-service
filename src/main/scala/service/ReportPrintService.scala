package com.luxoft.sensor.statistics
package service

import zio.{ExitCode, URIO}

trait ReportPrintService {

  def printReport(files: List[String]): URIO[zio.ZEnv, ExitCode]

}
