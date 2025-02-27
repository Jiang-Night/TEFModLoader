package silkways.terraria.efmodloader.logic

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.util.Log
import java.util.Locale
import android.os.Process

/*******************************************************************************
 * 文件名称: EFLog
 * 项目名称: TEFModLoader
 * 创建时间: 2024/11/3 01:54
 * 作者: EternalFuture゙
 * Github: https://github.com/2079541547
 * 版权声明: Copyright © 2024 EternalFuture゙. All rights reserved.
 * 许可证: This program is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU Affero General Public License as published
 *         by the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         This program is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU Affero General Public License for more details.
 *
 *         You should have received a copy of the GNU Affero General Public License
 *         along with this program. If not, see <https://www.gnu.org/licenses/>.
 * 描述信息: 本文件为TEFModLoader项目中的一部分。
 * 注意事项: 请严格遵守GNU AGPL v3.0协议使用本代码，任何未经授权的商业用途均属侵权行为。
 *******************************************************************************/

object EFLog {

    // 定义一个常量TAG，用于日志输出时的标记，便于过滤查看
    private const val TAG = "TEFModLoader"

    // 使用默认区域设置创建日期格式化对象，用于格式化日志的时间戳
    @SuppressLint("ConstantLocale") // 忽略lint关于常量区域设置的警告
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    /**
     * 打印调试级别的日志信息。
     * @param message 需要打印的日志消息
     */
    fun d(message: String) {
        log(Log.DEBUG, message)
    }

    /**
     * 打印信息级别的日志信息。
     * @param message 需要打印的日志消息
     */
    fun i(message: String) {
        log(Log.INFO, message)
    }

    /**
     * 打印警告级别的日志信息。
     * @param message 需要打印的日志消息
     */
    fun w(message: String) {
        log(Log.WARN, message)
    }

    /**
     * 打印错误级别的日志信息。
     * @param message 需要打印的日志消息
     */
    fun e(message: String) {
        log(Log.ERROR, message)
    }

    /**
     * 实际执行日志记录的方法。
     * @param level 日志级别（如 DEBUG, INFO, WARN, ERROR）
     * @param message 日志消息
     */
    private fun log(level: Int, message: String) {
        // 获取当前线程的堆栈跟踪信息
        val stackTrace = Thread.currentThread().stackTrace

        // 从堆栈跟踪中找到调用此日志方法的实际类和方法
        var targetElement: StackTraceElement? = null
        for (element in stackTrace) {
            // 跳过EFLog类自身的调用
            if (element.className != EFLog::class.java.name && element.className.startsWith("silkways.terraria.efmodloader")) {
                targetElement = element
                break
            }
        }

        // 如果找到了目标调用者，则构建日志消息并根据级别打印
        targetElement?.let {
            val pid = Process.myPid() // 获取当前进程ID
            // 构建完整的日志消息，包括时间戳、进程ID、文件名、行号、类名、方法名以及实际的消息
            val logMessage = "${dateFormat.format(System.currentTimeMillis())} [PID: $pid] [${it.fileName}:${it.lineNumber}] ${it.className}.${it.methodName} - $message"
            // 根据不同的日志级别调用不同的Log.x方法
            when (level) {
                Log.DEBUG -> Log.d(TAG, logMessage)
                Log.INFO -> Log.i(TAG, logMessage)
                Log.WARN -> Log.w(TAG, logMessage)
                Log.ERROR -> Log.e(TAG, logMessage)
            }
        } ?: run {
            // 如果没有找到调用者信息，打印错误日志
            Log.e(TAG, "Could not find the caller information.")
        }
    }
}