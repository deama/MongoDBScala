import org.mongodb.scala._
import org.mongodb.scala.bson.BsonString
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import org.mongodb.scala.model.Updates._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success} // Is required

object Main extends App
{
  val mongoClient: MongoClient = MongoClient("mongodb://localhost")
  val database: MongoDatabase = mongoClient.getDatabase("mydb")

  val collection: MongoCollection[Document] = database.getCollection("test");

  var input = true
  while(input)
  {
    Thread.sleep(800)
    userInput()
  }







  def userInput() : Unit =
  {
    val result:String = scala.io.StdIn.readLine("\n" + "1. insertDocument\n" + "2. deleteDocument\n" +
      "3. viewDocuments\n" + "4. updateDocument\n" + "5. exit\n" + "which function: ")

    result.toInt match
    {
      case 1 => insertDocument( scala.io.StdIn.readLine("Enter ID for creation: ").toInt )
      case 2 => deleteDocumentId( scala.io.StdIn.readLine("Enter ID for deletion: ").toInt )
      case 3 => viewDocuments()
      case 4 =>
      {
        val id:String = scala.io.StdIn.readLine("Enter ID for update: ")
        val name:String = scala.io.StdIn.readLine("Enter new name: ")
        updateDocument(id.toInt, name)
      }

      case 5 => input = false
    }
  }

  def viewDocuments() : Unit =
  {
    collection.find().subscribe(new Observer[Document]
    {
      override def onNext(result: Document): Unit = println( result.toJson() )
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = None
    })
  }

  def insertDocument( id:Int ) : Unit =
  {
    val doc: Document = Document( "_id" -> id, "name2" -> "MongoDB", "type" -> "database",
      "count" -> 1, "info" -> Document("x" -> 203, "y" -> 102) )

    collection.insertOne(doc).subscribe(new Observer[Completed]
    {
      override def onNext(result: Completed): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = None
    })
  }

  def deleteDocumentId( id:Int ) : Unit =
  {
    collection.deleteOne(equal("_id", id)).subscribe(new Observer[DeleteResult]
    {
      override def onNext(result: DeleteResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = None
    })
  }

  def updateDocument( id:Int, name:String ) : Unit =
  {
    collection.updateOne( equal("_id", id), set("name2", name) ).subscribe(new Observer[UpdateResult]
    {
      override def onNext(result: UpdateResult): Unit = println(result)
      override def onError(e: Throwable): Unit = println("Failed")
      override def onComplete(): Unit = None
    })
  }
}


