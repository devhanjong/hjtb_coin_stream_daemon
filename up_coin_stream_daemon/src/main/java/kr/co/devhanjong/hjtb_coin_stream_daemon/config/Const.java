package kr.co.devhanjong.hjtb_coin_stream_daemon.config;

public class Const {

    /*
        vasp
     */
    public static final String BINANCE = "BINANCE";
    public static final String HUOBI = "HUOBI";
    public static final String PHEMEX = "PHEMEX";


    /*
        path
     */
    public static final String PATH_DEPTH = "depth";
    public static final String PATH_HEALTH = "health";

    /*
        kafka topic
     */
    public static final String TOPIC_STREAM_HOGA = "json.stream.hoga";
    public static final String TOPIC_STREAM_DAEMON_START = "json.stream.start";
    public static final String TOPIC_STREAM_DAEMON_STOP = "json.stream.stop";
    public static final String TOPIC_STREAM_DAEMON_RESTART = "json.stream.restart";
    public static final String TOPIC_PARSE_DAEMON_START = "json.parse.start";
    public static final String TOPIC_PARSE_DAEMON_STOP = "json.parse.start";
    public static final String TOPIC_PREPROCESSOR_DAEMON_START = "json.preprocessor.start";
    public static final String TOPIC_PREPROCESSOR_DAEMON_STOP = "json.preprocessor.stop";
}
