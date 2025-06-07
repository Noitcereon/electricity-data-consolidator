package me.noitcereon.external.api.eloverblik.models;

import java.util.ArrayList;
import java.util.List;

@EloverblikApiModel
public record MeteringPointsRequest(MeteringPoints meteringPoints) {
    public static MeteringPointsRequest from(List<MeteringPointApiDto> meteringPointApiDtos) {
        return from(meteringPointApiDtos, true);
    }

    public static MeteringPointsRequest from(List<MeteringPointApiDto> meteringPointApiDtos, boolean includeChildMeteringPoints) {
        List<String> meteringPoints = new ArrayList<>();
        for (MeteringPointApiDto meteringPoint : meteringPointApiDtos) {
            meteringPoints.add(meteringPoint.getMeteringPointId());
            if (meteringPoint.getChildMeteringPoints().isEmpty() || !includeChildMeteringPoints) {
                continue; // Don't try to add child metering points.
            }
            for (ChildMeteringPointDto childMeteringPoint : meteringPoint.getChildMeteringPoints()) {
                meteringPoints.add(childMeteringPoint.getMeteringPointId());
            }
        }
        return new MeteringPointsRequest(new MeteringPoints(meteringPoints));
    }
}
