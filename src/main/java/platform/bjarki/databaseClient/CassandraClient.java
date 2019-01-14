package platform.bjarki.databaseClient;


import com.datastax.driver.core.Session;
import platform.bjarki.beans.PathConstants;
import platform.bjarki.databaseClient.repository.DeviceRepository;
import platform.bjarki.databaseClient.repository.KeyspaceRepository;
import org.jboss.logging.Logger;


public class CassandraClient {


    private static final Logger logger = Logger.getLogger(CassandraClient.class);
    public static final String keyspace = "data";
    public static final CassandraConnector connector = new CassandraConnector();
//    Address of the server
//    private static final String address = PathConstants.LOCAL_SERVER_IP;
    private static final String address = PathConstants.DOCKER_LOCAL_NETWORK;


    public CassandraClient() {

        //Connect to cassandra using the default IP address and port 9042
        connector.connector(address, 9042);
        logger.info("Cassandra client initilized");
        Session session = connector.getSession();


        //Create a new keyspace
        KeyspaceRepository keyspaceRepository = new KeyspaceRepository(session);
        keyspaceRepository.dropkeyspace(keyspace);
        keyspaceRepository.createKeyspace(keyspace, "SimpleStrategy", 1);
        keyspaceRepository.useKeyspace(keyspace);

        //Create the eyetracker Table if does not exist
        DeviceRepository eyetrackerRepository = new DeviceRepository(session);
//        ShimmerRepository shimmerRepository = new ShimmerRepository(session);
//        CassandraRepository cassandraRepository = new CassandraRepository(session);
        //TODO: take out droptable when devolopement is finished
//        eyetrackerRepository.dropTable();
//        shimmerRepository.dropTable();



//        deviceRepository.createTable();
//        shimmerRepository.createTable();



    }




    public void CassandraClose(){
        connector.close();
    }
}