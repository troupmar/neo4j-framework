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

package com.graphaware.test.util;

import com.graphaware.test.integration.WrappingServerIntegrationTest;
import com.graphaware.test.unit.GraphUnit;
import org.junit.Test;

/**
 * Test for {@link com.graphaware.test.util.TestUtils}.
 */
public class TestHttpClientTest extends WrappingServerIntegrationTest {

    @Test
    public void shouldBeAbleToExecuteCypherStatement() throws InterruptedException {
        httpClient.executeCypher(baseNeoUrl(), "CREATE (:Person {name:'Michal'})");

        Thread.sleep(100);

        GraphUnit.assertSameGraph(getDatabase(), "CREATE (:Person {name:'Michal'})");
    }

    @Test
    public void shouldBeAbleToExecuteCypherStatements() {
        httpClient.executeCypher(baseNeoUrl(), "CREATE (:Person {name:'Michal'})", "CREATE (:Person {name:'Vince'})");

        GraphUnit.assertSameGraph(getDatabase(), "CREATE (:Person {name:'Michal'}), (:Person {name:'Vince'})");
    }
}
