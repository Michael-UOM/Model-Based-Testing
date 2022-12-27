package org.example.api;

import com.google.gson.Gson;
import okhttp3.*;
import org.example.Alert;
import org.example.GetResponse;

import java.io.IOException;

public class ApiFunctionality {
    final OkHttpClient httpClient = new OkHttpClient();
    private int numberOfAlerts = 0;
    private boolean uploaded = false;
    private boolean purged = false;
    private boolean alertType = false;
    private boolean heading = false;
    private boolean description  = false;
    private boolean url = false;
    private boolean imageUrl = false;
    private boolean postedBy = false;
    private boolean priceInCents = false;

    int getNumberOfAlerts() {
        return numberOfAlerts;
    }

    boolean isUploaded(){
        return uploaded;
    }

    boolean isPurged() {
        return purged;
    }

    boolean containsAlertType(){
        return alertType;
    }

    boolean containsHeading(){
        return heading;
    }

    boolean containsDescription(){
        return description;
    }

    boolean containsUrl() {
        return url;
    }

    boolean containsImageUrl() {
        return imageUrl;
    }

    boolean containsPostedBy() {
        return postedBy;
    }

    boolean containsPriceInCents() {
        return priceInCents;
    }

    public void initialPurgeAlerts() throws IOException {
        Request request = new Request.Builder().url("https://api.marketalertum.com/Alert?userId=c55bc56a-232c-46a4-9778-7f0d41690aa2").delete().build();
        try (Response response = httpClient.newCall(request).execute()) {}
    }

    void purgeAlerts() throws IOException {
        Request request = new Request.Builder().url("https://api.marketalertum.com/Alert?userId=c55bc56a-232c-46a4-9778-7f0d41690aa2").delete().build();
        try (Response response = httpClient.newCall(request).execute()) {}

        // Update status according to status of the MarketAlertUm website
        GetResponse getResponse = getRequestFromMarketAlertUm();
        if (getResponse != null && getResponse.eventLogType == 1){
            purged = true;
            uploaded = false;
            numberOfAlerts = getResponse.systemState.alerts.size();
        } else {
            purged = false;
        }
    }

    void uploadCorrectAlert() throws IOException {
        Alert correctAlert = new Alert(
                2,
                "Trimarchi 57 S. - Tohatsu 115 hp",
                "Trimarchi 57 S (5.70 mt.) Year 2021 ° with Tohatsu 115 hp engine year 2022 ° with only 30 hours of motion",
                "https://www.maltapark.com/item/details/9516448",
                "https://www.maltapark.com/asset/itemphotos/9516448/9516448_1.jpg?_ts=4",
                2400000
        );

        // Upload alert
        String json = new Gson().toJson(correctAlert);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://api.marketalertum.com/Alert")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        }

        GetResponse getResponse = getRequestFromMarketAlertUm();
        if (getResponse != null && getResponse.eventLogType == 0){
            purged = false;
            uploaded = true;
            numberOfAlerts = getResponse.systemState.alerts.size();
            Alert finalAlert = getResponse.systemState.alerts.get(numberOfAlerts - 1);

            // Check that alerts have all attributes required
            alertType = finalAlert.alertType >= 1 && finalAlert.alertType <= 6;

            heading = !finalAlert.heading.equals("");

            description = !finalAlert.description.equals("");

            url = !finalAlert.url.equals("");

            imageUrl = !finalAlert.imageURL.equals("");

            postedBy = !finalAlert.postedBy.equals("");

            priceInCents = finalAlert.priceInCents > 0;
        } else {
            uploaded = false;
            purged = false;
            alertType = false;
            heading = false;
            description = false;
            url = false;
            imageUrl = false;
            postedBy = false;
            priceInCents = false;
        }
    }

    void uploadIncorrectAlert() throws IOException {
        Alert correctAlert = new Alert(
                2,
                "Trimarchi 57 S. - Tohatsu 115 hp",
                "Trimarchi 57 S (5.70 mt.) Year 2021 ° with Tohatsu 115 hp engine year 2022 ° with only 30 hours of motion",
                "https://www.maltapark.com/item/details/9516448",
                "https://www.maltapark.com/asset/itemphotos/9516448/9516448_1.jpg?_ts=4",
                2400000
        );

        // Upload alert
        String json = new Gson().toJson(correctAlert);

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://api.marketalertum.com/Alert")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        }

        GetResponse getResponse = getRequestFromMarketAlertUm();
        if (getResponse != null && getResponse.eventLogType == 0){
            purged = false;
            uploaded = true;
            numberOfAlerts = getResponse.systemState.alerts.size();

            Alert finalAlert = getResponse.systemState.alerts.get(numberOfAlerts - 1);

            // Check that alerts have all attributes required
            alertType = finalAlert.alertType >= 1 && finalAlert.alertType <= 6;

            heading = !finalAlert.heading.equals("");

            description = !finalAlert.description.equals("");

            url = !finalAlert.url.equals("");

            imageUrl = !finalAlert.imageURL.equals("");

            postedBy = !finalAlert.postedBy.equals("");

            priceInCents = finalAlert.priceInCents > 0;

        } else {
            uploaded = false;
            purged = false;
            alertType = false;
            heading = false;
            description = false;
            url = false;
            imageUrl = false;
            postedBy = false;
            priceInCents = false;
        }
    }

    GetResponse getRequestFromMarketAlertUm() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.marketalertum.com/EventsLog/c55bc56a-232c-46a4-9778-7f0d41690aa2")
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = response.body().string();

            GetResponse[] responseArray = new Gson().fromJson(jsonString, GetResponse[].class);

            // Return GetResponse object
            return responseArray[0];
        }
    }
}
