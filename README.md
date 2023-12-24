# jdbmig
Versions: 
- 1.0.0 First release

I have looked around to find an utility jdbc based, to download and upload data to different databases, but didn't found anything. For this reason i decided to build my own.

JDBMig is an agnostic java application that uses jdbc to export and import data into different databases.

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
</pre>
The java folder contains the source project 

The dist folder contains all you need to run the example, the jdbc driver for sqlite is included. 

If you need other drivers download from respective vendors or download my dist packages from https://github.com/msbragi/jdbmig-driverpack
