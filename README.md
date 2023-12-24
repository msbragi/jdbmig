# jdbmig
I have looked around to find an utility jdbc based, to download and upload data to different databases, but didn't found anything. For this reason i decided to build my own.

JDBMig is an agnostic java application that uses jdbc to export and import data into different databases.

All the process is done by jdbc drivers. 

The configuration file 
<pre>
Usage: java -jar JDBMig-1.0.0 --import|--export --config config_file_path [--dataDir EXISTING_PATH]
--config|-c file:       configuration file see config/config.json as example
--import|-i:            import data from json file
--export|-x:            export data to json file
--dataDir|-d path:      the directory where store json files or read from
                        (optional this option override the one defined in [config].json)
Example: java -jar .\JDBMig-1.0.0.jar -x -c .\config\sqlite.json -d .\test

View dist/config/config.json.EXAMPLE for help on configuration file

Versions: 
- 1.0.0 First release
</pre>
