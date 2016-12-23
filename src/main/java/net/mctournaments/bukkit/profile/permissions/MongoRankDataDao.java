package net.mctournaments.bukkit.profile.permissions;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.mctournaments.bukkit.profile.Rank;
import org.bson.Document;

import java.util.List;

public class MongoRankDataDao implements RankDataDao {

    private final String ip;
    private final int port;
    private final String dbName;

    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> coll;

    public MongoRankDataDao(String ip, int port, String dbName) {
        this.ip = ip;
        this.port = port;
        this.dbName = dbName;
    }

    @Override
    public void initialize() {
        this.client = new MongoClient(this.ip, this.port);
        this.db = this.client.getDatabase(this.dbName);
        this.coll = this.db.getCollection("ranks");
    }

    @Override
    public RankData getRankData(Rank rank) {
        Document result = this.coll.find(Filters.eq("_id", rank.name())).first();

        checkNotNull(result, "Failed to get permissions for %s, is there a document in the %s collection for it?",
                rank.name(), this.coll.getNamespace().getCollectionName());

        checkState(result.containsKey("perms") && result.containsKey("negatedPerms"),
                rank.name() + " does not have both perms and negated perms data.");

        return new RankData((List<String>) result.get("perms"), (List<String>) result.get("negatedPerms"));
    }

    @Override
    public void close() {
        this.client.close();
    }
}
