package utils;

/**
 * 雪花算法 工具类
 */
public class SnowFlake {

    // 起始的时间戳
    private static final long START_STAMP = 1480166465631L;
    // 序列号占用的位数
    private static final long SEQUENCE_BIT = 12;
    // 机器标识占用的位数
    private static final long MACHINE_BIT = 5;
    // 数据中心占用的位数
    private static final long DATA_CENTER_BIT = 5;
    // 每一部分最大值
    private static final long MAX_DATA_CENTER_NUM = ~(-1L << DATA_CENTER_BIT);
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    // 每一部分向左的位移
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private static final long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;
    // 数据中心
    private long dataCenterId;
    // 机器标识
    private long machineId;
    // 序列号
    private long sequence = 0L;
    // 上一次时间戳
    private long lastStamp = -1L;

    public SnowFlake(long dataCenterId, long machineId) {

        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }


    /**
     * @return 产生下一个ID
     * @throws IllegalStateException 时钟回拨 抛出该异常
     */
    public synchronized long nextId() {


        long currentStamp = System.currentTimeMillis();
        if (currentStamp < lastStamp) {
            throw new IllegalStateException("Clock moved backwards.  Refusing to generate id");
        }

        if (currentStamp == lastStamp) {
            //if条件里表示当前调用和上一次调用落在了相同毫秒内，只能通过第三部分，序列号自增来判断为唯一，所以+1.
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大，只能等待下一个毫秒
            if (sequence == 0L) {
                currentStamp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            //执行到这个分支的前提是currTimestamp > lastTimestamp，说明本次调用跟上次调用对比，已经不再同一个毫秒内了，这个时候序号可以重新回置0了。
            sequence = 0L;
        }

        lastStamp = currentStamp;
        //就是用相对毫秒数、机器ID和自增序号拼接
        return  //时间戳部分
                (currentStamp - START_STAMP) << TIMESTAMP_LEFT
                        //数据中心部分
                        | dataCenterId << DATA_CENTER_LEFT
                        //机器标识部分
                        | machineId << MACHINE_LEFT
                        //序列号部分
                        | sequence;
    }

    private long getNextMill() {
        long mill = System.currentTimeMillis();
        while (mill <= lastStamp) {
            mill = System.currentTimeMillis();
        }
        return mill;
    }


}
