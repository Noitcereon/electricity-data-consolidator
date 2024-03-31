package me.noitcereon.external.api.eloverblik.models;

public record MeteringPoint(String meteringPoint) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeteringPoint that)) return false;

        return meteringPoint.equals(that.meteringPoint);
    }

    @Override
    public int hashCode() {
        return meteringPoint.hashCode();
    }

    @Override
    public String toString() {
        return "MeteringPoint{" +
                "meteringPoint='" + meteringPoint + '\'' +
                '}';
    }
}