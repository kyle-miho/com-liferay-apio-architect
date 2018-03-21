/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.apio.architect.message.json;

import aQute.bnd.annotation.ConsumerType;

import com.liferay.apio.architect.alias.RequestFunction;
import com.liferay.apio.architect.documentation.Documentation;
import com.liferay.apio.architect.form.FormField;

import java.util.Optional;

/**
 * Maps the API {@link Documentation} to its JSON object representation.
 * Instances of this interface work like events. The {@code
 * javax.ws.rs.ext.MessageBodyWriter} of the {@code Documentation} calls the
 * {@code DocumentationMessageMapper} methods. In each method, developers should
 * only map the provided part of the resource to its representation in a JSON
 * object. To enable this, each method receives a {@link JSONObjectBuilder}.
 *
 * <p>
 * The methods {@link #onStart(JSONObjectBuilder, Object, HttpHeaders)} and
 * {@link #onFinish(JSONObjectBuilder, Object, HttpHeaders)} are called when the
 * writer starts and finishes the page, respectively. Otherwise, the page
 * message mapper's methods aren't called in a particular order.
 * </p>
 *
 * @author Alejandro Hernández
 */
@ConsumerType
@SuppressWarnings("unused")
public interface DocumentationMessageMapper
	extends MessageMapper<Documentation> {

	/**
	 * Maps the API description to its JSON object representation.
	 *
	 * @param jsonObjectBuilder the JSON object builder for the documentation
	 * @param description the API description
	 */
	public default void mapDescription(
		JSONObjectBuilder jsonObjectBuilder, String description) {
	}

	public default void mapFormField(
		JSONObjectBuilder jsonObjectBuilder, Object name,
		Optional<FormField> formFieldOptional) {
	}

	public default void mapOperation(
		JSONObjectBuilder jsonObjectBuilder, String entity,
		RequestFunction requestFunction) {
	}

	public default void mapResource(
		JSONObjectBuilder jsonObjectBuilder, String entity) {
	}

	/**
	 * Maps the API title to its JSON object representation.
	 *
	 * @param jsonObjectBuilder the JSON object builder for the documentation
	 * @param title the API title
	 */
	public default void mapTitle(
		JSONObjectBuilder jsonObjectBuilder, String title) {
	}

	public default void onFinishOperation(
		JSONObjectBuilder jsonObjectBuilder,
		JSONObjectBuilder propertyJsonObjectBuilder) {
	}

	public default void onFinishProperty(
		JSONObjectBuilder jsonObjectBuilder,
		JSONObjectBuilder propertyJsonObjectBuilder) {
	}

	public default void onFinishResource(
		JSONObjectBuilder jsonObjectBuilder,
		JSONObjectBuilder entityJsonObjectBuilder) {
	}

	public default void onStartOperation(
		JSONObjectBuilder jsonObjectBuilder,
		JSONObjectBuilder operationJsonObjectBuilder) {
	}

	public default void onStartProperty(
		JSONObjectBuilder jsonObjectBuilder,
		JSONObjectBuilder propertyJsonObjectBuilder) {
	}

	public default void onStartResource(
		JSONObjectBuilder jsonObjectBuilder,
		JSONObjectBuilder entityJsonObjectBuilder) {
	}

}