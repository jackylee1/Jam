package o.lartifa.jam.model.tasks

import o.lartifa.jam.common.config.{JamConfig, SystemConfig}
import o.lartifa.jam.common.util.MasterUtil
import o.lartifa.jam.pool.JamContext

/**
 * 作息时间 - 睡眠
 *
 * Author: sinar
 * 2020/1/23 14:10 
 */
class GoASleep extends JamCronTask(name = "睡眠") {
  override def execute(): Unit = {
    JamContext.clientConfig.get().setHttpPaused(true)
    JamContext.clientConfig.get().setEventPaused(true)
    if (SystemConfig.debugMode) {
      MasterUtil.notifyMaster(s"${JamConfig.name} 已经休眠")
    }
    JamContext.logger.get().log(s"${JamConfig.name} 已经休眠")
  }
}
