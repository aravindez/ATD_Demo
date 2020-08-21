//DEMO

db.getCollection("customers").insert( { firstName: "John", lastName: "Smith"} );
db.getCollection("customers").insert( { firstName: "John", lastName: "Ruisi"} );
db.getCollection("customers").insert( { firstName: "Alice", lastName: "Smith"} );


db.getCollection("customers").find({})



//db.getCollection("customers").remove({})