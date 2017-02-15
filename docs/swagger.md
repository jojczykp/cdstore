CD Store - Swagger solutions
============================

* [What is Swagger?](#what-is-swagger)
* [How it is used here?](#how-it-is-used-here)
* [Swagger and Maven](#swagger-and-maven)


What is Swagger?
----------------

Swagger is a tool that generates interactive documentation of API, basing on
annotations.

More info [here](http://swagger.io//).


How it is used here?
--------------------

Swagger is attached to application as a servlet, available at `/swagger`.

All resources classes are annotated with:
- `@Api`
- `@ApiOperation`
- `@ApiParam`
- `@ApiResponse`
- `@ApiResponses`


Swagger and Maven
-----------------

Swagger is well integrated with dropwizard thanks to great project
[dropwizard-swagger](https://github.com/federecio/dropwizard-swagger).

To use dropwizard-swagger:

1. Add maven dependency:

```xml
<dependency>
	<groupId>com.smoketurner</groupId>
	<artifactId>dropwizard-swagger</artifactId>
	<version>1.0.5-4</version>
</dependency>
```

2. Add config section to point to package that will be scanned for swagger annotations:

```yml
swagger:
    resourcePackage: pl.jojczykp.cdstore
```

3. Create corresponding field in configuration class:

```java
@JsonProperty
@Getter
private SwaggerBundleConfiguration swagger;
```

4. And finally, load swagger endpoint as a bundle:

```java

@Override
public void initialize(Bootstrap<CdStoreConfiguration> bootstrap) {
	bootstrap.addBundle(new SwaggerBundle<CdStoreConfiguration>() {
		@Override
		protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(CdStoreConfiguration configuration) {
			return configuration.getSwagger();
		}
	});
}
```
