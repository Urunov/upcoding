---
title: Gson-Fire and Gson usage
categories:
 - tools
tags:
 - gson, json, tools, spring, spring boot, java
---

Gson Fire project contains very usefull additions to the Gson project, in order to keep your code organized and clean. Gson Fire is something that better to have, otherwise you have to implement JsonSerializer and JsonDeserializer interfaces and register TypeAdapter classes to run some additional behaviors in serialization and desirialization processes.

This article is not about usage of GsonFire, because best usage can be found from the official [Gson Fire](https://github.com/julman99/gson-fire) project. This article more about how to transfer from usage of GsonBuilder to GsonFireBuilder.

## Gson
Gson is a Java library that is used to convert Java Objects into their JSON representation, also used to convert a JSON string to an equivalent Java object. Gson can work with arbitrary Java objects including pre-existing objects that you do not have source-code of. For people who are familiar with [Jackson](https://github.com/FasterXML/jackson) project can think it as an alternative.

### Goals of Gson Project
- Provide simple toJson() and fromJson() methods to convert Java objects to JSON and vice-versa
- Allow pre-existing unmodifiable objects to be converted to and from JSON
- Extensive support of Java Generics
- Allow custom representations for objects
- Support arbitrarily complex objects (with deep inheritance hierarchies and extensive use of generic types)

## Gson Fire
[Gson Fire](https://github.com/julman99/gson-fire) project implements some useful features missing from the extremely useful Gson project. It is not an attempt to modify any behavior or existing classess of Gson. It is highly dependant on Gson project and full fills some painful points that are not supported by original Gson project. The main objective is to extend Gson using TypeAdapter and TypeAdapterFactory instances to support more features.


## Get GsonBuilder from GsonFireBuilder
Since GsonFireBuilder wrapps GsonBuilder, we may think we can just use all configurations of GsonBuilder at GsonFireBuilder, but it is not. For example, If we have some date-format configuration setting at GsonBuilder, then we cannot directly set it on GsonFireBuilder. We have to get GsonBuilder object from the GsonFireBuilder and then set the necessary configurations:

```java
private static GsonFireBuilder fireBuilder = new GsonFireBuilder()
    .registerPostProcessor(ExampleVO.class, new PostProcessor<ExampleVO>() {

        @Override
        public void postDeserialize(ExampleVO exampleVO, JsonElement src, Gson gson) {
            // do some deserialization actions
        }

        @Override
        public void postSerialize(JsonElement result, ExampleVO src, Gson gson) {
            // do some serialization actions
        }
    });

private static GsonBuilder gsonBuilder = fireBuilder.createGsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    .disableHtmlEscaping()
    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT)
    .registerTypeAdapter(SpecialObject.class, new SpecialObjectTypeAdepter());

private static Gson gson = gsonBuilder.create();
```

If we are satisfied with default configurations for Gson then we can create Gson from GsonFireBuilder. As you can see from above example the post-processing operations over ExampleVO object can be performed inside `postDeserialize` method.


## By Implementing Deserializer
It is possible to do post processing without using GsonFire, by implementing JsonDeserializer.

```java
public class ExampleVOTypeAdapter implements JsonDeserializer<ExampleVO> {
    @Override
    public ExampleVO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        Gson g = new Gson();
        ExampleVO exampleVO = g.fromJson(json, ExampleVO.class);
        String someField = "Some Operations result";
        exampleVO.setSomeField(someField);
        return exampleVO;
    }
}
```

After that this type adapter should be included into GsonBuilder object:

```java
private static Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    .disableHtmlEscaping()
    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT)
    .registerTypeAdapter(ContentVO.class, new ContentVOTypeAdapter())
    .create();
```

By some people standards this is not a clean code and may cause some confusions later. For example myself had a bug problem while try to go with TypeAdapter way. You may face forever loop problem of serializers, where two serailizers call each other recursively. It is very much advised to use GsonFire kind of post-processor based solutions.