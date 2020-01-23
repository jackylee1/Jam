package o.lartifa.jam.model.tasks

import cn.hutool.cron.task.Task
import o.lartifa.jam.common.config.{JamConfig, SystemConfig}
import o.lartifa.jam.common.util.MasterUtil
import o.lartifa.jam.cool.qq.listener.RuleEngineListener

/**
 * 调整回复频率
 *
 * Author: sinar
 * 2020/1/23 14:26 
 */
class ChangeRespFrequency(val freq: Int) extends Task {
  override def execute(): Unit = {
    RuleEngineListener.adjustFrequency(freq)
    if (SystemConfig.debugMode) {
      MasterUtil.notifyMaster(s"${JamConfig.name}的回复频率以变更为：$freq%")
    }
  }
}

object ChangeRespFrequency {
  /**
   * 构造器
   *
   * @param freq 回复频率
   * @return 调整回复频率 task 实例
   */
  def apply(freq: Int): ChangeRespFrequency = new ChangeRespFrequency(freq)
}