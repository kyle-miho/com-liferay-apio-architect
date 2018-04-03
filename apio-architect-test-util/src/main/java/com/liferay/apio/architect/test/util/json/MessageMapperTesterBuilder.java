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

package com.liferay.apio.architect.test.util.json;

import static java.nio.charset.StandardCharsets.UTF_8;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import com.liferay.apio.architect.error.APIError;
import com.liferay.apio.architect.functional.Try;
import com.liferay.apio.architect.message.json.DocumentationMessageMapper;
import com.liferay.apio.architect.message.json.ErrorMessageMapper;
import com.liferay.apio.architect.message.json.FormMessageMapper;
import com.liferay.apio.architect.message.json.MessageMapper;
import com.liferay.apio.architect.message.json.PageMessageMapper;
import com.liferay.apio.architect.message.json.SingleModelMessageMapper;
import com.liferay.apio.architect.test.util.model.RootModel;
import com.liferay.apio.architect.test.util.writer.MockDocumentationWriter;
import com.liferay.apio.architect.test.util.writer.MockFormWriter;
import com.liferay.apio.architect.test.util.writer.MockPageWriter;
import com.liferay.apio.architect.test.util.writer.MockSingleModelWriter;
import com.liferay.apio.architect.writer.ErrorWriter;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;

/**
 * Utility class that can be used to test different message mappers of the same
 * media type using the "snapshot testing" technique.
 *
 * @author Alejandro Hernández
 * @review
 */
public class MessageMapperTesterBuilder {

	/**
	 * Provides information about the HTTP headers. This object can be real or
	 * created with mock frameworks.
	 *
	 * @param  httpHeaders the request HTTP headers.
	 * @return the next step of the builder
	 * @review
	 */
	public static MediaTypeStep httpHeaders(HttpHeaders httpHeaders) {
		return new MediaTypeStep(httpHeaders);
	}

	public static class MediaTypeStep {

		/**
		 * Sets the media type that the different message mappers should have
		 *
		 * @param  mediaType the media type of the tested message mappers
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep mediaType(MediaType mediaType) {
			return new MessageMapperStep(_httpHeaders, mediaType.toString());
		}

		/**
		 * Sets the media type that the different message mappers should have
		 *
		 * @param  mediaType the media type of the tested message mappers
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep mediaType(String mediaType) {
			return new MessageMapperStep(_httpHeaders, mediaType);
		}

		private MediaTypeStep(HttpHeaders httpHeaders) {
			_httpHeaders = httpHeaders;
		}

		private final HttpHeaders _httpHeaders;

	}

	public static class MessageMapperStep {

		/**
		 * Creates a {@code documentation.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file will
		 * contain the representation created by the message mapper.
		 *
		 * @param  documentationMessageMapper the message mapper
		 * @param  path the path in which the test lives
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep createDocumentationFile(
			DocumentationMessageMapper documentationMessageMapper, Path path) {

			String actual = MockDocumentationWriter.write(
				_httpHeaders, documentationMessageMapper);

			_createFile(actual, path, "documentation");

			return this;
		}

		/**
		 * Creates a {@code error.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file will
		 * contain the representation created by the message mapper.
		 *
		 * @param  errorMessageMapper the message mapper
		 * @param  path the path in which the test lives
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep createErrorFile(
			ErrorMessageMapper errorMessageMapper, Path path) {

			String actual = ErrorWriter.writeError(
				errorMessageMapper, _MOCK_API_ERROR, _httpHeaders);

			_createFile(actual, path, "error");

			return this;
		}

		/**
		 * Creates a {@code form.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file will
		 * contain the representation created by the message mapper.
		 *
		 * @param  formMessageMapper the message mapper
		 * @param  path the path in which the test lives
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep createFormFile(
			FormMessageMapper formMessageMapper, Path path) {

			String actual = MockFormWriter.write(
				_httpHeaders, formMessageMapper);

			_createFile(actual, path, "form");

			return this;
		}

		/**
		 * Creates a {@code page.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file will
		 * contain the representation created by the message mapper.
		 *
		 * @param  pageMessageMapper the message mapper
		 * @param  path the path in which the test lives
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep createPageFile(
			PageMessageMapper<RootModel> pageMessageMapper, Path path) {

			String actual = MockPageWriter.write(
				_httpHeaders, pageMessageMapper);

			_createFile(actual, path, "page");

			return this;
		}

		/**
		 * Creates a {@code single_model.json.auto} file inside the {@code
		 * src/test/resources} directory in the provided path. The file will
		 * contain the representation created by the message mapper.
		 *
		 * @param  singleModelMessageMapper the message mapper
		 * @param  path the path in which the test lives
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep createSingleModelFile(
			SingleModelMessageMapper<RootModel> singleModelMessageMapper,
			Path path) {

			String actual = MockSingleModelWriter.write(
				_httpHeaders, singleModelMessageMapper);

			_createFile(actual, path, "single_model");

			return this;
		}

		/**
		 * Validates that the output created by the provided message mapper
		 * matches the content of the {@code
		 * /src/test/resources/documentation.json} file.
		 *
		 * @param  documentationMessageMapper the message mapper
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep validateDocumentationMessageMapper(
			DocumentationMessageMapper documentationMessageMapper) {

			String actual = MockDocumentationWriter.write(
				_httpHeaders, documentationMessageMapper);

			_validateMessageMapper(
				documentationMessageMapper, actual, "documentation");

			return this;
		}

		/**
		 * Validates that the output created by the provided message mapper
		 * matches the content of the {@code /src/test/resources/error.json}
		 * file.
		 *
		 * @param  errorMessageMapper the message mapper
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep validateErrorMessageMapper(
			ErrorMessageMapper errorMessageMapper) {

			String actual = ErrorWriter.writeError(
				errorMessageMapper, _MOCK_API_ERROR, _httpHeaders);

			_validateMessageMapper(errorMessageMapper, actual, "error");

			return this;
		}

		/**
		 * Validates that the output created by the provided message mapper
		 * matches the content of the {@code /src/test/resources/form.json}
		 * file.
		 *
		 * @param  formMessageMapper the message mapper
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep validateFormMessageMapper(
			FormMessageMapper formMessageMapper) {

			String actual = MockFormWriter.write(
				_httpHeaders, formMessageMapper);

			_validateMessageMapper(formMessageMapper, actual, "form");

			return this;
		}

		/**
		 * Validates that the output created by the provided message mapper
		 * matches the content of the {@code /src/test/resources/page.json}
		 * file.
		 *
		 * @param  pageMessageMapper the message mapper
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep validatePageMessageMapper(
			PageMessageMapper<RootModel> pageMessageMapper) {

			String actual = MockPageWriter.write(
				_httpHeaders, pageMessageMapper);

			_validateMessageMapper(pageMessageMapper, actual, "page");

			return this;
		}

		/**
		 * Validates that the output created by the provided message mapper
		 * matches the content of the {@code
		 * /src/test/resources/single_model.json} file.
		 *
		 * @param  singleModelMessageMapper the message mapper
		 * @return the next step of the builder
		 * @review
		 */
		public MessageMapperStep validateSingleModelMessageMapper(
			SingleModelMessageMapper<RootModel> singleModelMessageMapper) {

			String actual = MockSingleModelWriter.write(
				_httpHeaders, singleModelMessageMapper);

			_validateMessageMapper(
				singleModelMessageMapper, actual, "single_model");

			return this;
		}

		private MessageMapperStep(HttpHeaders httpHeaders, String mediaType) {
			_httpHeaders = httpHeaders;
			_mediaType = mediaType;
		}

		private void _createFile(String actual, Path path, String fileName) {
			Path newPath = Paths.get(path.toString(), fileName + ".json.auto");

			try {
				Files.write(newPath, actual.getBytes());
			}
			catch (IOException ioe) {
				throw new AssertionError(
					"Unable to create the file with path: " + newPath);
			}

			throw new AssertionError("File doesn't exist. Creating...");
		}

		private void _validateMessageMapper(
			MessageMapper messageMapper, String actual, String fileName) {

			assertThat(messageMapper.getMediaType(), is(_mediaType));

			Try<Class<?>> classTry = Try.fromFallible(this::getClass);

			String file = fileName + ".json";

			String expected = classTry.map(
				Class::getClassLoader
			).map(
				classLoader -> classLoader.getResource(file)
			).map(
				URL::getPath
			).map(
				File::new
			).map(
				File::toPath
			).map(
				Files::readAllBytes
			).map(
				bytes -> new String(bytes, UTF_8)
			).orElseThrow(
				() -> new AssertionError("Unable to found the file: " + file)
			);

			try {
				assertEquals(expected, actual, true);
			}
			catch (JSONException jsone) {
				throw new AssertionError(
					"An error occurred while parsing the file: " + file);
			}
		}

		private static final APIError _MOCK_API_ERROR = new APIError(
			new IllegalArgumentException(), "A title", "A description",
			"A type", 404);

		private final HttpHeaders _httpHeaders;
		private final String _mediaType;

	}

}