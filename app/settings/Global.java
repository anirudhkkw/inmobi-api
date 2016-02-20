package settings;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import play.Application;
import play.Configuration;
import play.GlobalSettings;
import play.Play;

import java.util.ArrayList;
import java.util.List;

public class Global extends GlobalSettings {
    private static MorphiaObject morphiaObject;
    private static MongoClient mongoClient;
    private static AmazonS3 s3Client;
    private static String amazonBucket;

    @Override
    public void onStart(Application configuration){
        Morphia morphia = new Morphia();
        MongoClient mongo = this.ensureMongoCLient();
        Datastore ds = morphia.createDatastore(mongo, configuration.configuration().getString("mongo.db"));
        ds.ensureIndexes();
        morphiaObject = new MorphiaObject(mongo, morphia, ds);
        doAmazons3Connection(configuration.configuration().getString("aws.secretKey"),configuration.configuration().getString("aws.accessKey"));
        amazonBucket = configuration.configuration().getString("aws.bucket");
    }
    protected MongoClient ensureMongoCLient() {
        if(mongoClient == null) {
            Configuration config = Play.application().configuration();
            String serversStr = config.getString("mongo.servers");
            String[] serversArr = serversStr.split(",");
            ArrayList serverObjs = new ArrayList();
            List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
            MongoCredential credentia = MongoCredential.createCredential(
                    "inmobi", "inmobi", "inmobi@123".toCharArray());
            credentialsList.add(credentia);

            for(int i = 0; i < serversArr.length; ++i) {
                serverObjs.add(new ServerAddress(config.getString("mongo." + serversArr[i] + ".host"), config.getInt("mongo." + serversArr[i] + ".port").intValue()));
            }

            mongoClient = new MongoClient(serverObjs, credentialsList);
        }

        return mongoClient;
    }

    public static MorphiaObject getMorphiaObject() {
        if(morphiaObject == null) {
            throw new IllegalStateException("Morphia not initialized.Call initMongo()");
        } else {
            return morphiaObject;
        }
    }

    public static AmazonS3 getS3Client() {
        return s3Client;
    }

    public static String getAmazonBucket() {
        return amazonBucket;
    }

    public static void doAmazons3Connection(String awsSecretKey, String awsAccessKey) {
        //System.out.println(" secret key " + awsSecretKey + " and access key is " + awsAccessKey);
        s3Client = new AmazonS3Client(new AWSCredentials() {
            @Override
            public String getAWSSecretKey() {
                //System.out.println("3Zdf9eOtm6VwdFWudYOokHtW9I2HMXpP8o4NeRlD".equals(awsSecretKey));
                return awsSecretKey ;//"3Zdf9eOtm6VwdFWudYOokHtW9I2HMXpP8o4NeRlD";
            }

            @Override
            public String getAWSAccessKeyId() {
                //System.out.println("AKIAINAE46OTMRQ7JU5Q".equals(awsAccessKey));
                return awsAccessKey;//"AKIAINAE46OTMRQ7JU5Q";
            }
        });
    }
}
