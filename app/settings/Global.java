package settings;

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

    @Override
    public void onStart(Application configuration){
        Morphia morphia = new Morphia();
        MongoClient mongo = this.ensureMongoCLient();
        Datastore ds = morphia.createDatastore(mongo, configuration.configuration().getString("mongo.db"));
        ds.ensureIndexes();
        morphiaObject = new MorphiaObject(mongo, morphia, ds);
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
}
