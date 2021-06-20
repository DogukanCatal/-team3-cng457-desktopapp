/*Important Note:
*
* There is one error in this program
* you can not get more than 2 items at the same time from database
* otherwise every necessary operation works well
* */
package sample;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

//We take IDs from our strings with REGEX
class Regex {
    public String operate(String input){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(input);
        m.find();
        return(m.group());
    }
    public static void main(String[]args) {

    }
}
//Usig merge sort we complete Sort By Price
class MergeSort
{
    void merge(float arr[], int l, int m, int r)
    {
        int n1 = m - l + 1;
        int n2 = r - m;

        float L[] = new float[n1];
        float R[] = new float[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        int i = 0, j = 0;

        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            }
            else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
    void sort(float arr[], int l, int r)
    {
        if (l < r) {
            int m =l+ (r-l)/2;
            sort(arr, l, m);
            sort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }
}

public class Controller {
    @FXML
    private TextArea UserInput;
    @FXML
    private ListView AllList;
    @FXML
    private ListView CommentRatingList;
    @FXML
    private ListView BasicInfoList;
    //we defined this values here to use them in all other funcitons easily.
    JSONParser parser = new JSONParser();
    String response = "";
    Object obj;
    JSONArray array;
    ArrayList<String> IDarray = new ArrayList<String>();
    public void GetButtonPressed(ActionEvent event) throws IOException, ParseException {
        //clear id list every time
        IDarray.clear();
        //first set the selection mode
        AllList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //clear lists always
        AllList.getItems().clear();
        Object obj2;
        String response = "";
        //take user input and set URL
        String input = UserInput.getText();
        String url_connection = "http://localhost:8080/";
        url_connection = url_connection.concat(input);
        HttpURLConnection connection = (HttpURLConnection) new URL(url_connection).openConnection();
        connection.setRequestMethod("GET");
        int responsecode = connection.getResponseCode();
        if(responsecode == 200) {
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                response += scanner.nextLine();
                response += "\n";
            }
            scanner.close();
        }

        obj = parser.parse(response);
        array = (JSONArray) obj;
        String stdlist = "";
        String note = "";
        String value = "";
        String brand = "";
        String tempString = "";
        for(int i = 0 ; i< array.size(); i++){
            JSONObject temp = (JSONObject) array.get(i);
            value = "";
            //check if it is Phone or Computer
            //use "internal_memory" and "memory" for this operation
            //Computer has no "internal_memory"
            //Phone has no "memory"
            value += temp.get("internal_memory");
            stdlist = "";
            if(!value.equals("null")) {
                //check if it is 0 or not to be sure
                if (Float.parseFloat(value) > 0) {
                    //find the brand name of product
                    //send a request to web server
                    //then take name of the brand
                    //temp2 used for this operation
                    tempString = "";
                    tempString += temp.get("brand");
                    obj2 = parser.parse(tempString);
                    JSONObject temp2 = (JSONObject) obj2;
                    note = "";
                    //telephone
                    //set the notes for products
                    value += temp.get("screen_size");
                    if (Float.parseFloat(value) > 6)
                        note += " Large Screen";
                    value = "";
                    value += temp.get("internal_memory");
                    if (parseInt(value) > 128)
                        note += " Large Storage";
                    value = "";
                    value += temp.get("prod_id");
                    stdlist += temp2.get("name")+"  "  + temp.get("model") + "  " + note;
                    IDarray.add(value);
                    value = "";
                    stdlist += "\n";
                    AllList.getItems().add(stdlist);
                }
            }
            value = "";
            //it is computer or not
            value += temp.get("memory");
            //check it is zero or not to be sure
            if(!value.equals("null")) {
                if (parseInt(value) > 0) {
                    //again find name of the brand with same operation
                    tempString = "";
                    tempString += temp.get("brand");
                    obj2 = parser.parse(tempString);
                    JSONObject temp2 = (JSONObject) obj2;

                    //computer
                    note = "";
                    value = "";
                    value += temp.get("storage_capacity");

                    if (Float.parseFloat(value) > 1 && Float.parseFloat(value) <= 5)
                        note += " Large Storage";
                    value = "";
                    value += temp.get("memory");
                    if (parseInt(value) > 16)
                        note += " Large Memory";
                    value = "";
                    value += temp.get("prod_id");
                    IDarray.add(value);
                    value = "";
                    stdlist += temp2.get("name")+" " +  temp.get("model") + " " + note;
                    AllList.getItems().add(stdlist);

                }
            }
        }
    }

    public void SortButtonPressed(ActionEvent event) throws ParseException {
        IDarray.clear();
        Object obj2;
        String tempString = "";
        //firstly,clear list
        AllList.getItems().clear();
        MergeSort sort = new MergeSort();
        String value = "";
        String note = "";
        String stdlist = "";
        //we will use merge sort
        //to use merge sort, first define an array then fill it with right values
        float sortArray[] = new float[array.size()];
        for(int i = 0 ; i< array.size(); i++){
            value = "";
            JSONObject temp = (JSONObject) array.get(i);
            value +=  temp.get("price");
            sortArray[i] = Float.parseFloat(value);
        }
        //apply merge sort
        sort.sort(sortArray, 0, sortArray.length-1);
        //use nested loops to check JSON values and sorted array at the same time
        //while checking, print in right order (increasing order)
        for(int j = 0 ; j < array.size() ; j++){
            for(int i = 0 ; i < array.size() ; i++) {
                value = "";
                stdlist = "";
                //use same operations to print right values to listview
                JSONObject temp = (JSONObject) array.get(i);
                value +=  temp.get("price");
                if(sortArray[j] == parseInt(value)){
                    value = "";
                    value += temp.get("internal_memory");
                    if(!value.equals("null")) {
                        if (Float.parseFloat(value) != 0) {
                            tempString = "";
                            tempString += temp.get("brand");
                            obj2 = parser.parse(tempString);
                            JSONObject temp2 = (JSONObject) obj2;

                            note = "";
                            //telephone
                            value += temp.get("screen_size");
                            if (Float.parseFloat(value) > 6)
                                note += " Large Screen";
                            value = "";
                            value += temp.get("internal_memory");
                            if (parseInt(value) > 128)
                                note += " Large Storage";
                            value = "";
                            value += temp.get("prod_id");
                            IDarray.add(value);
                            value = "";
                            stdlist += temp2.get("name")+" " + temp.get("model") + " " + note;
                            stdlist += "\n";
                            AllList.getItems().add(stdlist);
                        }
                    }
                    value = "";
                    value += temp.get("memory");
                    if(!value.equals("null")) {
                        if(parseInt(value) > 0){
                            //computer
                            tempString = "";
                            tempString += temp.get("brand");
                            obj2 = parser.parse(tempString);
                            JSONObject temp2 = (JSONObject) obj2;

                            note = "";
                            value = "";
                            //telephone
                            value += temp.get("storage_capacity");
                            if (Float.parseFloat(value) > 1 && Float.parseFloat(value) <= 5)
                                note += " Large Storage";
                            value = "";
                            value += temp.get("memory");
                            if (parseInt(value) > 16)
                                note += " Large Memory";
                            value = "";
                            value +=temp.get("prod_id");
                            IDarray.add(value);
                            value = "";
                            stdlist += temp2.get("name") +" " + temp.get("model") + " " + note;
                            stdlist += "\n";
                            AllList.getItems().add(stdlist);
                        }
                    }
                }
            }
        }
    }
    public void CompareButtonPressed(ActionEvent event) throws IOException, ParseException {
        //regex will be used for strings
        Regex regex = new Regex();
        JSONArray arraycomment;
        int control;
        String testValue = "";
        String test = "";
        String tempString = "";
        //clear lists first
        BasicInfoList.getItems().clear();
        CommentRatingList.getItems().clear();
        ObservableList<Integer> topics;
        //take selected indexes from ID array
        topics = AllList.getSelectionModel().getSelectedIndices();
        //user can not select more than 3 value from listview
        if(topics.size() > 3){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Operation");
            alert.setHeaderText("ERROR");
            alert.setContentText("You choosed more than 3 value from list");
        }
        else {
            for (int i = 0; i < topics.size(); i++) {
                tempString = "";
                test = "";
                //get product id from string
                Object obj2;
                //use right URL with right ID value
                String url_connection = "http://localhost:8080/getproduct/" + IDarray.get(topics.get(i));
                HttpURLConnection connection = (HttpURLConnection) new URL(url_connection).openConnection();
                connection.setRequestMethod("GET");
                int responsecode = connection.getResponseCode();
                if (responsecode == 200) {
                    Scanner scanner = new Scanner(connection.getInputStream());
                    while (scanner.hasNextLine()) {
                        tempString += scanner.nextLine();
                        tempString += "\n";
                    }
                    scanner.close();
                }
                //with same JSON operations take values from database and print basic informations of product
                obj2 = parser.parse(tempString);
                JSONObject temp2 = (JSONObject) obj2;
                ;
                tempString = "";
                tempString += "Product ID : " + temp2.get("prod_id") + " \n" + "Model : " + temp2.get("model") + "\n" + "Price : " + temp2.get("price") + "\n" + "Screen size : " + temp2.get("screen_size") + "\n";
                testValue = "";
                //check if it is Phone or Computer
                //we used same differentiation operation here
                //if it is phone, print internal memory, else print other necessary values
                testValue += temp2.get("internal_memory");
                if (!testValue.equals("null")) {
                    if (Float.parseFloat(testValue) > 0)
                        tempString += "Internal Memory : " + temp2.get("internal_memory") + "\n" + "Phone \n";
                }
                testValue = "";
                testValue += temp2.get("memory");
                if (!testValue.equals("null")) {
                    if (parseInt(testValue) > 0)
                        tempString += "Memory : " + temp2.get("memory") + "\n" + "Screen resolution" + temp2.get("screen_resolution") + "\n" + "Storage capacity : " + temp2.get("storage_capacity") + "\n" + "Proccesor : " + temp2.get("processor") + "\n" + "Computer \n";
                }
                BasicInfoList.getItems().add(tempString);
                //we completed basic informations here
                //now moving on the comments and rating
                //first we will use comments JSON variable to reach comments, because we already have the value, no need to use new URL
                //then print them in right order
                test = "";
                test += temp2.get("comments");
                obj2 = parser.parse(test);
                arraycomment = (JSONArray) obj2;
                test = "";
                float average = 0;
                String comments = "";
                //prepare the comments string for printing
                comments += "<--Last 3 comments-->\n";
                for (int m = 0; m < arraycomment.size(); m++) {
                    tempString = "";
                    temp2 = (JSONObject) arraycomment.get(m);
                    tempString += temp2.get("rate");
                    if (m > arraycomment.size() - 4)
                        comments += "*" + temp2.get("text") + "\n";
                    //calculate average at the same time
                    average += Float.parseFloat(tempString);
                }
                average /= arraycomment.size();
                tempString = "";
                tempString = "Average Rate : " + average + "\n" + comments;
                //finally, print items
                CommentRatingList.getItems().add(tempString);
            }
        }
    }
}
