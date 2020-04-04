package browser.green.org.bona;

/**
 * @author jsbintask@gmail.com
 * @date 2018/4/25 15:35
 * 常量类
 */
public class Constants {
    public static final String EMPTY = "";
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_CHINESE_TIME_FORMAT = "yyyy年MM月dd日 HH:mm";
    public static final String CLOCK_RECEIVER_ACTION = "cn.jsbintask.memo.action.CLOCK_RECEIVER";
    public static String [] voiceType={"绵雨","蝉鸣知夏","湖水"};
    public static String [] state={"false","false","false"};
    public static String [] url={
            "http://m7.music.126.net/20200404005804/6523f7e9b89b0afd98319b37373d3426/ymusic/530f/0f5f/0053/defd83d1d08546480609798cb70fbdf1.mp3",
            "http://m8.music.126.net/20200404005750/39906b819d8ed99d74e93b7a7f6d2106/ymusic/ccb7/814d/3ac7/baa322bfbc4c52aa7a18f03d7145677c.mp3",
            "http://m7.music.126.net/20200403235726/1ed072bb0ddba5d7bec3a9127365702d/ymusic/3544/a29e/5ca7/0941b504ab87c0a91665f2f8709b44ba.mp3"
    };
    public interface EventFlag {
        int IMPORTANT = 1;
        int NORMAL = 0;
    }

    public interface MemoIconTag {
        int FIRST = 1;
        int OTHER = 2;
    }

    public interface EventClockFlag {
        int NONE = 0;
        int CLOCKED = 10;
    }

    public static final int HANDLER_SUCCESS = 0x0001;
    public static final int HANDLER_FAILED = 0x0000;
}
