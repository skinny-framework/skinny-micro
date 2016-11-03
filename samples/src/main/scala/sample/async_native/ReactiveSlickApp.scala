// package sample.async_native
//
// import skinny.micro._
// import skinny.micro.contrib.jackson.JSONSupport
// import slick.dbio.Effect.{ Schema, Write }
// import slick.driver.H2Driver
//
// import scala.concurrent._
// import scala.concurrent.duration._
//
// import slick.driver.H2Driver.api._
//
// class ReactiveSlickApp extends TypedAsyncWebApp with JSONSupport {
//
//   lazy val db: H2Driver.backend.DatabaseDef = Database.forConfig("h2mem1")
//   lazy val suppliers: TableQuery[Suppliers] = TableQuery[Suppliers]
//   lazy val coffees: TableQuery[Coffees] = TableQuery[Coffees]
//
//   before() { implicit ctx =>
//     contentType = "application/json"
//   }
//   error {
//     case e =>
//       e.printStackTrace
//       throw e
//   }
//
//   get("/slick-demo/suppliers") { implicit ctx =>
//     db.run(suppliers.result).map { ss =>
//       Ok(toJSONString(ss))
//     }
//   }
//   get("/slick-demo/coffees") { implicit ctx =>
//     db.run(coffees.filter(_.price > 9.0).result).map { cs =>
//       Ok(toJSONString(cs))
//     }
//   }
//
//   val setupAction: DBIOAction[Unit, NoStream, Write with Schema] = DBIO.seq(
//     (suppliers.schema ++ coffees.schema).create,
//     suppliers += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
//     suppliers += (49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
//     suppliers += (150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966"),
//     coffees ++= Seq(
//       ("Colombian", 101, 7.99, 0, 0),
//       ("French_Roast", 49, 8.99, 0, 0),
//       ("Espresso", 150, 9.99, 0, 0),
//       ("Colombian_Decaf", 101, 8.99, 0, 0),
//       ("French_Roast_Decaf", 49, 9.99, 0, 0)
//     )
//   )
//   val dbFuture: Future[Unit] = db.run(setupAction)
//   Await.result(dbFuture, 3.seconds)
//
// }
