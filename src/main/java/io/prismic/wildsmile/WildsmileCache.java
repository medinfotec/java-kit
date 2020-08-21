package io.prismic.wildsmile;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.RemovalCause;


import java.util.concurrent.TimeUnit;

public class WildsmileCache implements io.prismic.Cache   {


  private final Cache<String, JsonNode> manualCache;

  public WildsmileCache(int maxEntries) {
    this.manualCache = Caffeine.newBuilder()
      .maximumSize(maxEntries)
      .removalListener((String k, Object value, RemovalCause cause) -> {
        System.out.printf("Key %s was removed (%s)%n", k, cause);
      })
      .build();
  }


  @Override
  public void set(String key, Long ttl, JsonNode response) {

    manualCache.put(key,response);

   // manualCache.cleanUp();
    //System.out.println("set Cache and cleanup");
  }

  @Override
  public JsonNode get(String key) {
    return manualCache.getIfPresent(key);
  }

  @Override
  public JsonNode getOrSet(String key, Long ttl, Callback f) {

    System.out.println("Cache Size: " + this.manualCache.estimatedSize());

    JsonNode found = this.get(key);
    if(found == null) {
      JsonNode json = f.execute();
      this.set(key, ttl, json);
      return json;
    } else {
      return found;
    }

  }

}
