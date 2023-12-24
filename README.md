# jdbmig
Versions: 
- 1.0.0 First release

I have looked around to find an utility jdbc based, to download and upload data to different databases, but didn't found anything. For this reason i decided to build my own.

JDBMig is an agnostic java application that uses jdbc to export and import data into different databases. 

The data structure of the destination database must match the definition of the source. Some little adjustment can be done on the resulting jsons files generated.

All the process is done by jdbc drivers. 
<br>You can download the drivers i used from here https://github.com/msbragi/jdbmig-driverpack

<pre>
Usage: java -jar JDBMig-1.0.0 --import|--export --config config_file_path [--dataDir EXISTING_PATH]
--config|-c file:       configuration file see config/config.json as example
--import|-i:            import data from json file
--export|-x:            export data to json file
--dataDir|-d path:      the directory where store json files or read from
                        (optional this option override the one defined in [config].json)
Example: java -jar .\JDBMig-1.0.0.jar -x -c .\config\sqlite.json -d .\test
</pre>
View dist/config/config.json.EXAMPLE for help on configuration file

This is the config file for the SQLite included example: 
<pre>
{
	"dataDir":  "data/",
	"fieldToLowerCase":  false,
	"prettyPrint":  true,
	"tables":  [ "customers", "employees", "invoices", "invoice_items", "artists", "albums", "media_types", "genres", "tracks", "playlist_track"  ],
	"connection": {
		"type": "sqlite",
		"initString": null,
		"jdbcUrl": "jdbc:sqlite:./sqlite/test.sqlite"
	},
	"drivers": [
		{"name": "sqlite", "className": "org.sqlite.JDBC", "jarFile": "lib/sqlite-jdbc-3.32.3.8.jar"}
	]
}

For each table in tables array, jdbmig create a json file. 
Example json file for exported artists table (artists.json):
{
  "name" : "artists",
  "fields" : [ {
    "name" : "ArtistId",
    "type" : 4,
    "renameTo" : null,
    "dflt" : null
  }, {
    "name" : "Name",
    "type" : 12,
    "renameTo" : null,
    "dflt" : null
  } ],
  "data" : [ {
    "ArtistId" : 1,
    "Name" : "AC/DC"
  }, {
    "ArtistId" : 2,
    "Name" : "Accept"
  }, {
    "ArtistId" : 3,
    "Name" : "Aerosmith"
  }, {
    "ArtistId" : 4,
    "Name" : "Alanis Morissette"
  }]
}
Before import you can adjust something using the fields array.
The <b>type</b> defines the sql.data.type (view Java.sql.type.txt for reference)
The <b>renameTo</b> property permits you to change the name of destination field in database.
The <b>dflt</b> property permits you to define a value to put if source value of data.field is null.
If you change the value of <b>name</b> property the relative field in data section will not be imported.
</pre>


The java folder contains the source project 

The dist folder contains all you need to run the example, the jdbc driver for sqlite is included. 

If you need other drivers download from respective vendors or download my dist packages from https://github.com/msbragi/jdbmig-driverpack
