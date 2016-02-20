package settings;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class MorphiaObject {
    private MongoClient mongo;
    private Morphia morphia;
    private Datastore datastore;

    MorphiaObject(MongoClient mongo, Morphia morphia, Datastore datastore) {
        this.mongo = mongo;
        this.morphia = morphia;
        this.datastore = datastore;
    }

    public MongoClient getMongo() {
        return this.mongo;
    }

    public Morphia getMorphia() {
        return this.morphia;
    }

    public Datastore getDatastore() {
        return this.datastore;
    }
}
