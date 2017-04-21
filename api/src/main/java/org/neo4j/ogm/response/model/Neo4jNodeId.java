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

package org.neo4j.ogm.response.model;

public class Neo4jNodeId implements NodeId {

	private Long nativeId;

	public Long getNativeId() {
		return nativeId;
	}

	public void setNativeId(Long nativeId) {
		this.nativeId = nativeId;
	}

	public Neo4jNodeId(Long nativeId) {
		this.nativeId = nativeId;
	}

	@Override
	public Object getValue() {
		return nativeId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Neo4jNodeId that = (Neo4jNodeId) o;

		return nativeId != null ? nativeId.equals(that.nativeId) : that.nativeId == null;
	}

	@Override
	public int hashCode() {
		return nativeId != null ? nativeId.hashCode() : 0;
	}

	public static NodeId of(Long id) {
		return new Neo4jNodeId(id);
	}

	@Override
	public String toString() {
		return "Neo4jNodeId{" + nativeId + '}';
	}
}
