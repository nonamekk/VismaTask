package internship;

public class Data {
    private String guid;
    private String name;
    private String author;
    private String category;
    private String language;
    private String publication_date;
    private String isbn;
    private String taken_by;
    private String taken_at;
    private String rent_period;

    public Data() {

    }

    public Data(String guid, String name, String author, String category, String language, String publication_date, String isbn) {
        this.guid = guid;
        this.name = name;
        this.author = author;
        this.category = category;
        this.language = language;
        this.publication_date = publication_date;
        this.isbn = isbn;
        this.taken_by = "";
        this.taken_at = "";
        this.rent_period = "";
    }

    public Data(String guid, String name, String author, String category, String language, String publication_date, String isbn, String taken_by, String taken_at, String rent_period) {
        this.guid = guid;
        this.name = name;
        this.author = author;
        this.category = category;
        this.language = language;
        this.publication_date = publication_date;
        this.isbn = isbn;
        this.taken_by = taken_by;
        this.taken_at = taken_at;
        this.rent_period = rent_period;
    }

    public String getGuid() {
        return guid;
    }

    public String getName() {
        return name;
    }
    public String getAuthor() { return author;}
    public String getCategory() { return category;}
    public String getLanguage() { return language;}
    public String getPublication_date() { return publication_date;}
    public String getIsbn() { return isbn;}
    public String getTaken_by() {return taken_by;}
    public String getTaken_at() { return taken_at;}
    public String getRent_period() { return rent_period;}

   // public String getJSON() {
   //     return "{\"guid\": \""+guid+"\", \"name\": \""+name+"\", \"author\": \""+author+"\", \"category\": \""+category+"\", \"language\": \""+language+"\", \"publicationdate\": \""+ publicationdate +"\", \"isbn\": \""+isbn+"\", \"takenby\": \""+ takenby +"\", \"taken_at\": \""+ takenat +"\", \"rent_period\": \""+ rentperiod +"\"}";
   // }

    public void setBookLog(String taken_by, String taken_at, String rent_period) {
        this.taken_by = taken_by;
        this.taken_at = taken_at;
        this.rent_period = rent_period;
    }
}
