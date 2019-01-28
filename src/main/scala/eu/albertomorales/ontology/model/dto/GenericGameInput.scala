package eu.albertomorales.ontology.model.dto

import eu.albertomorales.ontology.model.taxonomy.FamilyTree
import eu.albertomorales.ontology.model.GameInput

class GenericGameInput(private val _tree: FamilyTree, private val _questions: List[Question], private val _queries: List[Query]) extends GameInput {

  override def tree: FamilyTree = {
    _tree
  }

  override def questions: List[Question] = {
    _questions
  }

  override def queries: List[Query] = {
    _queries
  }

}