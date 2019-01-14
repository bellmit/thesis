package platform.bjarki.beans;


public final class PathConstants {

//    JNDIpaths
    public static final String EYETRACKER_QUEUE = "java:jboss/exported/jms/queue/eyetracker";
    public static final String EYETRACKER_RAW_QUEUE = "java:jboss/exported/jms/queue/eyetrackerRaw";


    public static final String SHIMMER_RAW_QUEUE = "java:jboss/exported/jms/queue/shimmerRaw";
    public static final String SHIMMER_QUEUE = "java:jboss/exported/jms/queue/shimmer";
//    public static final String REPLY_QUEUE = "java:jboss/exported/jms/queue/reply";


    public static final String DATABASE_CONNECTION_FACTORY = "java:/DatabaseConnectionFactory";
    public static final String CON = "java:/ConnectionFactory";
    public static final String INCOMING_DATABASE = "java:/IncomingDataConnectionFactory";
    public static final String POOLED_CONNECTION_FACTORY = "jms/remoteCF";


//
    public static final String DATABASE_QUEUE = "java:jboss/exported/jms/queue/database";

    public static final String REPLY_QUEUE = "java:jboss/exported/jms/queue/reply";
    public static final String REST_REPLY_QUEUE = "java:jboss/exported/jms/queue/restreply";

    public static final String PROCESSING_SERVER_CONNECTION_FACTORY = "java:/ProcessingConnectionFactory";
    public static final String DATABASE_SERVER_CONNECTION_FACTORY = "java:/DatabaseConnectionFactory";




    //    IPs and ports
    public static final String REMOTE_SERVER_IP ="157.230.18.17";
    public static final String REMOTE_SERVER_JMS_PORT ="5445";

    public static final String LOCAL_SERVER_IP ="104.248.27.213";
    public static final String DOCKER_LOCAL_NETWORK ="172.17.0.1";


}
