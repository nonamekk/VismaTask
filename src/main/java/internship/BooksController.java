package internship;


import internship.Data;
import internship.DataGrab;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class BooksController {

    @GetMapping
    public String getBooks() {
        List<Data> list = DataGrab.getAll();
        if (list == null) {
            return null;
        }
        else return DataGrab.toJSONString(list);
    }
    @GetMapping("/search/{guid}")
    public String getBookBy (@PathVariable("guid") String guid) {
        return DataGrab.toJSONString(DataGrab.getBy(guid));
    }

    @GetMapping("/filter/{filter}:{parameter}")
    public ResponseEntity<String> filterBooksBy(@PathVariable("filter") String filter, @PathVariable("parameter") String parameter ) {
        List<Data> list = DataGrab.getSortedBy(filter, parameter);
        if (list == null) {
            return new ResponseEntity<>(
                    "Filter or parameter is wrong. Allowed filters (name, author, category, language, isbn, books_are). Allowed parameters (asc, desc), but with filter (books_are) they are (taken, available).",
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                DataGrab.toJSONString(list),
                HttpStatus.OK
        );
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody Data payload) {
        // check if data is there

        if (payload.getGuid() != null)
            if (payload.getName() != null)
                if (payload.getAuthor() != null)
                    if (payload.getCategory() != null)
                        if (payload.getLanguage() != null)
                            if (payload.getPublication_date() != null)
                                if (payload.getIsbn() != null)
                                {
                                    Data book = new Data(payload.getGuid(), payload.getName(), payload.getAuthor(), payload.getCategory(), payload.getLanguage(), payload.getPublication_date(), payload.getIsbn());
                                    if (DataGrab.saveNewBook(book) != null)
                                    return new ResponseEntity<>(
                                            "Book GUID: " + book.getGuid() + " is added successfully",
                                            HttpStatus.OK
                                        );
                                    else return new ResponseEntity<>(
                                            "Book GUID: "+ book.getGuid()+" already exists, can't add",
                                            HttpStatus.CONFLICT
                                    );
                                }
                                else return new ResponseEntity<>(
                                            "isbn is not set",
                                            HttpStatus.BAD_REQUEST
                                    );
                            else return new ResponseEntity<>(
                                "publication date is not set",
                                HttpStatus.BAD_REQUEST
                            );
                        else return new ResponseEntity<>(
                            "language is not set",
                            HttpStatus.BAD_REQUEST
                        );
                    else return new ResponseEntity<>(
                        "category is not set",
                        HttpStatus.BAD_REQUEST
                    );
                else return new ResponseEntity<>(
                    "author is not set",
                    HttpStatus.BAD_REQUEST
                );
            else return new ResponseEntity<>(
                "name is not set",
                HttpStatus.BAD_REQUEST
            );
        else return new ResponseEntity<>(
                "guid is not set",
                HttpStatus.BAD_REQUEST
        );



    }

    @PostMapping("/take")
    public ResponseEntity<String> takeBook (@RequestBody Data payload) {
        //return new ResponseEntity<>(payload, HttpStatus.OK);
        if (payload.getGuid() != null)
            if (payload.getTaken_by() != null)
                if (payload.getRent_period() != null)
                {
                    try {
                        int period = Integer.parseInt(payload.getRent_period());
                        if (period > 61) {
                            return new ResponseEntity<>(
                                    "Book can only be taken for the period no more than 2 months or 61 days",
                                    HttpStatus.UNPROCESSABLE_ENTITY
                            );
                        }
                        List<Data> list = DataGrab.getTakenBy(payload.getTaken_by());
                        if (list != null) {
                            if (list.size() > 2) {
                                return new ResponseEntity<>(
                                    "Can't add book to user. The specified user already has more than 2 books assigned as taken",
                                        HttpStatus.FORBIDDEN
                                );
                            }
                        }



                        Data savedBook = DataGrab.getBy(payload.getGuid());
                        if (savedBook.getTaken_by() != null) {
                            return new ResponseEntity<>(
                                "This book is already taken, can't assign to specified user",
                                HttpStatus.CONFLICT
                            );
                        }
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

                        LocalDateTime currentDT = LocalDateTime.now();
                        String taken_at = currentDT.format(formatter);

                        savedBook.setBookLog(payload.getTaken_by(), taken_at, payload.getRent_period());

                        // save the data
                        DataGrab.write(savedBook);
                        return new ResponseEntity<>(
                                "Book added successfully on user behalf",
                                HttpStatus.OK
                        );

                    } catch (Exception e) {
                        return new ResponseEntity<>(
                                "rent_period cannot be converted to integer (period is in days) ",
                                HttpStatus.UNPROCESSABLE_ENTITY
                        );
                    }


                }
                else return new ResponseEntity<>(
                        "rent_period (period for which the book will be taken) is not set",
                        HttpStatus.BAD_REQUEST
                );
            else return new ResponseEntity<> (
                   "taken_by or (user taking the book) is not set",
                   HttpStatus.BAD_REQUEST
                );
        else return new ResponseEntity<>(
                "guid is not set",
                HttpStatus.BAD_REQUEST
        );
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteBook(@RequestBody Data payload) {
        if (payload.getGuid() != null) {
            Data existingBook = DataGrab.getBy(payload.getGuid());
            if (existingBook == null) {
                return new ResponseEntity<>(
                        "The book with specified GUID doesn't exist",
                        HttpStatus.NOT_FOUND
                );
            }
            return new ResponseEntity<>(
                "Book with GUID = " + DataGrab.delete(payload) + "is successfully deleted",
                HttpStatus.OK
            );

        }
        else return new ResponseEntity<>(
            "guid is not specified",
            HttpStatus.BAD_REQUEST
        );
    }







}
