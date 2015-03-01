package be.doeraene.spickling

package object playjson {
  implicit val builder: PlayJsonPBuilder.type = PlayJsonPBuilder
  implicit val reader: PlayJsonPReader.type = PlayJsonPReader
}
