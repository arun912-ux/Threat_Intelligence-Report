# Threat_Intelligence-Report

### How to Run

1. Build the project with Maven. War file is created in 
    <p> <code>/out/arifact/"artifactname"/"artifactname".war</code> </p>
   <br/>
2. Copy the war file into Tomcat webapps folder
   <p><code> %CATALINE_HOME%/webapps/ </code></p>
   <br/>
3. Start the Tomcat server. Startup script is in `bin/` directory
    - `startup.bat` : for Windows
    - `startup.sh`  : for Linux
      
    <br/>
4. Goto `localhost:8080/"artifactname"/`
   
   <br/>
5. End Points : 
    - `api/url`  : to fetch urls
    - `api/domain`  : to fetch domains
    - `api/ip`  : to fetch ips


### Homepage

- IP : to fetch IPs
- DOMAIN : to fetch Domains
- URL : to fetch URL
- Search box : to search specific content

<img src="homepage.png" alt="homepage.png" />

