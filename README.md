# LogiGraph Shipment Intelligence Platform



LogiGraph is a graph-powered shipment management application built for the Wexa AI CognoDB take-home assignment.



It allows users to explore shipments, locations, tracking events, vehicles, drivers, customers, and operational disruptions through relationships stored in CognoDB.



\## Tech Stack



\### Backend

\- Java 21

\- Spring Boot

\- REST APIs

\- Neo4j Java Driver

\- CognoDB

\- Maven



\### Frontend

\- React

\- Vite

\- JavaScript

\- CSS



\### Database

\- CognoDB

\- openCypher

\- Bolt protocol



\---



\# Why a Graph Database?



Shipment operations are naturally relationship-heavy.



A shipment is connected to:



\- an origin location

\- a destination location

\- a customer

\- tracking events

\- locations where events occurred

\- a vehicle

\- a driver

\- operational disruptions



In a relational database, answering questions that cross several of these relationships would require multiple JOINs across different tables.



With a graph database, these connections are represented directly as relationships.



For example:



> Which shipments are affected by a disruption at a particular location?



The graph can traverse:



`Disruption â†’ Location â†’ TrackingEvent â†’ Shipment`



This makes multi-hop relationship queries natural and keeps the data model close to the real-world logistics domain.



\---






## Data Model

```mermaid
graph TD
    C[Customer] -->|RECEIVES| S[Shipment]

    S -->|ORIGINATES_FROM| L1[Location]
    S -->|DESTINED_FOR| L2[Location]

    S -->|HAS_EVENT| E[TrackingEvent]
    E -->|OCCURRED_AT| L3[Location]

    L3 -->|AFFECTS| D[Disruption]

    S -->|ASSIGNED_TO| V[Vehicle]
    V -->|DRIVEN_BY| DR[Driver]
```



Customer
   |
 RECEIVES
   ↓
Shipment
 ├── ORIGINATES_FROM ──→ Location
 ├── DESTINED_FOR ─────→ Location
 ├── HAS_EVENT ────────→ TrackingEvent
 │                          |
 │                       OCCURRED_AT
 │                          ↓
 │                       Location
 │                          |
 │                        AFFECTS
 │                          ↓
 │                      Disruption
 │
 └── ASSIGNED_TO ─────→ Vehicle
                            |
                         DRIVEN_BY
                            ↓
                          Driver



## Why a Graph Database?

Logistics data is highly relationship-driven.

A shipment is connected to a customer, origin, destination, tracking events,
vehicles, drivers, and potentially operational disruptions. These connections
are not isolated records; they form a network of related entities.

A relational database could represent the same information using multiple
tables and foreign keys. However, questions involving several relationships
would require multiple JOIN operations and increasingly complex queries.

CognoDB is a better fit for LogiGraph because relationships are first-class
elements of the data model.

For example, LogiGraph can answer questions such as:

- Which shipments are affected by a particular disruption?
- Where was a shipment when a tracking event occurred?
- Which vehicle and driver are responsible for a shipment?
- Which customers own shipments currently affected by an operational problem?
- What locations, events, and disruptions are connected to a shipment?

These questions can be represented naturally as graph traversals.

For example:

Shipment
→ HAS_EVENT
→ TrackingEvent
→ OCCURRED_AT
→ Location

and:

Shipment
→ USES
→ Vehicle
→ DRIVEN_BY
→ Driver

This makes relationship-heavy logistics queries easier to model and reason
about compared with a relational schema built primarily around JOIN operations.

## Main Cypher Queries

LogiGraph uses parameterised Cypher queries through the official Neo4j Java
driver. Query parameters are passed separately from the Cypher statements;
no user input is concatenated into query strings.

1. Find All Shipments

```cypher
MATCH (s:Shipment)
RETURN s
ORDER BY s.id
//This query retrieves all shipment nodes available in the graph.
2. Find a Shipment by ID
MATCH (s:Shipment {id: $id})
RETURN s
//The shipment ID is supplied as the $id parameter.
3. Find Shipment Tracking Events
MATCH (s:Shipment {id: $shipmentId})
      -[:HAS_EVENT]->(e:TrackingEvent)
RETURN e
ORDER BY e.timestamp
//This follows the relationship between a shipment and its tracking events.
4. Multi-Hop Shipment Location Traversal
MATCH (s:Shipment {id: $shipmentId})
      -[:HAS_EVENT]->(e:TrackingEvent)
      -[:OCCURRED_AT]->(l:Location)
RETURN e, l
ORDER BY e.timestamp
/*This is a multi-hop graph traversal:

Shipment
→ HAS_EVENT
→ TrackingEvent
→ OCCURRED_AT
→ Location

The query can therefore determine where shipment activity occurred without
requiring separate application-level lookups.*/
5. Find Shipments Affected by a Disruption
MATCH (d:Disruption {id: $disruptionId})
      <-[:AFFECTS]-(l:Location)
      <-[:ORIGIN|DESTINATION]-(s:Shipment)
RETURN DISTINCT s
/*
This query traverses:

Disruption
→ affected Location
→ Shipment

It demonstrates how operational disruptions can be connected to shipments
through shared locations.
*/
6. Shipment → Vehicle → Driver
MATCH (s:Shipment {id: $shipmentId})
      -[:USES]->(v:Vehicle)
      -[:DRIVEN_BY]->(d:Driver)
RETURN s, v, d
/*
This multi-hop traversal identifies the vehicle and driver associated with a
shipment.*/
7. Find the Customer and Route for a Shipment
MATCH (c:Customer)
      -[:OWNS]->(s:Shipment {id: $shipmentId})
      -[:ORIGIN]->(origin:Location)
MATCH (s)-[:DESTINATION]->(destination:Location)
RETURN c, s, origin, destination
/*
This query retrieves the customer, shipment, origin and destination as one
connected graph result.*/

