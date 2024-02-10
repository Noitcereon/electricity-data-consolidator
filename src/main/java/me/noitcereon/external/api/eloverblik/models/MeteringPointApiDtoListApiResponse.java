package me.noitcereon.external.api.eloverblik.models;

import java.util.List;

@EloverblikApiModel
public class MeteringPointApiDtoListApiResponse {
    private List<MeteringPointApiDto> result;

    public MeteringPointApiDtoListApiResponse() {
        // Empty constructor for serialization
    }

    public List<MeteringPointApiDto> getResult() {
        return result;
    }

    public void setResult(List<MeteringPointApiDto> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MeteringPointApiDtoListApiResponse{" +
                "result=" + result +
                '}';
    }
}
