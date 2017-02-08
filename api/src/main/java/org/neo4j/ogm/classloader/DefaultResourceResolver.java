/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with
 * separate copyright notices and license terms. Your use of the source
 * code for these subcomponents is subject to the terms and
 *  conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package org.neo4j.ogm.classloader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author vince
 */
public class DefaultResourceResolver implements ResourceResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultResourceResolver.class);
    private static Method osgiFinder;

    static {
        try {
            osgiFinder = Class.forName("org.eclipse.core.runtime.FileLocator").getMethod("toFileURL", URL.class);
        } catch (Throwable ex) {
            LOG.debug("Unable to find org.eclipse.core.runtime.FileLocator#resolve. Not in an OSGi environment.");
        }
    }

    @Override
    public File resolve( final URL resource ) throws URISyntaxException, MalformedURLException
    {
        if(resource.getProtocol().equals("file")) {
            return new File(resource.toURI());
        }

        if(resource.getProtocol().equals("jar")) {
            String jarFileURL = resource.getPath().substring(0, resource.getPath().indexOf("!"));  //Strip out the jar protocol
            return resolve(new URL(jarFileURL));
        }

        if (osgiFinder != null && resource.getProtocol().equals("bundleresource")) {
            try {
                URL url = (URL) osgiFinder.invoke(null, resource);
                return new File(url.getPath());
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.warn("Error resolving OSGi bundle {} - {}", resource, e.getMessage());
            }
        }

        return null; // not handled
    }
}
