package com.luxoft.sensor.statistics
package service.impl

import dto.SensorDataDto
import service.FileReadService

import zio.blocking.Blocking
import zio.stream.{ZStream, ZTransducer}

import java.io.File

class CVSFileReadService extends FileReadService {

  override def readFile(file: File): ZStream[Blocking, Throwable, SensorDataDto] =
    ZStream
      .fromFile(file.toPath)
      .transduce(ZTransducer.utf8Decode >>> ZTransducer.splitLines)
      .drop(1)
      .mapM(SensorDataDto(_))
}
