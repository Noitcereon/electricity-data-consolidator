package me.noitcereon.external.api.eloverblik.models;

import me.noitcereon.external.api.JsonSchemaModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marker annotation to show that the annotated class is based on a model from the Eloverblik API.
 */
@JsonSchemaModel(documentationUrl = "https://api.eloverblik.dk/CustomerApi/index.html")
@Documented
@Target(ElementType.TYPE)
@interface EloverblikApiModel {

}

