# SleepVote Plugin

A plugin to allow sleeping with a subset of players in a CraftBukkit Posedion 1060 Server.

There are two config options for setting sleep amounts the default config will be generated when running with the plugin
- Percentage
    - Configures the required amount of sleeping players to be a percentage of the total players
- Number
    - Configures the required amount of sleeping players to be a number. This supercedes percentage

The plugin also has configurable weather options
 - Rain Percentage chance
  - A random number between 0-1 will be generated if the number is <= the rain chance it will rain or the weather will be set to clear
 - Thunder percentage chance
    - If raining a random number between 0-1 will be generated if the number is <= the thunder chance it will thunder
 - Max Rain duration
    - Max length of time in game days that the rain will last for before the game recalculates weather
 - Max clear weather duration
    - Max length of time in game days that the clear weather will last for before the game recalculates weather


 [Project Poseidon Server Repo](https://github.com/retromcorg/Project-Poseidon)

    
## Build

```mvn clean package```
