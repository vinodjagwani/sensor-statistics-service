package com.luxoft.sensor.statistics
package dto

import zio.{Task, UIO, ZIO}

case class SensorDataDto(sensorId: String, humidity: Option[Int])

object SensorDataDto {
  def apply(csvData: String): Task[SensorDataDto] =
    csvData.split(",").toList match {
      case sensorId :: data => UIO(SensorDataDto(sensorId, data.headOption.flatMap(_.toIntOption)))
      case _ => ZIO.fail(new Exception(s"Failed to parse csv data: $csvData"))
    }
}

