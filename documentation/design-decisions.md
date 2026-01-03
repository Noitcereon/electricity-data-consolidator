# Design Decisions

This document briefly describes the design decisions that went into the making of this project.

- This project has two primary purposes:
  1. Making it easier to analyze some electrical data via an existing spreadsheet.
  2. Programming practice.
- A Console UI was used for a quick proof of concept, since I lack of Desktop UI knowledge.
  - Note: Electron was also considered later, but because everything is already written in Java, a rewrite to JavaScript is needed... + missing experience making Electron apps)
- Not making a web app, because it would require additional security if it is available online, which is a lot of unneeded complexity.
- Controller classes for managing workflow
- Manager classes for retrieving data
- `external` package is intended for any communication with external dependencies, such as 3rd party REST APIs.
- Using SL4J logging for flexibility (no vendor lock in)
- Use an orchestrator (me.noitcereon.external.api.orchestration) for all api calls for easier debugging and ease of modification of beforeCall and afterCall handling. I use my own custom implementation instead of a library mostly for learning reasons (but also to not add extra dependencies)
