package me.noitcereon.external.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This marker annotation indicates that a model was made based on an external json schema.
 */
@Documented
@Target(ElementType.ANNOTATION_TYPE)
public @interface JsonSchemaModel {
    /**
     * @return A link to the documentation, where the json schema can be found.
     */
    String documentationUrl();
}
