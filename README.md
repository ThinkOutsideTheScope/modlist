# Modlist

Adds a server-side /mods command to view all mods installed.

# Usage

- Query a mod with ```/mods query <format> <mod_name>```. Format can contain normal text, and curly brackets to be replaced with info about the mod (see Query Format). The mod name can be the name of a mod (case sensitive), or an asterisk (*) to get info about all mods.
- Operators with an admin or higher permission level can hide a property about a mod using ```/mods mask <field> <mod_name>```. The mod name can be a mod or an asterisk hide a property for all mods. Properties cannot be hidden from admins, and do not save across restarts. See All Properties for the field parameter.
- Operators with an admin or higher permission level can unhide a property about a mod using ```/mods unmask <field> <mod_name>```. It has the inverse functionality of ```/mods mask```.
- Operators with an admin or higher permission level can query the hidden state of a property using ```/mods mask_query <field> <mod_name>```. These are the same parameter types as ```/mods mask``` and ```/mods unmask```.

# All Properties

Here are all of the current property types:
- ID
- license
- homepage
- authors
- type
- provides
- version
- environment
- dependencies
- description
- contributors
- source
For ```/mods query```, there is a special property type named homepageOrElseSource, which becomes the homepage link if it exists, otherwise the source link.

# Query Format

The query format used in ```/mods query``` is used to retrive information about a mod. In a query format, normal text is left as-is, and a property name (see All Properties) surrounded in curly brackets ({}) is replaced with the value of that property. To escape a bracketed field (make it output the literal {PROPERTY} instead of replacing it), simply prefix the opening curly bracket with a backward slash (\). When typing a query format into the command line, always wrap it in double quotes ("").

## Example query formats:

"{name} version {version} is licensed as {license} and has a homepage of {homepage}."
