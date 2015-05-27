/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2015 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.caravan.commons.jaxrs;

/**
 * Marker interface that have to implement all JAX-RS services and providers.
 */
//CHECKSTYLE:OFF
public interface JaxRsComponent {
  //CHECKSTYLE:ON

  /**
   * OSGi property that marks a JaxRS component that is registered globally to all JAX-RS applications.
   * It has to be implemented as OSGi component factory to ensure a new instance is provided for each context.
   */
  String GLOBAL_COMPONENT_FACTORY = "caravan.jaxrs.global.factory";

}
