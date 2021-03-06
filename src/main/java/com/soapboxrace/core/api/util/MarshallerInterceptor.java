/*
 * This file is part of the Soapbox Race World core source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Copyright (c) 2020.
 */

package com.soapboxrace.core.api.util;

import com.soapboxrace.jaxb.annotation.XsiSchemaLocation;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.APPLICATION_XML)
public class MarshallerInterceptor implements MessageBodyWriter<Object> {

    @Context
    protected Providers providers;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType
            , MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws WebApplicationException {
        if (object != null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
                jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
                if (annotations != null) {
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof XsiSchemaLocation) {
                            XsiSchemaLocation schemaAnnotation = (XsiSchemaLocation) annotation;
                            String schemaLocation = schemaAnnotation.schemaLocation();
                            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
                        }
                    }
                }
                XmlType xmlTypeAnnotation = object.getClass().getAnnotation(XmlType.class);
                QName qname = new QName("", xmlTypeAnnotation.name());
                StringWriter stringWriter = new StringWriter();
                JAXBElement<Object> jaxbElement = new JAXBElement<>(qname, (Class<Object>) object.getClass(),
                        null, object);
                jaxbMarshaller.marshal(jaxbElement, stringWriter);
                entityStream.write(stringWriter.toString().getBytes());
            } catch (Exception e) {
                throw new WebApplicationException(e);
            }
        }
    }

    @Override
    public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

}
