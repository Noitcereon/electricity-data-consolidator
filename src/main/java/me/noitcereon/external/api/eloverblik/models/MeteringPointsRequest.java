package me.noitcereon.external.api.eloverblik.models;

import java.util.ArrayList;
import java.util.List;

@EloverblikApiModel
public record MeteringPointsRequest (MeteringPoints meteringPoints){
    public static MeteringPointsRequest from(List<MeteringPointApiDto> meteringPointApiDtos) {
        List<String> meteringPoints = new ArrayList<>();
        for (MeteringPointApiDto item : meteringPointApiDtos){
            meteringPoints.add(item.getMeteringPointId());
        }
        return new MeteringPointsRequest(new MeteringPoints(meteringPoints));
    }
}
