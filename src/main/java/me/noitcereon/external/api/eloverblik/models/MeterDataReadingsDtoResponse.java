package me.noitcereon.external.api.eloverblik.models;

@EloverblikApiModel
public record MeterDataReadingsDtoResponse(boolean success, int errorCode, String errorText, String id,
                                           String stackTrace, MeterDataReadingsDto result) {
}
