package eu.albertomorales.ontology.model

import eu.albertomorales.ontology.model.dto._
import eu.albertomorales.ontology.model.taxonomy.FamilyTree

trait GameInput {

  def tree: FamilyTree
  def questions: List[Question]
  def queries: List[Query]

}
