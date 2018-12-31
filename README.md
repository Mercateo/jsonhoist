# Library for chained JSON Transformation (for instance Up-/Down-casting) according to a ruleset store in a repository. 

[![CircleCI](https://circleci.com/gh/Mercateo/jsonhoist/tree/master.svg?style=svg)](https://circleci.com/gh/Mercateo/jsonhoist/tree/master)
[![codecov](https://codecov.io/gh/Mercateo/jsonhoist/branch/master/graph/badge.svg)](https://codecov.io/gh/Mercateo/jsonhoist)
[![CodeFactor](https://www.codefactor.io/repository/github/mercateo/jsonhoist/badge)](https://www.codefactor.io/repository/github/mercateo/jsonhoist)
[![MavenCentral](https://img.shields.io/maven-central/v/org.jsonhoist/jsonhoist.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.jsonhoist%22)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/739b29b2cdc14252a8f66aefbd71453b)](https://www.codacy.com/app/uwe/jsonhoist?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Mercateo/jsonhoist&amp;utm_campaign=Badge_Grade)
<a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img class="inline" src="https://img.shields.io/badge/license-ASL2-green.svg?style=flat">
</a>
## TL;DR;

* Upcast persisted, deprecated models to current
* Upcast & Downcast in EventSourcing
* Works with Jackson to deserialize into POJOs after transformation

## Background

When persisting JSON data from serialized objects, it is hard to change those objects in a non-compatible way: either you have to persist a second, updated model and fall back to the deprecated one, or you need to migrate the model, which (in some cases) is error-prone and might take days.

If you only deal with updatable, persisted JSON, migration might be an option. At some point you know all data has been updated, and you can rely on reading just the newest version and so remove all the compatibility code.

However, what if your JSON-Data is immutable and you have to be able to read it forever (for example in an EventSourcing scenario)? In this case you'll end up with a big pile of compatibility code over the lifetime of your application.

## Goals

JSONHoist now aims to provide means of transformation to the JSON document to be used before deserializing, so that you can get rid of all classes dealing with earlier versions of your POJO.
These transformations can be expressed in an implementation of the `JsonTransformation`-Interface, or by providing a little ECMAScript to do the transformation for you.

In the future, these scripts can be loaded at runtime, so that a producer of a message can change his message in an incompatible way, as long as he provides a transformation to an earlier version, without breaking consumers.

## Examples

A simple example could be found in the test class HoistObjectMapperTest 
