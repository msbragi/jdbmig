# jdbmig
Versions: 
- 1.0.0 First release

I have looked around to find an utility jdbc based, to download and upload data to different databases, but didn't found anything. For this reason i decided to build my own.

JDBMig is an agnostic java application that uses jdbc to export and import data into different databases. 

The data structure of the destination database must match the definition of the source. Some little adjustment can be done on the resulting jsons files generated.

All the process is done by jdbc drivers. 
<br>You can download the drivers i used from here https://github.com/msbragi/jdbmig-driverpack

<pre>
JDBMig 1.0.0 - (c) Marco Sbragi (m.sbragi@nospace.net)
Usage: java -jar JDBMig.jar --import|--export --config config_file_path [--dataDir YOUR_EXISTING_PATH]
--config|-c file:       configuration file see config/config.json as example
--import|-i:            import data from json file
--export|-x:            export data to json file
--useConn|-u conn:      the connection to use (if not have standard [connection] defined or more than one)
                        (optional this option override "connection"  or "useConn" defined in [config].json)
--dataDir|-d path:      the directory where store json files or read from
                        (optional this option override the one defined in config.json)

Example: java -jar .\JDBMig.jar -x -c .\config\sqlite.json -d .\test
</pre>
View dist/config/config.json.EXAMPLE for help on configuration file

This is the config file for the SQLite included example: 
<pre>
{
	"dataDir":  "data/",
	"fieldToLowerCase":  false,
	"prettyPrint":  true,
	"tables":  [ "TEST", "artists", "albums", "media_types", "genres", "tracks", "playlist_track"  ],
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
Example TEST.json
{
"name": "TEST",
"fields": [
	{"name": "ID",		"type": 2,	"renameTo": null,	"dflt": null},
	{"name": "NAME",	"type": 12,	"renameTo": null,	"dflt": null},
	{"name": "DESCRIPTION",	"type": 12,	"renameTo": null,	"dflt": null},
	{"name": "SHORT",	"type": 12,	"renameTo": null,	"dflt": null},
	{"name": "CODE",	"type": 12,	"renameTo": null,	"dflt": null},
	{"name": "VERSION",	"type": 2,	"renameTo": null,	"dflt": null},
	{"name": "CREATED",	"type": 93,	"renameTo": null,	"dflt": null},
	{"name": "FDATE",	"type": 93,	"renameTo": null,	"dflt": null},
	{"name": "FFLOAT",	"type": 2,	"renameTo": null,	"dflt": null}
],
"data": [{object}, {object}, {object}]
}
Before import you can adjust something using the fields array.
The <b>type</b> defines the sql.data.type (view Java.sql.type.txt for reference)
The <b>renameTo</b> property permits you to change the name of destination field in database.
The <b>dflt</b> property permits you to define a value to put if source value of data.field is null.

ATTENTION: if you change a name field in fields array an error occurs when importing data.
If you don't want to import a field data, remove the field from fields array.
</pre>


The java folder contains the source project 

The dist folder contains all you need to run the example, the jdbc driver for sqlite is included. 

If you need other drivers download from respective vendors or download my dist packages from https://github.com/msbragi/jdbmig-driverpack
