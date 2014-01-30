gemfire-dsl
===========

# A Groovy DSL for GemFire

[![Build Status](https://travis-ci.org/smaldini/gemfire-dsl.png?branch=master)](https://travis-ci.org/smaldini/gemfire-dsl)

An awesome builder for your data grid.

Groovy's Closure object can be used in place of each of GemFire's listener objects.

The meta-programming capabitilies offered by Groovy's extension modules feature means that it's possible to add additional methods to existing Java objects.
For example, it's possible to add a new method to the Region object that takes a Closure and maps that to the methods in a CacheListener.

    public void cacheListener(Closure closure)

The resulting code in a Groovy program would look like this:

    def region = cache.getRegion('regionName')
    region.cacheListener {
      afterCreate { e->
        println "received afterCreate event: ${e}"
      }
    }

Just as easily, a CacheWriter can be implemented using a Closure, with the result as follows:

    region.cacheWriter {
      beforeCreate { e->
        println "received beforeCreate event: ${e}"
      }
    }


