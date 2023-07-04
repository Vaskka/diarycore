package com.vaskka.diary.core.utils.uid;


/**
 * 扩展的雪花算法
 * 设计思路：<a href="https://www.yuque.com/dpitech/dy2rme/pvhyo5">语雀：全局id生成思路</a>
 * github：<a href="https://github.com/IntelliMarket3/global-id-generator">github</a>
 * 第一位为固定位1，指定id为非负数。
 * 第2位 ～第42位， 一共41位长度。为当前时间戳。
 * 第43位～第45位，一共3位长度。表示不同的业务领域。
 * 第46位～第52位，一共7位长度。表示当前机器id
 * 第53位～第64位，一共12位长度。表示同一时间戳下的递增id。
 */
public class GlobalIdCore {

    /**
     * union id 的二进制长度: 64
     */
    public static final int UNION_ID_LENGTH_BINARY = Long.toBinaryString(Long.MAX_VALUE).length() + 1;

    /**
     * union id 的十进制长度：19
     */
    public static final int UNION_ID_LENGTH_DECIMAL = String.valueOf(Long.MAX_VALUE).length();

    /**
     * 时间戳部分41位长度
     */
    private static final long TIMESTAMP_LENGTH = 41L;

    /**
     * 时间戳部分开始的位置
     */
    private static final long TIMESTAMP_POSITION = 2L;

    /**
     * 业务领域id最大
     */
    private static final long BIZ_MAX = 0x7L;

    /**
     * 业务领域部分3位长度
     */
    private static final long BIZ_LENGTH = 3L;

    /**
     * 业务领域部分开始的位置
     */
    private static final long BIZ_POSITION = 43L;

    /**
     * 机器id最大
     */
    private static final long MACHINE_MAX = 0x7FL;

    /**
     * 机器id部分7位长度
     */
    private static final long MACHINE_LENGTH = 7L;

    /**
     * 机器id部分开始的位置
     */
    private static final long MACHINE_POSITION = 46L;

    /**
     * 当前时间戳下序列号最大
     */
    private static final long SEQUENCE_MAX = 0xFFFL;

    /**
     * 上一时间戳
     */
    private long preTimestamp = -1L;

    /**
     * 机器id
     */
    private final long machineId;

    /**
     * 当前递增序列
     */
    private long currSequence = 0L;

    /**
     * constructor
     * @param machineId 机器id
     */
    public GlobalIdCore(long machineId) {

        if (machineId < 0) {
            throw new RuntimeException("machine id can not be negative.");
        }

        if (machineId > MACHINE_MAX) {
            throw new RuntimeException(String.format("machine id can not bigger than %d", MACHINE_MAX));
        }

        this.machineId = machineId;
    }

    /**
     * 获取全局id
     * @param biz 业务区分位
     * @return long
     */
    public synchronized long getUnionId(long biz) {

        if (biz < 0 || biz > BIZ_MAX) {
            throw new RuntimeException(String.format("biz is out of range 0~%d, current is: %d", BIZ_MAX, biz));
        }

        long currTimestamp = System.currentTimeMillis();
        if (currTimestamp < preTimestamp) {
            throw new RuntimeException("clock error.");
        }

        if (currTimestamp == preTimestamp) {
            currSequence = (currSequence + 1) % (SEQUENCE_MAX + 1);
            if (currSequence == 0) {
                // wait for next millis
                currTimestamp = getNextMillis();
            }
        }
        else {
            // next timestamp
            currSequence = 0;
        }

        preTimestamp = currTimestamp;

        return (currTimestamp << (UNION_ID_LENGTH_BINARY - TIMESTAMP_POSITION - TIMESTAMP_LENGTH + 1)) |
                (biz           << (UNION_ID_LENGTH_BINARY - BIZ_POSITION       - BIZ_LENGTH       + 1)) |
                (machineId     << (UNION_ID_LENGTH_BINARY - MACHINE_POSITION   - MACHINE_LENGTH   + 1)) |
                currSequence;
    }

    /**
     * cpu等待下一时刻
     * @return long
     */
    private long getNextMillis() {
        long next = System.currentTimeMillis();
        while (next <= preTimestamp) {
            next = System.currentTimeMillis();
        }

        return next;
    }

}