# WorldBuilder

## Description
Places blocks and schematics en masse at certain locations according to a datafile.

## Setup
Get the plugin jar by compiling with maven, type ```mvn install``` in repo basedir. Place it in your 
```/plugins``` directory.

You must specify a datafile in ```config.yml```. This file will be generated the first time the plugin 
is loaded. A template datafile will also be generated. In this file you can put the locations to spawn a 
schematic or block, which are referenced by name. To link the name of a building to a block or schematic,
edit the generated ```config.yml``` template.

## How to use
* Type ```/placebuildings``` in game to initialize block and schematic spawning
* Type ```/availablebuildings``` to see the buildings and blocks that can be placed
* Type ```/removebuildings``` to remove buildings that are placed during this session
* Type ```/testschematic [index no]``` to place a single schematic in the direction a player is looking at, 
where ```[index no]``` is the number associated with a schematic. Type  ```/availablebuildings``` for the 
available index numbers belonging to a single schematic.