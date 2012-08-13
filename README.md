# sample-jee

A sample project for exemplifying and testing various JEE technologies - mainly on JBoss AS7.

## Contents

The project consists of serveral sub-projects or modules grouped by name as follows...

- sample-jee-client-*
- sample-jee-relay-*
- sample-jee-server-*
- sample-jee-standalone-*

The *client* modules contins various server-based client components (EJBs, Servlets etc) that calls other server components
in the *relay* or *server* moules.

The *relay* modules act as intermediaries, accepting calls from some client and sending it on to some component in the
*server* modules.

The *server* modules accept calls from some client.

The *standalone* modules normally act as Java standlone clients - in counterpart to the ones in the *client* modules
that normally are server-based components acting as clients.

### sample-jee-client-*

Five modules containing various server-client(s) to other server component(s) - in sample-jee-relay-* or
sample-jee-server-*.

#### EAR: sample-jee-client-ear

The EAR of these client modules.

#### EJB: sample-jee-client-ejb

EJBs.

#### EJB interfaces: sample-jee-client-ejb-remote

The remote interfaces of the EJBs in sample-jee-client-ejb.

#### Utilities: sample-jee-client-util

Various util classes utilised by the components in this client EAR.

#### WEB: sample-jee-client-web

Web material and components (e g HTML-pages and Servlets) acting as clients to components in sample-jee-relay-*
and/or sample-jee-server-*.

### sample-jee-relay-*

Three moudules (ear, ejb, ejb-remote)

#### EAR: sample-jee-relay-ear

The EAR of these "relay"-server modules.

#### EJB: sample-jee-relay-ejb

EJBs.

#### EJB remote interfaces: sample-jee-relay-ejb-remote

The remote interfaces of the EJBs in sample-jee-relay-ejb.

### sample-jee-server-*

Four modules:

#### EAR: sample-jee-server-ear

The EAR of these server modules.

#### EJB: sample-jee-server-ejb

EJBs.

#### EJB local interfaces: sample-jee-server-ejb-local

The local interfaces of the EJBs in sample-jee-server-ejb.

#### EJB remote interfaces: sample-jee-server-ejb-remote

The remote interfaces of the EJBs in sample-jee-server-ejb.

### sample-jee-standalone-*

One module: (expect a sample-jee-standalone-jms-client soon)

#### sample-jee-standalone-ejb-client