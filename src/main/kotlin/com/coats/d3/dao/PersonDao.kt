package com.coats.d3.dao


import com.coats.d3.Country
import com.coats.d3.Person

class PersonDao {


 // companion object {
  // val mock_person by lazy {
   var mock_person= mutableListOf(Person(7,"Satheesh", 28 , Country("Russia", "RU")),
   Person(6,"Stewart", 45, Country("New Zealand", "NZ")),
   Person(5,"Sasi",30, Country("Australia", "AU")),
   Person(4,"Tamana",23 ,Country("Australia", "AU"))
    )
  //}
//}

  /**
   * Fetches all persons.
   */
  fun fetchPersons() = mock_person

  /**
   * Fetches person by id.
   *
   * @param id Optional code to filter person by.
   */
  fun fetchPerson(pid: Int? = null) =
    mock_person.filter { pid == null || it.pid == pid }

  fun addPerson(person:Person): Person {
    println("Hello, Im In addPerson")

    var  pidIs:Boolean = false
    if (person != null) {
      if(person.pid==0){
        val pid = (0..1000).random()
        person.pid=pid
        mock_person.add(person)
        pidIs=true
      }
      else {
        mock_person.add(person)
      }
    }
    if(pidIs) {
      return person
    }
   return person
  }
}
