package io.prismic;

import com.fasterxml.jackson.databind.JsonNode;
import io.prismic.wildsmile.WildsmileCache;
import org.apache.commons.collections4.map.LRUMap;

public interface Cache {

  void set(String key, Long ttl, JsonNode response);
  JsonNode get(String key);
  JsonNode getOrSet(String key, Long ttl, Callback f);

  // --
  class NoCache implements Cache {

    @Override
    public void set(String key, Long ttl, JsonNode response) {
    }

    @Override
    public JsonNode get(String key) {
      return null;
    }

    @Override
    public JsonNode getOrSet(String key, Long ttl, Callback f) {
      return f.execute();
    }

  }

  // --


  class DefaultCache {

    //private static final Cache defaultCache = new BuiltInCache(10);
    private static final Cache defaultCache = new WildsmileCache(10);

    private DefaultCache() {}

    public static Cache getInstance() {
      return defaultCache;
    }
  }


  // --

  interface Callback {
    JsonNode execute();
  }

  /*
  class BuiltInCache implements Cache {

    private final java.util.Map<String, Entry> cache;

    static class Entry {
      public final Long expiration;
      public final JsonNode value;
      public Entry(Long expiration, JsonNode value) {
        this.expiration = expiration;
        this.value = value;
      }
    }

    public BuiltInCache(int maxDocuments) {
      this.cache = java.util.Collections.synchronizedMap(new LRUMap<String, Entry>(maxDocuments));
    }

    @Override
    public JsonNode get(String key) {
      synchronized(this.cache) {
        Entry entry = this.cache.get(key);
        Boolean isExpired = this.isExpired(key);
        if (entry != null && !isExpired) {
          return entry.value;
        }
      }
      return null;
    }

    @Override
    public void set(String key, Long ttl, JsonNode response) {
      Long expiration = ttl + System.currentTimeMillis();
      synchronized(this.cache){
        this.cache.put(key, new Entry(expiration, response));
      }
    }

    @Override
    public JsonNode getOrSet(String key, Long ttl, Callback f) {
      JsonNode found = this.get(key);
      if(found == null) {
        JsonNode json = f.execute();
        this.set(key, ttl, json);
        return json;
      } else {
        return found;
      }
    }

    private Boolean isExpired(String key) {

      System.out.println("Cache Size: " + this.cache.size());


     // Entry entry = this.cache.get(key);
    //  return entry != null && entry.expiration !=0 && entry.expiration < System.currentTimeMillis();


      //System.out.println("Is Cache Empty: " + this.cache.isEmpty() );
      return false; //cache never expires
    }

  }
  */
}
