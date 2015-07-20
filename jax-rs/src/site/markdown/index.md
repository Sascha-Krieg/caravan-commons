## About Commons JAX-RS Connector

Provides transparent JAX-RS integration for OSGi services based on the Jersey JAX-RS implementation.


### Maven Dependency

```xml
<dependency>
  <groupId>io.wcm.caravan</groupId>
  <artifactId>io.wcm.caravan.commons.jax-rs</artifactId>
  <version>0.5.0</version>
</dependency>
```

### Documentation

* [Usage][usage]
* [API documentation][apidocs]
* [Changelog][changelog]


[usage]: usage.html
[apidocs]: apidocs/
[changelog]: changes-report.html


### Overview

Provides a [JAX-RS](https://jsr311.java.net/) integration based on [Jersey](https://jersey.java.net/) with OSGi. The implementation uses an OSGI Extender pattern.

If a JAX-RS integration is configured for a bundle a JAX-RS application is created automatically and registered in a Servlet container to the Apache Felix HTTP whiteboard service. This is done transparently to the bundle, only the application path has to be configured and each JAX-RS components has to be registered as an OSGi service using a special marker interface.

This connector only support publishing REST interfaces via JAX-RS. Consuming them via the JAX-RS client interface is not supported.

Dependencies:

* Jersey has to be deployed as additional bundle, wcm.io provides a wrapped version: http://repo1.maven.org/maven2/io/wcm/osgi/wrapper/io.wcm.osgi.wrapper.jersey/


### Alternatives

* [OSGi JAX-RS Connector](https://github.com/hstaudacher/osgi-jax-rs-connector) - basically similar concept as the Caravan JAX-RS integrations, but creates always one single big Jersey Application for all bundles which breaks isolation between the bundles e.g. concerning @Provider JAX-RS components. Besides this the implementation is very stable and well maintained, and does not only support publishing but also consuming JAX-RS services within OSGi. This comes at a price of a quite complex implementation and a lot of dependencies.
* [JAX-RS Extender Bundle for OSGi](https://github.com/njbartlett/jaxrs-osgi-extender) - quite old implementation based on the OSGi extender pattern. Builds one JAX-RS Application per module, but the JAX-RS components are not OSGi components, so OSGI dependency injection is not possible.
