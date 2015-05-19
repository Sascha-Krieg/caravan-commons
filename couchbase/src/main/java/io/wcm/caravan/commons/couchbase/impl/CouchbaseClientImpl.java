/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
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
package io.wcm.caravan.commons.couchbase.impl;

import io.wcm.caravan.commons.couchbase.CouchbaseClient;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Cluster;

/**
 * Default implementation of {@link CouchbaseClient}.
 */
@Component(immediate = true, metatype = true,
label = "wcm.io Caravan Couchbase Client Provider",
description = "Provides access to a preconfigured couchbase client.",
configurationFactory = true, policy = ConfigurationPolicy.REQUIRE)
@Service(CouchbaseClient.class)
@Property(name = "webconsole.configurationFactory.nameHint",
value = "{" + CouchbaseClientImpl.CLIENT_ID_PROPERTY + "}: {" + CouchbaseClientImpl.CACHE_BUCKET_NAME_PROPERTY + "}")
public class CouchbaseClientImpl implements CouchbaseClient {

  @Property(label = "Client ID", description = "ID to uniquely identify the couchbase client if multiple are defined.")
  static final String CLIENT_ID_PROPERTY = CouchbaseClient.CLIENT_ID_PROPERTY;

  @Property(label = "Enabled", description = "Enable or disable couchbase caching.",
      boolValue = CouchbaseClientImpl.ENABLED_PROPERTY_DEFAULT)
  static final String ENABLED_PROPERTY = "enabled";
  private static final boolean ENABLED_PROPERTY_DEFAULT = true;

  @Property(label = "Couchbase Hosts", description = "Couchbase cluster host list.",
      cardinality = Integer.MAX_VALUE)
  static final String COUCHBASE_HOSTS_PROPERTY = "couchbaseHosts";

  @Property(label = "Bucket Name", description = "Couchbase bucket name")
  static final String CACHE_BUCKET_NAME_PROPERTY = "bucketName";

  @Property(label = "Bucket Password", description = "Couchbase bucket password")
  static final String CACHE_BUCKET_PASSWORD_PROPERTY = "bucketPassword";

  private static final Logger log = LoggerFactory.getLogger(CouchbaseClientImpl.class);

  private String clientId;
  private boolean enabled;
  private String bucketName;
  private Cluster cluster;
  private AsyncBucket bucket;

  @Activate
  private void activate(Map<String, Object> config) {
    clientId = PropertiesUtil.toString(config.get(CLIENT_ID_PROPERTY), null);
    enabled = PropertiesUtil.toBoolean(config.get(ENABLED_PROPERTY), ENABLED_PROPERTY_DEFAULT);
    if (!enabled) {
      log.info("Couchbase caching for client '{}' is disabled by configuration.", clientId);
      return;
    }

    String[] couchbaseHosts = PropertiesUtil.toStringArray(config.get(COUCHBASE_HOSTS_PROPERTY));
    if (couchbaseHosts == null || couchbaseHosts.length == 0) {
      enabled = false;
      log.warn("No couchbase host configured, client '{}' is disabled.", clientId);
      return;
    }

    bucketName = PropertiesUtil.toString(config.get(CACHE_BUCKET_NAME_PROPERTY), null);
    String bucketPassword = PropertiesUtil.toString(config.get(CACHE_BUCKET_PASSWORD_PROPERTY), null);
    if (StringUtils.isEmpty(bucketName)) {
      enabled = false;
      log.warn("No couchbase bucket name configured, client '{}' is disabled.", clientId);
      return;
    }

    try {
      cluster = CouchbaseUtil.createCluster(couchbaseHosts);
      bucket = CouchbaseUtil.openBucket(cluster, bucketName, bucketPassword);
    }
    catch (Throwable ex) {
      enabled = false;
      log.warn("Unable to connect to couchbase cluster or open couchbase bucket, client '" + clientId + "' is disabled.", ex);
    }
  }

  @Deactivate
  private void deactivate() {
    if (bucket != null) {
      bucket.close();
      bucket = null;
    }
    if (cluster != null) {
      cluster.disconnect();
      cluster = null;
    }
  }

  @Override
  public String getClientId() {
    return clientId;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public String getBucketName() {
    return bucketName;
  }

  @Override
  public AsyncBucket getBucket() {
    return bucket;
  }

}
