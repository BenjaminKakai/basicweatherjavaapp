import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApp extends Application {
    private Label temperatureLabel;
    private Label humidityLabel;
    private Label conditionLabel;
    private TextField locationField;
    private Button submitButton;
    private String API_KEY = "your_api_key";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        temperatureLabel = new Label();
        humidityLabel = new Label();
        conditionLabel = new Label();
        locationField = new TextField();
        submitButton = new Button("Submit");

        submitButton.setOnAction(event -> {
            String location = locationField.getText();
            String weatherJson = getWeatherData(location);
            updateWeatherInfo(weatherJson);
        });

        HBox locationBox = new HBox(new Label("Location:"), locationField, submitButton);
        locationBox.setSpacing(10);
        locationBox.setPadding(new Insets(10));

        VBox root = new VBox(locationBox, temperatureLabel, humidityLabel, conditionLabel);
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Weather App");
        primaryStage.show();
    }

    private String getWeatherData(String location) {
        try {
            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + API_KEY;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            connection.disconnect();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
       
