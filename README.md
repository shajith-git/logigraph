\# LogiGraph — Shipment Intelligence Platform



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



`Disruption → Location → TrackingEvent → Shipment`



This makes multi-hop relationship queries natural and keeps the data model close to the real-world logistics domain.



\---



\# Data Model



```text

&#x20;                        ┌─────────────┐

&#x20;                        │   Customer      │

&#x20;                        └──────┬──────┘

&#x20;                                 │

&#x20;                               OWNS

&#x20;                                 │

&#x20;                                 ▼

┌─────────────┐          ┌─────────────┐          ┌─────────────┐

│   Location      │◄──────│   Shipment       │──────►│   Location       │

│   Origin        │          └──────┬──────┘          │ Destination      │

└─────────────┘            │                           └─────────────┘

&#x20;                               │

&#x20;                          HAS\_EVENT

&#x20;                               │

&#x20;                               ▼

&#x20;                      ┌─────────────────┐

&#x20;                      │   TrackingEvent       │

&#x20;                      └────────┬────────┘

&#x20;                               │

&#x20;                          OCCURRED\_AT

&#x20;                               │

&#x20;                               ▼

&#x20;                        ┌─────────────┐

&#x20;                        │  Location       │

&#x20;                        └──────┬──────┘

&#x20;                               │

&#x20;                            AFFECTS

&#x20;                               │

&#x20;                               ▼

&#x20;                      ┌────────────────┐

&#x20;                      │  Disruption         │

&#x20;                      └────────────────┘



Shipment ──USES──> Vehicle ──DRIVEN\_BY──> Driver

