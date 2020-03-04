# Commute Time Measurement

Tool that periodically runs a query to the Google Maps Distance Matrix API to check the 

# How to run

Configure home and work locations via the `application.yaml` file in the root folder.

Then build the application via

```
./gradlew bootJar
```

and start via

```
java -DAPI_KEY=ENTER_KEY_HERE -jar build/libs/commute-time-measurement-0.0.1-SNAPSHOT.jar
```