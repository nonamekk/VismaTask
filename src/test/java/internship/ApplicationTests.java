package internship;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
class ApplicationTests {

    @Test
    void bookIsSavedAndDeleted() {
        Data book = new Data("100", "100","100", "100","100", "100","100" );
        String savedGuid = DataGrab.saveNewBook(book);
        String setGuid = book.getGuid();

        assertEquals(savedGuid, setGuid);
        book = DataGrab.getBy("100");
        assertEquals(book.getGuid().equals(""), false);
        assertEquals(book.getName().equals(""), false);
        assertEquals(book.getAuthor().equals(""), false);
        assertEquals(book.getCategory().equals(""), false);
        assertEquals(book.getLanguage().equals(""), false);
        assertEquals(book.getIsbn().equals(""), false);
        assertEquals(book.getPublication_date().equals(""), false);

        assertEquals(book.getTaken_at().equals(""), true);
        assertEquals(book.getTaken_by().equals(""), true);
        assertEquals(book.getRent_period().equals(""), true);

        DataGrab.delete(book);
        assertEquals(DataGrab.getBy(setGuid), null);
    }

    @Test
    void listIsSorted() {

        List<Data> mainList = new ArrayList<Data>();
        List<Data> listA = new ArrayList<Data>();
        List<Data> listB = new ArrayList<Data>();


        listA = DataGrab.getSortedBy("name", "asc");
        listB = DataGrab.getSortedBy("name", "desc");
        assertEquals(listA.get(0).getName(), listB.get(listB.size()-1).getName());

        listA = DataGrab.getSortedBy("author", "asc");
        listB = DataGrab.getSortedBy("author", "desc");
        assertEquals(listA.get(0).getAuthor(), listB.get(listB.size()-1).getAuthor());

        listA = DataGrab.getSortedBy("category", "asc");
        listB = DataGrab.getSortedBy("category", "desc");
        assertEquals(listA.get(0).getCategory(), listB.get(listB.size()-1).getCategory());

        listA = DataGrab.getSortedBy("language", "asc");
        listB = DataGrab.getSortedBy("language", "desc");
        assertEquals(listA.get(0).getLanguage(), listB.get(listB.size()-1).getLanguage());

        listA = DataGrab.getSortedBy("isbn", "asc");
        listB = DataGrab.getSortedBy("isbn", "desc");
        assertEquals(listA.get(0).getIsbn(), listB.get(listB.size()-1).getIsbn());


        mainList = DataGrab.getAll();
        listA = DataGrab.getSortedBy("books_are", "available");
        listB = DataGrab.getSortedBy("books_are", "taken");
        assertEquals(mainList.size(), (listA.size()+listB.size()));
    }

    @Test
    void bookIsTakenSuccessfullyAndDelete() {
        Data book = new Data("1000", "100","100", "100","100", "100","100" );
        String bookGuid = DataGrab.saveNewBook(book);

        Data payload = new Data(bookGuid, null, null, null, null, null, null, "Bill", null, "42");

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime currentDT = LocalDateTime.now();
        String taken_at = currentDT.format(formatter);

        Data savedBook = DataGrab.getBy(bookGuid);
        savedBook.setBookLog(payload.getTaken_by(), taken_at, payload.getRent_period());
        DataGrab.write(savedBook);

        assertEquals(DataGrab.getBy(bookGuid).getTaken_at(), taken_at);

        DataGrab.delete(DataGrab.getBy(bookGuid));
        assertEquals(DataGrab.getBy(bookGuid), null);
    }

}
