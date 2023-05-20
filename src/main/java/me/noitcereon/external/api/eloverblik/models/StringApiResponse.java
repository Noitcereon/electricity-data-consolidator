package me.noitcereon.external.api.eloverblik.models;

public class StringApiResponse {
    private String result;

    public StringApiResponse() {
        // For serialization
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "StringApiResponse{" +
                "result='" + result + '\'' +
                '}';
    }
}
