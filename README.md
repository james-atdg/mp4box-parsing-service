# MP4 Box Parsing service
The service returns MP4 box metadata from a user supplied MP4 file.

### Description
The MP4 Box Parsing service retrieves an MP4 file from a user supplied URL, extracts the MP4 boxes from it, creates a nested list of MP4Box objects and returns this to the caller.

The service utilizes a buffered stream approach for reading and processing the MP4 file ensuring that the file is processed as received and is not stored in memory or on disk.  This approach will accommodate parsing small and large files alike.


### Installation
Prerequisites: Java 17, Maven 3.9.1 (tested against these versions)

To build the project execute <b>build.sh</b> from within the project directory.

To run the application after building execute <b>run.sh</b> from within the project directory.

The application may be accessed at: 
* parse request end-point: POST [http://localhost:8082/api/mp4/parse](http://localhost:8082/api/castlabs/parse)

* rest api documentation: [http://localhost:8082/swagger-ui/index.html?displayRequestDuration=true&operationsSorter=method&configUrl=/api-docs/swagger-config#/](http://localhost:8082/swagger-ui/index.html?displayRequestDuration=true&operationsSorter=method&configUrl=/api-docs/swagger-config#/)

<u>Example Request</u>
 
curl -X POST http://localhost:8082/api/mp4/parse -H "Content-Type: application/json" -d '{"url": "https://demo.castlabs.com/tmp/text0.mp4"}'

 
<i>See the rest api doc for request and response definitions</i>

