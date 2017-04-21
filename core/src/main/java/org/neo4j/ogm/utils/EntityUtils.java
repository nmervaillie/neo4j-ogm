/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with
 * separate copyright notices and license terms. Your use of the source
 * code for these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */
package org.neo4j.ogm.utils;

import java.lang.reflect.Field;
import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.neo4j.ogm.metadata.ClassInfo;
import org.neo4j.ogm.metadata.FieldInfo;
import org.neo4j.ogm.metadata.MetaData;
import org.neo4j.ogm.response.model.Neo4jNodeId;
import org.neo4j.ogm.response.model.NodeId;

/**
 * The utility methods here will all throw a <code>NullPointerException</code> if invoked with <code>null</code>.
 *
 * @author Luanne Misquitta
 */
public class EntityUtils {

    public static Long identity(Object entity, MetaData metaData) {

        ClassInfo classInfo = metaData.classInfo(entity);
        Object id = classInfo.identityField().readProperty(entity);
        return (id == null ? -System.identityHashCode(entity) : (Long) id);
    }

    /**
     * Returns the full set of labels, both static and dynamic, if any, to apply to a node.
     */
    public static Collection<String> labels(Object entity, MetaData metaData) {
        ClassInfo classInfo = metaData.classInfo(entity);
        Collection<String> staticLabels = classInfo.staticLabels();
        FieldInfo labelFieldInfo = classInfo.labelFieldOrNull();
        if (labelFieldInfo != null) {
            Collection<String> labels = (Collection<String>) labelFieldInfo.readProperty(entity);
            return CollectionUtils.union(staticLabels, labels);
        }
        return staticLabels;
    }

    public static Long getEntityId(MetaData metaData, Object entity) {
        return (Long) metaData.classInfo(entity).identityField().readProperty(entity);
    }

    public static void setIdentityId(MetaData metaData, Object entity, Long identity) {
		ClassInfo classInfo = metaData.classInfo(entity);
		Field identityField = classInfo.getField(classInfo.identityField());
		FieldInfo.write(identityField, entity, identity);
    }

    // FIXME replace with optional
    public static NodeId nodeId(Object node, MetaData metaData) {
        ClassInfo classInfo = metaData.classInfo(node.getClass().getName());
        FieldInfo primaryIndexField = classInfo.primaryIndexField();
        if (primaryIndexField != null) {
            Object idValue = primaryIndexField.readProperty(node);
            // ...
            return null;
        } else {
            Long entityId = getEntityId(metaData, node);
            if (entityId != null) {
                return Neo4jNodeId.of(entityId);
            } else {
                return null;
            }
        }
    }
}
