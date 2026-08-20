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



\# Data Model



```text

&#x20;                        â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”

&#x20;                        â”‚   Customer      â”‚

&#x20;                        â””â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”˜

&#x20;                                 â”‚

&#x20;                               OWNS

&#x20;                                 â”‚

&#x20;                                 â–¼

â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”          â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”          â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”

â”‚   Location      â”‚â—„â”€â”€â”€â”€â”€â”€â”‚   Shipment       â”‚â”€â”€â”€â”€â”€â”€â–ºâ”‚   Location       â”‚

â”‚   Origin        â”‚          â””â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”˜          â”‚ Destination      â”‚

â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜            â”‚                           â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜

&#x20;                               â”‚

&#x20;                          HAS\_EVENT

&#x20;                               â”‚

&#x20;                               â–¼

&#x20;                      â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”

&#x20;                      â”‚   TrackingEvent       â”‚

&#x20;                      â””â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”˜

&#x20;                               â”‚

&#x20;                          OCCURRED\_AT

&#x20;                               â”‚

&#x20;                               â–¼

&#x20;                        â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”

&#x20;                        â”‚  Location       â”‚

&#x20;                        â””â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”˜

&#x20;                               â”‚

&#x20;                            AFFECTS

&#x20;                               â”‚

&#x20;                               â–¼

&#x20;                      â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”

&#x20;                      â”‚  Disruption         â”‚

&#x20;                      â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜



Shipment â”€â”€USESâ”€â”€> Vehicle â”€â”€DRIVEN\_BYâ”€â”€> Driver


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
                    |
                    v
                 Shipment
              /     |      \
             /      |       \
ORIGINATES  /    HAS_EVENT   \ ASSIGNED_TO
           v         |         v
       Location      v       Vehicle
                     |          |
               OCCURRED_AT   DRIVEN_BY
                     |          |
                     v          v
                  Location    Driver
                     |
                   AFFECTS
                     |
                     v
                 Disruption

Shipment ──DESTINED_FOR──> Location



