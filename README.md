# VismaTask

Java Rest API on Spring

Route  | Info
------------- | -------------
/  | GET -> Returns all books
/search/{guid}  | GET -> Return requested book
/filter/{filter}:{parameter} | GET -> Return list of books sorted by the filter and parameter <br> Filter sets by which value list will be sorted, parameter sets the list to ascending or descending<br> Filter can be "name", "author", "category", "language", "isbn" or "books_are" <br> In all cases except "books_are" the parameter can only be "asc" or "desc" <br> With "books_are" the parameter is either "available" or "taken"   
/add | POST -> Saves the book <br> Requires "guid", "name", "category", "language", "publication_date", "isbn"
/take | POST -> Registers book taker, if the requirements are met. <br> Requires "guid", "taken_by", "rent_period"
/remove | DELETE -> Deletes the book <br> Requires "guid"
