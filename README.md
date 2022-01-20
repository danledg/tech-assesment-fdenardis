# server

## build

to compile and run the tests

    ./mvnw clean verify

to create the jar file

    ./mvnw clean package

## run it

from the server project root

    java -jar target/techtest-0.0.1-SNAPSHOT.jar

### H2 database

The application server is configured to use an in memory h2 database (to store the programmes and the schedule)
If you want to allow h2 to save to your filesystem you'll need to set the Spring datasource url

    java -jar -Dspring.datasource.url=h2:file:/path/to/your/data target/techtest-0.0.1-SNAPSHOT.jar

A console for H2 is available at

    http://localhost:8080/h2-console

Login parameters for the h2 console:
jdbc url: **jdbc:h2:mem:schedule**  unless you have changed it when launching the app server.
user name **sa** password **secret**

A small set of data is fed into the tables when they are initialised (see src/main/resources/data.sql)

### Swagger page

The application server will start on the default 8080 port. If you want to change it set the server.port variable on the command line

    java -jar -Dserver.port=8180 target/techtest-0.0.1-SNAPSHOT.jar

The swagger page is available at

    http://localhost:8080/swagger-ui.html

# client

My very basic client is in the client-vue folder

To run it, from within the client-vue folder issue the following command:

    npm run dev

This will serve the application from a node server listening on the port 3000. 

    http://localhost:3000/

Node is also proxying the API requests to the server. If you are running the server on a port different from 8080 
you'll have to edit the proxy configuration in nuxt.config.js

# design

* to calculate the cost of the entire schedule: group the schedule by programme first, calculate the cost for each programme and then reassemble the schedule

# assumptions

things that I should have asked but since today is Saturday I will make assumptions on the answers
* for the discount rules (repeat within 3 days, repeat within 7 days) I will consider the repetition to be within the specified interval based on the start datetime relative to the start datetime of the first playout (as opposite to considering the repetition end time relative to the start time of the first playout)
