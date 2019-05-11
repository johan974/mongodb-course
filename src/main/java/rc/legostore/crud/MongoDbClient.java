package rc.legostore.crud;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Updates.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class MongoDbClient {
    public static void main(String[] args) {
        // MongoClientURI connectionString = new MongoClientURI("mongodb://hostOne:27017,hostTwo:27017");
        // MongoClient mongoClient = new MongoClient(connectionString);

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("legostore");
        MongoCollection<Document> collection = database.getCollection("legoSet");

        MongoDbClient dbClient = new MongoDbClient();

        dbClient.testCursor( collection);

        dbClient.testFindName( collection, "Millennium Falcon");

        dbClient.insertDocumentByJson( collection);

        // CRUD operations
        collection = database.getCollection("cruditems");
        // dbClient.crudInsertOne( collection);
        // dbClient.crudInsertMany(  collection);
        //dbClient.insertManyFromJson(  collection);
        dbClient.readSelect(  collection);

        dbClient.crudUpdate(  collection);



    }

    private void testCursor( MongoCollection<Document> collection) {
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
    }

    private void testFindName(MongoCollection<Document> collection, String name) {
        Document myDoc = collection.find(  eq( "name", "Millennium Falcon")).first();
        System.out.println( "Find document: " + myDoc.toJson());
    }

    private void insertDocumentByAppending() {}

    private void insertDocumentByJson( MongoCollection<Document> collection) {
        String json = "{\n" +
                "    \"name\" : \"Johan Game\",\n" +
                "    \"difficulty\" : \"HARD\",\n" +
                "    \"theme\" : \"Johans live\",\n" +
                "    \"reviews\" : [ \n" +
                "        {\n" +
                "            \"userName\" : \"Trudie\",\n" +
                "            \"rating\" : 10\n" +
                "        }, \n" +
                "        {\n" +
                "            \"userName\" : \"Matthijs\",\n" +
                "            \"rating\" : 9\n" +
                "        }, \n" +
                "        {\n" +
                "            \"userName\" : \"Dick\",\n" +
                "            \"rating\" : 9\n" +
                "        }\n" +
                "    ],\n" +
                "    \"delivery\" : {\n" +
                "        \"deliveryDate\" : ISODate(\"1963-18-12T22:00:00.000Z\"),\n" +
                "        \"deliveryFee\" : 10,\n" +
                "        \"inStock\" : true\n" +
                "    }\n" +
                "}";
        Document dbObject = Document.parse(json);
        collection.insertOne(dbObject);
        Document myDoc = collection.find(  eq( "name", "Johan Game")).first();
        System.out.println( "Find document: " + myDoc.toJson());
    }

    private void crudInsertOne(  MongoCollection<Document> collection) {
        Document canvas = new Document("item", "canvas")
                .append("qty", 100)
                .append("tags", singletonList("cotton"));
        Document size = new Document("h", 28)
                .append("w", 35.5)
                .append("uom", "cm");
        canvas.put("size", size);
        collection.insertOne(canvas);

        Document doc = collection.find(eq("item", "canvas")).first();
        System.out.println( "Crud/insertOne: " + doc.toJson());
    }

    private void crudInsertMany( MongoCollection<Document> collection) {
        Document journal = new Document("item", "journal")
                .append("qty", 25)
                .append("tags", asList("blank", "red"));
        Document journalSize = new Document("h", 14)
                .append("w", 21)
                .append("uom", "cm");
        journal.put("size", journalSize);

        Document mat = new Document("item", "mat")
                .append("qty", 85)
                .append("tags", singletonList("gray"));

        Document matSize = new Document("h", 27.9)
                .append("w", 35.5)
                .append("uom", "cm");
        mat.put("size", matSize);

        Document mousePad = new Document("item", "mousePad")
                .append("qty", 25)
                .append("tags", asList("gel", "blue"));

        Document mousePadSize = new Document("h", 19)
                .append("w", 22.85)
                .append("uom", "cm");
        mousePad.put("size", mousePadSize);

        collection.insertMany(asList(journal, mat, mousePad));

        FindIterable<Document> findIterable = collection.find(new Document());
        for( Document doc : findIterable) {
            System.out.println( "CrudInsertMany: doc = " + doc.toJson());
        }
    }

    private void insertManyFromJson( MongoCollection collection) {
        collection.insertMany(asList(
                Document.parse("{ item: 'journal2', qty: 25, size: { h: 14, w: 21, uom: 'cm' }, status: 'A' }"),
                Document.parse("{ item: 'notebook2', qty: 50, size: { h: 8.5, w: 11, uom: 'in' }, status: 'A' }"),
                Document.parse("{ item: 'paper2', qty: 100, size: { h: 8.5, w: 11, uom: 'in' }, status: 'D' }"),
                Document.parse("{ item: 'planner2', qty: 75, size: { h: 22.85, w: 30, uom: 'cm' }, status: 'D' }"),
                Document.parse("{ item: 'postcard2', qty: 45, size: { h: 10, w: 15.25, uom: 'cm' }, status: 'A' }")
        ));
        FindIterable<Document> findIterable = collection.find(new Document());
        for( Document doc : findIterable) {
            System.out.println( "CrudInsertMany: doc = " + doc.toJson());
        }
    }

    private void readSelect( MongoCollection collection) {
        FindIterable<Document> docs = collection.find( eq("status", "D"));
        for( Document doc : docs) {
            System.out.println( "CrudSelect/: doc = " + doc.toJson());
        }

//        docs = collection.find(in("status", "A", "D"));
////
////        docs = collection.find(and(eq("status", "A"), lt("qty", 30)));
////
////        docs = collection.find(
////                and(eq("status", "A"),
////                        or(lt("qty", 30), regex("item", "^p")))
////        );
    }

    private void crudUpdate( MongoCollection<Document> collection) {
        FindIterable<Document> docs = collection.find( eq("item", "paper2"));
        for( Document doc : docs) {
            System.out.println( "CrudUpdate1/before: doc = " + doc.toJson());
        }
        collection.updateOne(eq("item", "paper2"),
                combine(set("size.uom", "cm"), set("status", "P"), currentDate("lastModified")));
        docs = collection.find( eq("item", "paper2"));
        for( Document doc : docs) {
            System.out.println( "CrudUpdate1/after: doc = " + doc.toJson());
        }

        collection.updateMany(lt("qty", 50),
                combine(set("size.uom", "in"), set("status", "P"), currentDate("lastModified")));
    }

    private void replaceOne( MongoCollection<Document> collection) {
        collection.replaceOne(eq("item", "paper"),
                Document.parse("{ item: 'paper', instock: [ { warehouse: 'A', qty: 60 }, { warehouse: 'B', qty: 40 } ] }"));
    }

}
