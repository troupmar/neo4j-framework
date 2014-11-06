package com.graphaware.runtime.policy;

import com.graphaware.common.policy.fluent.IncludeRelationships;
import com.graphaware.runtime.policy.all.IncludeAllBusinessRelationships;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import static com.graphaware.common.description.predicate.Predicates.equalTo;
import static com.graphaware.common.description.predicate.Predicates.undefined;
import static com.graphaware.common.policy.composite.CompositeRelationshipInclusionPolicy.of;
import static com.graphaware.common.util.DatabaseUtils.registerShutdownHook;
import static com.graphaware.runtime.config.RuntimeConfiguration.GA_PREFIX;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.neo4j.graphdb.Direction.*;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

/**
 * Test for {@link com.graphaware.common.policy.composite.CompositeRelationshipInclusionPolicy} with {@link com.graphaware.runtime.policy.all.IncludeAllBusinessNodes} and a programmatically configured {@link com.graphaware.common.policy.fluent.IncludeRelationships}
 */
public class IncludeBusinessRelationshipsTest {

    @Test
    public void shouldIncludeCorrectRelationships() {
        GraphDatabaseService database = new TestGraphDatabaseFactory().newImpermanentDatabase();
        registerShutdownHook(database);

        try (Transaction tx = database.beginTx()) {
            Node n1 = database.createNode();
            Node n2 = database.createNode();
            Relationship r = n1.createRelationshipTo(n2, withName("TEST"));
            r.setProperty("test", "test");

            Relationship internal = n1.createRelationshipTo(n2, withName(GA_PREFIX + "TEST"));
            internal.setProperty("test", "test");

            assertTrue(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all()).include(r));
            assertFalse(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all()).include(internal));
            assertTrue(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(OUTGOING)).include(r, n1));
            assertFalse(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(OUTGOING)).include(internal, n1));
            assertFalse(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(INCOMING)).include(r, n1));
            assertFalse(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(OUTGOING)).include(r, n2));
            assertTrue(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(INCOMING)).include(r, n2));
            assertFalse(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(INCOMING)).include(internal, n2));

            assertTrue(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(BOTH, withName("TEST"))).include(r));
            assertTrue(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(BOTH, "TEST", "TEST2")).include(r));
            assertFalse(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(BOTH, withName("TEST2"), withName("TEST3"))).include(r));

            assertTrue(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(BOTH, withName("TEST"))).include(r));
            assertTrue(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(OUTGOING, withName("TEST"), withName("TEST2"))).include(r, n1));
            assertFalse(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(OUTGOING, withName("TEST"), withName("TEST2"))).include(r, n2));
            assertFalse(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(BOTH, withName("TEST2"), withName("TEST3"))).include(r, n1));
            assertFalse(of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships.all().with(BOTH, withName("TEST2"), withName("TEST3"))).include(r, n2));

            assertTrue(
                    of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships
                            .all()
                            .with(BOTH, withName("TEST"))
                            .with("test", equalTo("test"))).include(r));

            assertFalse(
                    of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships
                            .all()
                            .with("test", equalTo("test"))).include(internal));

            assertFalse(
                    of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships
                            .all()
                            .with(BOTH, withName("TEST"))
                            .with("test", equalTo("test2"))).include(r));

            assertFalse(
                    of(IncludeAllBusinessRelationships.getInstance(), IncludeRelationships
                            .all()
                            .with(BOTH, withName("TEST"))
                            .with("test", undefined())).include(r));

            tx.success();
        }
    }
}
