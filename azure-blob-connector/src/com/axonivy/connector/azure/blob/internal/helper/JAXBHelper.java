package com.axonivy.connector.azure.blob.internal.helper;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.ivy.environment.Ivy;

public class JAXBHelper {
	private static final List<String> INVALID_CHARACTER_AT_BEGIN = List.of("\uFEFF");

	/**
	 * Unmarshal an XML string
	 */
	public static <T> T unmarshal(String xml, Class<T> type) {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(type);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			String validXml = removeInvalidCharacter(xml);
			StringReader xmlReader = new StringReader(validXml);
			T data = type.cast(jaxbUnmarshaller.unmarshal(xmlReader));
			xmlReader.close();

			return data;
		} catch (JAXBException e) {
			Ivy.log().warn("Convert to object is error. Message: ", e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Marshal an Object to XML.
	 * 
	 * @throws IOException
	 */
	public static String marshal(Object object) {
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller jaxbMarshaller = context.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

			StringWriter stringWriter = new StringWriter();
			jaxbMarshaller.marshal(object, stringWriter);
			stringWriter.close();

			return stringWriter.toString();
		} catch (JAXBException | IOException e) {
			Ivy.log().warn("Convert to xml is error. Message: ", e.getMessage(), e);
			return null;
		}
	}

	private static String removeInvalidCharacter(String xml) {
		AtomicReference<String> validXml = new AtomicReference<>(xml);
		INVALID_CHARACTER_AT_BEGIN.stream().filter(it -> xml.startsWith(it)).forEach(it -> {
			validXml.set(validXml.get().replaceFirst(it, StringUtils.EMPTY));
		});
		return validXml.get();
	}
}
