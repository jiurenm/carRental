package com.edu.car.uid;

import lombok.extern.slf4j.Slf4j;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.Clock;
import java.util.Enumeration;
import java.util.Objects;

/**
 * id生成器
 *
 * @author Administrator
 * @date 2019/1/7 13:15
 */
@Slf4j
public class IdWorker {
    private long workerId;
    private long dataCenterId;
    private long sequence = 0L;

    private static long workerIdBits = 5L;
    private static long dataCenterIdBits = 5L;
    private static long maxWorkerId = ~(-1L << (int) dataCenterIdBits);
    private static long maxDataCenterId = ~(-1L << (int) dataCenterIdBits);
    private static long sequenceBits = 12L;

    private static long workerIdShift = sequenceBits;
    private static long dataCenterIdShift = sequenceBits + workerIdBits;
    private static long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;
    private static long sequenceMask = ~(-1L << (int) sequenceBits);

    private long lastTimestamp = -1L;

    private static IdWorker idWorker = null;
    private static final Object LOCK = new Object();

    public static long getId() {
        if (idWorker == null) {
            synchronized (LOCK) {
                long dataCenterId = IdWorker.getDataCenterId();
                long workId = IdWorker.getWorkerId();
                idWorker = new IdWorker(workId,dataCenterId);
            }
        }
        return idWorker.nextId();
    }

    private IdWorker(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            log.error("worker Id can't be greater than %d or less than 0", maxWorkerId);
        }
        if (dataCenterId >maxDataCenterId || dataCenterId < 0) {
            log.error("dataCenter Id can't be greater than %d or less than 0", maxDataCenterId);
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    private synchronized long nextId() {
        long timestamp = timeGen( );
        if (timestamp < lastTimestamp) {
            log.error("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp);
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        long twepoch = 1288834974657L;
        return ((timestamp - twepoch) << (int) timestampLeftShift) |
                (dataCenterId << (int) dataCenterIdShift) |
                (workerId << (int) workerIdShift) |
                sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private static long timeGen() {
        return Clock.systemDefaultZone().millis();
    }

    private static long getWorkerId() {
        long machinePiece;
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<NetworkInterface> e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace( );
        }
        while (Objects.requireNonNull(e).hasMoreElements()) {
            NetworkInterface networkInterface = e.nextElement();
            stringBuilder.append(networkInterface.toString());
        }
        machinePiece = stringBuilder.toString().hashCode();
        return machinePiece & maxWorkerId;
    }

    private static long getDataCenterId() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Long.valueOf(runtimeMXBean.getName().split("@")[0]) & maxDataCenterId;
    }
}