# LogiGraph â€” Shipment Intelligence Platform



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


