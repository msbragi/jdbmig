Usage: jdbcUtil --import|--export --config config_file_path [--dataDir YOUR_EXISTING_PATH]
--config|-c file:       configuration file see config/config.json as example
--import|-i:            import data from json file
--export|-x:            export data to json file
--dataDir|-d path:      the directory where store json files or read from
                        (optional this option override the one defined in config.json)
Example: java -jar .\jdbcUtil.jar -x -c .\config\config.json -d .\test

View config/config.json.EXAMPLE for help on configuration file

Versions: 
- 1.0.0.0 First release
