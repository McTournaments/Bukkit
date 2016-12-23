package net.mctournaments.bukkit.profile.storage;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.text;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.TextSearchOptions;
import net.mctournaments.bukkit.profile.NavigableRankSet;
import net.mctournaments.bukkit.profile.Profile;
import net.mctournaments.bukkit.profile.Rank;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MongoProfileStorageDao implements ProfileStorageDao {

    private final String ip;
    private final int port;
    private final String dbName;

    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> coll;

    public MongoProfileStorageDao(String ip, int port, String dbName) {
        this.ip = ip;
        this.port = port;
        this.dbName = dbName;
    }

    @Override
    public void initialize() {
        this.client = new MongoClient(this.ip, this.port);
        this.db = this.client.getDatabase(this.dbName);
        this.coll = this.db.getCollection("profileData");
    }

    private void supplyDbField(Document document, String key, Consumer<Object> ifFound) {
        if (document.containsKey(key)) {
            Object val = document.get(key);
            if (val != null) {
                ifFound.accept(val);
            }
        }
    }

    @Override
    public Optional<Profile> getProfileData(UUID uuid) {
        Document result = this.coll.find(eq("_id", uuid.toString())).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(this.createProfile(result));
    }

    @Override
    public Optional<Profile> getProfileData(String username) {
        Document result = this.coll.find(eq("lastKnownUsername", username)).first();
        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(this.createProfile(result));
    }

    @Override
    public boolean addRank(UUID uuid, Rank rank) {
        Bson query = eq("_id", uuid.toString());

        Document doc = this.coll.find(query).projection(Projections.include("ranks")).first();
        if (doc == null) {
            return false;
        }

        NavigableRankSet ranks = new NavigableRankSet(((List<String>) doc.get("ranks"))
                .stream().map(Rank::valueOf).collect(Collectors.toList()));

        ranks.add(rank);

        return this.coll.updateOne(query, new Document("$set", new Document("ranks", ranks.asList())))
                .getModifiedCount() >= 1;
    }

    @Override
    public boolean removeRank(UUID uuid, Rank rank) {
        Bson query = eq("_id", uuid.toString());

        Document doc = this.coll.find(query).projection(Projections.include("ranks")).first();
        if (doc == null) {
            return false;
        }

        NavigableRankSet ranks = new NavigableRankSet(((List<String>) doc.get("ranks"))
                .stream().map(Rank::valueOf).collect(Collectors.toList()));

        ranks.remove(rank);

        return this.coll.updateOne(query, new Document("$set", new Document("ranks", ranks.asList())))
                .getModifiedCount() >= 1;
    }

    @Override
    public boolean setRank(UUID uuid, Rank rank) {
        return this.coll.updateOne(eq("_id", uuid.toString()), new Document("$set", new Document("ranks", Lists.newArrayList(rank.name()))))
                .getModifiedCount() >= 1;
    }

    @Override
    public boolean addRank(String username, Rank rank) {
        Bson query = new Document("lastKnownUsername", text(username, new TextSearchOptions().caseSensitive(false)));

        Document doc = this.coll.find(query).projection(Projections.include("ranks")).first();
        if (doc == null) {
            return false;
        }

        NavigableRankSet ranks = new NavigableRankSet(((List<String>) doc.get("ranks"))
                .stream().map(Rank::valueOf).collect(Collectors.toList()));

        ranks.add(rank);

        return this.coll.updateOne(query, new Document("$set", new Document("ranks", ranks.asList())))
                .getModifiedCount() >= 1;
    }

    @Override
    public boolean removeRank(String username, Rank rank) {
        Bson query = new Document("lastKnownUsername", text(username, new TextSearchOptions().caseSensitive(false)));

        Document doc = this.coll.find(query).projection(Projections.include("ranks")).first();
        if (doc == null) {
            return false;
        }

        NavigableRankSet ranks = new NavigableRankSet(((List<String>) doc.get("ranks"))
                .stream().map(Rank::valueOf).collect(Collectors.toList()));

        ranks.remove(rank);

        return this.coll.updateOne(query, new Document("$set", new Document("ranks", ranks.asList())))
                .getModifiedCount() >= 1;
    }

    @Override
    public boolean setRank(String username, Rank rank) {
        Bson query = new Document("lastKnownUsername", Pattern.compile(".*" + username + ".*", Pattern.CASE_INSENSITIVE));

        return this.coll.updateOne(query, new Document("$set", new Document("ranks", Lists.newArrayList(rank.name())))).getModifiedCount() >= 1;
    }

    @Override
    public void updateUsername(UUID uuid, String username) {
        Bson query = eq("_id", uuid.toString());

        this.coll.updateOne(query, new Document("$set", new Document("lastKnownUsername", username)));
        this.coll.updateOne(query, new Document("$addToSet", new Document("knownUsernames", username)));
    }

    @Override
    public void updateIp(UUID uuid, String ip) {
        Bson query = eq("_id", uuid.toString());

        this.coll.updateOne(query, new Document("$set", new Document("lastKnownIp", ip)));
        this.coll.updateOne(query, new Document("$addToSet", new Document("knownIps", ip)));
    }

    @Override
    public void setSetting(UUID uuid, String setting, boolean value) {
        this.coll.updateOne(eq("_id", uuid.toString()), new Document("$set", new Document("settings." + setting, value)));
    }

    @Override
    public void setLastJoin(UUID uuid, long lastJoin) {
        this.coll.updateOne(eq("_id", uuid.toString()), new Document("$set", new Document("lastJoin", lastJoin)));
    }

    private Profile createProfile(Document document) {
        Profile profile = new Profile(UUID.fromString((String) document.get("_id")));

        supplyDbField(document, "ranks", ranks -> ((List<String>) ranks).forEach(rank -> profile.getRanks().add(Rank.valueOf(rank))));

        supplyDbField(document, "lastKnownUsername", lastKnownUsername -> profile.setLastKnownUsername((String) lastKnownUsername));
        supplyDbField(document, "lastKnownIp", lastKnownIp -> profile.setLastKnownIp((String) lastKnownIp));

        supplyDbField(document, "knownUsernames", knownUsernames -> profile.setKnownUsernames((List<String>) knownUsernames));
        supplyDbField(document, "knownIps", knownIps -> profile.setKnownIps((List<String>) knownIps));

        supplyDbField(document, "settings", settings -> profile.setSettings((Map<String, Boolean>) settings));

        supplyDbField(document, "firstJoin", firstJoin -> profile.setFirstJoin((Long) firstJoin));
        supplyDbField(document, "lastJoin", lastJoin -> profile.setLastJoin((Long) lastJoin));

        return profile;
    }

    @Override public void insert(Profile profile) {
        this.coll.insertOne(new Document("_id", profile.getUniqueId().toString())
                .append("ranks", profile.getRanks().asNavigatableSet().stream().map(Rank::name).collect(Collectors.toList()))
                .append("lastKnownUsername", profile.getLastKnownUsername())
                .append("lastKnownIp", profile.getLastKnownIp())
                .append("knownUsernames", profile.getKnownUsernames())
                .append("knownIps", profile.getKnownIps())
                .append("settings", profile.getSettings())
                .append("firstJoin", profile.getFirstJoin())
                .append("lastJoin", profile.getLastJoin()));
    }

    @Override
    public void close() {
        this.client.close();
    }

}
