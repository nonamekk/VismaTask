package internship;

import java.util.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataGrab {

    private static final Map<String, Data> map = new HashMap<String, Data>();

    static {
        init();
    }

    private static void init() {
        // get data from the file and add data
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("./data/data.json")) {
            Object file = jsonParser.parse(reader);
            JSONArray list = (JSONArray) file;

            list.forEach( book -> readJSONData((JSONObject) book));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void readJSONData(JSONObject data) {

        String guid = (String) data.get("guid");
        String name = (String) data.get("name");
        String author = (String) data.get("author");
        String category = (String) data.get("category");
        String language = (String) data.get("language");
        String publication_date = (String) data.get("publication_date");
        String isbn = (String) data.get("isbn");
        String taken_by = (String) data.get("taken_by");
        String taken_at = (String) data.get("taken_at");
        String rent_period = (String) data.get("rent_period");

        Data info = new Data(guid, name, author, category, language, publication_date, isbn, taken_by, taken_at, rent_period);
        map.put(info.getGuid(), info);
    }

    private static void saveJSONData() {
        List<Data> list = new ArrayList<Data>();
        list = getAll();

        ArrayList<String> listOfJsonStrings = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {

            ArrayList jsonConverted = new ArrayList<String>();

            jsonConverted.add ("{\n\"guid\": \"" + list.get(i).getGuid() + "\",\n");
            jsonConverted.add ("\"name\": \"" + list.get(i).getName() + "\",\n");
            jsonConverted.add ("\"author\": \"" + list.get(i).getAuthor() + "\",\n");
            jsonConverted.add ("\"category\": \"" + list.get(i).getCategory() + "\",\n");
            jsonConverted.add ("\"language\": \"" + list.get(i).getLanguage() + "\",\n");
            jsonConverted.add ("\"publication_date\": \"" + list.get(i).getPublication_date() + "\",\n");
            jsonConverted.add ("\"isbn\": \"" + list.get(i).getIsbn() + "\",\n");
            jsonConverted.add ("\"taken_by\": \"" + list.get(i).getTaken_by() + "\",\n");
            jsonConverted.add ("\"taken_at\": \"" + list.get(i).getTaken_at() + "\",\n");
            jsonConverted.add ("\"rent_period\": \"" + list.get(i).getRent_period() + "\"\n}");

            String out = "";
            for (int j = 0; j < jsonConverted.size(); j++) {
                out += jsonConverted.get(j);
            }

            listOfJsonStrings.add(out);
        }
        try {
            FileWriter file = new FileWriter ("./data/data.json");
            file.write("[ ");
            for (int i = 0; i < listOfJsonStrings.size(); i++) {
                file.write(listOfJsonStrings.get(i));
                if (listOfJsonStrings.size()-1 > i ) {
                    file.write(",");
                }
            }
            file.write(" ]");
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static String toJSONString(Data book) {
        String output = "";
        output += "{\"guid\": \"" + book.getGuid() + "\",";
        output += "\"name\": \"" + book.getName() + "\",";
        output += "\"author\": \"" + book.getAuthor() + "\",";
        output += "\"category\": \"" + book.getCategory() + "\",";
        output += "\"language\": \"" + book.getLanguage() + "\",";
        output += "\"publication_date\": \"" + book.getPublication_date() + "\",";
        output += "\"isbn\": \"" + book.getIsbn() + "\",";
        output += "\"taken_by\": \"" + book.getTaken_by() + "\",";
        output += "\"taken_at\": \"" + book.getTaken_at() + "\",";
        output += "\"rent_period\": \"" + book.getRent_period() + "\"}";
        return output;
    }

    public static String toJSONString (List<Data> list) {
        if (list == null) {
            return null;
        }

        String output = "[ ";
        for (int i = 0; i < list.size(); i++) {
            if (i>0 && i < list.size()-1) {
                output += ", ";
            }
            output += "{\"guid\": \"" + list.get(i).getGuid() + "\",";
            output += "\"name\": \"" + list.get(i).getName() + "\",";
            output += "\"author\": \"" + list.get(i).getAuthor() + "\",";
            output += "\"category\": \"" + list.get(i).getCategory() + "\",";
            output += "\"language\": \"" + list.get(i).getLanguage() + "\",";
            output += "\"publication_date\": \"" + list.get(i).getPublication_date() + "\",";
            output += "\"isbn\": \"" + list.get(i).getIsbn() + "\",";
            output += "\"taken_by\": \"" + list.get(i).getTaken_by() + "\",";
            output += "\"taken_at\": \"" + list.get(i).getTaken_at() + "\",";
            output += "\"rent_period\": \"" + list.get(i).getRent_period() + "\"}";

        }
        output += " ]";
        return output;
    }

    public static List<Data> getAll(){
        Collection<Data> dataCollection = map.values();
        List<Data> list = new ArrayList<Data>();
        list.addAll(dataCollection);
        return list;
    }

    public static Data getBy(String guid) {
        return map.get(guid);
    }

    public static List<Data> getSortedBy(String filter, String parameter) {
        List<Data> list = new ArrayList<Data>();
        List<Data> newList = new ArrayList<Data>();
        list = getAll();

        if (filter.equals("name") || filter.equals("author") || filter.equals("category") || filter.equals("language") || filter.equals("isbn")) {
            Data temp;
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.size(); j++) {

                    String a = getCorrectString(list, i, filter);
                    String b = getCorrectString(list, j, filter);
                    if (a.equals("") || b.equals("")) {
                        // Error: Missing data in the json file
                        return null;
                        // call 500 and admin, should not be triggered
                    }

                    if (parameter.equals("desc")) {
                        // check if first is lexicographically greater (sort in descending)
                        if (a.compareTo(b) > 0) {
                            temp = list.get(i);
                            list.set(i, list.get(j));
                            list.set(j, temp);
                        }
                    } else if (parameter.equals("asc")) {
                        // check if first is lexicographically less (sort in ascending)
                        if (a.compareTo(b) < 0) {
                            temp = list.get(i);
                            list.set(i, list.get(j));
                            list.set(j, temp);
                        }
                    } else return null; // parameter wrong

                }
            }
            return list;
        }
        if (filter.equals("books_are")) {
            if (parameter.equals("taken")) {
                for (int i = 0; i < list.size(); i++) {
                    if (!(list.get(i).getTaken_at().equals("") && list.get(i).getTaken_by().equals("") && list.get(i).getRent_period().equals(""))) {
                        newList.add(list.get(i));
                    }
                }
            }
            else if (parameter.equals("available")) {
                for (int i = 0; i < list.size(); i++) {
                    if ((list.get(i).getTaken_at().equals("") && list.get(i).getTaken_by().equals("") && list.get(i).getRent_period().equals(""))) {
                        newList.add(list.get(i));
                    }
                }
            } else return null; // parameter wrong
            return newList;
        }
        if (filter.equals("guid")) {
            newList.add(getBy(parameter));
            return newList;
        }


        return null; // filter wrong
    }

    public static List<Data> getTakenBy(String user) {
        List<Data> list = new ArrayList<Data>();
        List<Data> newList = new ArrayList<Data>();
        list = getAll();
        boolean trigger = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTaken_by().equals(user)) {
                trigger = true;
                newList.add(list.get(i));
            }
        }
        if (trigger) return newList; else return null;
        // Not sure, but returning newList without data should also return null
    }

    private static String getCorrectString(List<Data> list, int i, String filter) {
        switch (filter) {
            case "name": return list.get(i).getName();
            case "author": return list.get(i).getAuthor();
            case "category": return list.get(i).getCategory();
            case "language": return list.get(i).getLanguage();
            case "isbn": return list.get(i).getIsbn();
            default : return "";
        }
    }

    public static String saveNewBook (Data book) {
        if (map.get(book.getGuid()) == null) {
            write (book);
            return book.getGuid();
        }
        else return null;

    }

    // add or update
    public static Data write (Data book) {
        map.put(book.getGuid(), book);
        saveJSONData();
        return book;
    }

    public static String delete (Data book) {
        map.remove(book.getGuid());
        saveJSONData();
        return book.getGuid();
    }

}
