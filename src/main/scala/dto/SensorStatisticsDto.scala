package com.luxoft.sensor.statistics
package dto

case class HumidityData(min: Option[Int], avg: Option[Int], max: Option[Int])

case class SensorStatisticsData(successfulMeasurements: Long, failedMeasurements: Long, humidityData: List[(String, HumidityData)])
