/*
 * Copyright (c) 2015 GraphAware
 *
 * This file is part of GraphAware.
 *
 * GraphAware is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.example;

import com.graphaware.test.integration.GraphAwareApiTest;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;

import static org.junit.Assert.assertEquals;

/**
 * {@link GraphAwareApiTest} for {@link NodeCountApi}.
 */
public class NodeCountApiTest extends GraphAwareApiTest {

    @Test
    public void emptyDatabaseShouldHaveZeroNodes() {
        assertEquals("0", httpClient.get(baseUrl() + "/count", HttpStatus.SC_OK));
    }

    @Test
    public void whenTwoNodesAreCreatedThenDatabaseShouldHaveTwoNodes() {
        try (Transaction tx = getDatabase().beginTx()) {
            getDatabase().createNode();
            getDatabase().createNode();

            tx.success();
        }

        assertEquals("2", httpClient.get(baseUrl() + "/count", HttpStatus.SC_OK));
    }
}
