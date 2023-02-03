package edu.yjzxc.universeimserver.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnowFlake {

    /**
     * 组成部分
     */

    // 一个固定的时间戳 2023-02-03 04:55:46
    private static final long fixedTimeStamp = 1675371346L;

    // 机房Id
    private static long computerRoomId;

    @Value("${computerRoomIdConfig}")
    public void setComputerRoomId(long computerRoomIdConfig) {
        computerRoomId = computerRoomIdConfig;
    }

    // 机器Id
    private static long machineId;

    @Value("${machineIdConfig}")
    public void setMachineId(long machineIdConfig) {
        machineId = machineIdConfig;
    }

    // 序列号
    private static long sequence = 0L;

    /**
     * 所占的bit大小
     */

    // 时间戳在高位 所以就是其余组成部分所占bit剩余的部分

    // 机房Id占用bit数量
    private static final long computerRoomIdBitCnt = 2L;

    // 机器Id占用bit数量
    private static final long machineIdBitCnt = 2L;

    // 序列号占用bit数量
    private static final long sequenceBitCnt = 3L;

    /**
     * 各组成部分需要位移的位数
     */

    // 机器Id需要向左移的位数
    private static final long machineIdShift = sequenceBitCnt;

    // 机房Id需要向左移的位数
    private static final long computerRoomIdShift = machineIdShift + machineIdBitCnt;

    // 时间戳需要向左移的位数
    private static final long timeStampShift = computerRoomIdShift + computerRoomIdBitCnt;

    // 支持最大的机房Id大小
    private static final long maxComputerRoomId = -1 ^ (-1 << computerRoomIdBitCnt);

    // 支持最大的机器Id大小
    private static final long maxMachineId = -1 ^ (-1 << machineIdBitCnt);

    // 序列号掩码
    private static final long sequenceMask = -1 ^ (-1 << sequenceBitCnt);

    // 上一次生成的时间戳
    private static long lastTimeStamp = -1L;

    /**
     * 返回当前时间(秒级)
     * @return
     */
    private static long getCurrentTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static synchronized long getNextId() {
        long currentTimeStamp = getCurrentTime();

        // 同一秒内
        if(currentTimeStamp == lastTimeStamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 表示轮了一圈 等待下一秒再生成一个Id
            if(sequence == 0) {
                currentTimeStamp = getNextMillis();
            }
        } else {
            // 不是同一个毫秒内 初始化sequence
            sequence = 0;
        }

        // 维护当前时间戳
        lastTimeStamp = currentTimeStamp;

        return ((currentTimeStamp - fixedTimeStamp) << timeStampShift)
                | (computerRoomId << computerRoomIdShift)
                | (machineId << machineIdShift)
                | sequence;
    }

    private static long getNextMillis() {
        long currentTimeStamp = getCurrentTime();
        // 如果还是同一个毫秒 则继续等待
        while(currentTimeStamp <= lastTimeStamp) {
            currentTimeStamp = getCurrentTime();
        }

        return currentTimeStamp;
    }

}
