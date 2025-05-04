[![Build Status](https://app.travis-ci.com/mtumilowicz/java12-elasticsearch-inverted-index-workshop.svg?branch=master)](https://travis-ci.com/mtumilowicz/java12-elasticsearch-inverted-index-workshop)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# java12-elasticsearch-inverted-index-workshop

* references
    * http://siddhumehta.blogspot.com/2014/06/elasticsearch-tutorial-inverted-index.html
    * https://www.manning.com/books/elasticsearch-in-action
    * https://medium.com/elasticsearch/introduction-to-analysis-and-analyzers-in-elasticsearch-4cf24d49ddab
    * https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-mapping-charfilter.html
    * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/index.html
    * https://www.elastic.co/guide/en/elasticsearch/reference/current/glossary.html#glossary-primary-shard
    * https://www.nurkiewicz.com/2019/03/mapmerge-one-method-to-rule-them-all.html
    * https://www.quora.com/What-is-inverted-index-It-is-a-well-known-fact-that-you-need-to-build-indexes-to-implement-efficient-searches-What-is-the-difference-between-index-and-inverted-index-and-how-does-one-build-inverted-index
    * https://www.book-editing.com/why-book-indexing/
    * [2018 - Philipp Krenn - Full-Text Search Internals](https://www.youtube.com/watch?v=TiLYEqfdVhs)
    * https://codingexplained.com/coding/elasticsearch/understanding-sharding-in-elasticsearch
    * https://codingexplained.com/coding/elasticsearch/introduction-elasticsearch-architecture
    * https://codingexplained.com/coding/elasticsearch/understanding-replication-in-elasticsearch
    * https://chatgpt.com/
    * https://www.elastic.co/docs/manage-data/data-store/mapping/removal-of-mapping-types
    * https://www.elastic.co/docs/manage-data/data-store/mapping
    * https://www.elastic.co/blog/what-is-an-elasticsearch-index

## preface
* goals of this workshop
    * understand foundations of elasticsearch
        * index and inverted index
        * term analysis
    * understand how queries are analyzed (character filters, tokenizers, token filters)
    * introduction to internals: Lucene's segments, scoring, shards and nodes

* workshop are in `workshop` package, answers: `answers`

## elasticsearch
### document
```
{
    "name": "Michal",
    "surname": "Tumilowicz",
    "address": {
        "city": "Warsaw",
        "postalCode": "00-349"
    }
    "tasks": ["Task1", "Task2"]
}
```
* Elasticsearch is a distributed document store
* document is the smallest unit of data you index or search for
* properties
    * self-contained: both fields and their values
    * can be hierarchical: documents within documents
    * flexible structure: don’t depend on a predefined schema
* JSON representation
    * can contain arrays of values
 
### types
* elasticsearch 8.0.0 no longer supports mapping types
    * only one type per index is allowed, and it's always `_doc`
    * mapping is now defined at the index level, not per type

### mapping
* is the schema definition for the documents in an index
    * document = collection of fields + data types
    * includes metadata fields
        * example
            * `_index` - index to which the document belongs
            * `_id` - document’s ID
            * `_source` - original JSON representing the body of the document
            * others
* example
    ```
    GET /your-index-name/_mapping
  
    {
      "people": {
        "mappings": {
        "_source": { // by default ommitted
          "enabled": true
        },
        "_meta": { // custom metadata (ex.: for documentation or tooling)
          "version": "1.0",
          "description": "People index for sanction screening"
        },
          "properties": {
            "name": { "type": "text" },
            "birthdate": { "type": "date" },
            "country": { "type": "keyword" },
            "bio_vector": {
              "type": "dense_vector",
              "dims": 384
            }
          }
        }
      }
    }
    ```
* two types
    * dynamic mapping
        * automatically detects the data types of fields
            * might yield suboptimal results for specific use cases
            * default mappings
                * defined using dynamic templates
                * example: map `app_*.code` as keywords
                    ```
                    PUT /logs
                    {
                      "mappings": {
                        "dynamic_templates": [
                          {
                            "map_app_codes_as_keyword": {
                              "path_match": "app_*.code",
                              "mapping": {
                                "type": "keyword"
                              }
                            }
                          }
                        ]
                      }
                    }
                    ```
                    will produce types
                    ```
                    "app_error.code": { "type": "keyword" }
                    "app_warning.code": { "type": "keyword" }
                    ```
        * add new fields automatically
            * use case: some fields cannot be known in advance
        * some data types cannot be automatically detected
            * example: `geo_point`
                * can be represented in multiple ways
                    * string: `"41.12,-71.34"`
                        * looks like text
                        * what is first - latitude or longitude?
                    * array: `[ -71.34, 41.12 ]`
                        * looks like numeric array
                    * object: `{ "lat": 41.12, "lon": -71.34 }`
                        * looks like JSON
                * so Elasticsearch requires to explicitly declare `geo_point` fields in mapping
    * explicit mapping
        * used to have greater control over fields
        * recommended for production use cases
* can’t change mappings for fields that are already mapped
    * requires reindexing
    * sometimes multi-fields are solution (index same field in different ways)
        * drawback: old documents will not have them
        * example
            ```
            "city": {
              "type": "text",
              "fields": {
                "raw": {
                  "type":  "keyword"
                }
              }
            }
            ```
* mapping explosion
    * too many fields in an index => risk of out of memory errors
    * can be caused by lack of control over dynamic mapping
        * example: every new document inserted introduces new fields
    * solution: use the mapping limit settings to limit the number of field mappings

### indices
* logical namespace that holds a collection of documents
    * can be considered as a table
* logical abstraction over one or more Lucene indices (called shards)
    * by default: all shards are queried
        * solution: create logical groups of data in separate indices
            * example
                ```
                customers-switzerland → 2 shards
                customers-germany → 2 shards
                customers-rest → 1 shard
                ```
* can be thought of as an optimized collection of documents
    * each indexed field has optimized data structure
        * example
            * text fields -> inverted indices
            * numeric and geo fields -> BKD trees
* near-real time search
    * searches not run on the latest indexed data
        * indexing ≠ search visibility
        * however, document can be retrieved by ID immediately
            * but a search query won’t return it until a refresh happens
    * point-in-time view of the index
        * multiple searches hit the same files and reuse the same caches
* processes
    * indexing = storing
        * document is put in two places
            * in-memory buffer (Lucene memory buffer)
            * transaction log (called translog) on disk
                * crash recovery log
                * translog is not searchable
        * when
            * document sent to Elasticsearch
        * after indexing
            * document is durable (even if node crashes)
            * not yet searchable
    * refresh
        * makes newly indexed documents searchable
        * writes in-memory buffer into a new Lucene segment
            * usually reside in the OS page cache (memory)
                * aren’t guaranteed to be persisted until `fsync` or `flush`
                * in particular: files may never hit the actual disk
                    * Lucene will ignore them if there's no updated `segments_N`
                        * => update is done during commit
        * opens a new searcher
            * sees all committed segments
            * sees any new segments created by a refresh
            * does not see uncommitted in-memory data
                * example: buffer
            * every search request is handled by
                * grabbing the current active searcher
                    * each shard knows its current searcher
                * executing the query against that consistent view
                    * writes don’t interfere with ongoing searches
        * when
            * automatically every 1 second (default)
            * manually: `POST /my-index/_refresh`
        * after refresh
            * documents are searchable
    * commit
        * it is not about search
            * does not affect search => searchers see segments based on refresh, not commit
        * uses `fsync`
            * the only way to guarantee that the operating system has actually written data to disk
        * pauses index writers briefly
            * to ensure that commit reflects a consistent index state
        * clears the translog (since changes are now safely in Lucene index)
        * each commit creates a new `segments_N` file with an incremented generation number `(N)`
            * represents current state of the index
                * lists all the active segments
                * older `segments_N` files are effectively obsolete after a new one is committed
            * binary file
                * textual example
                    ```
                    Segments:
                    ----------
                    Segment: _0
                    - Uses compound file: true
                    - Doc count: 1,000
                    - Deleted docs: 0
                    - Files:
                        _0.cfs
                        _0.cfe
                        _0.si
                    - Codec: Lucene90
                    - Segment created with Lucene 9.9.0
                    ```
            * Lucene reads this file on startup
                * tells which `.cfs` segment files to load and use
                * reads `segments.gen` to find the latest `segments_N` file
        * when
            * the memory buffer is full
            * time since last flush
            * the transaction log hit a threshold
            * in particular: refreshing and committing are independent
    
### inverted indexing
* Lucene data structure where it keeps a list of where each word belongs
    ![alt text](img/inverted-index.jpg)
* example: index in the book with words and what pages they appear
    ![alt text](img/book-index.jpg)

### analysis
![alt text](img/analysis_overview.png)
![alt text](img/analysis_example.png)
* steps
    * character filtering — transform character sequences into character sequences
        * example
            * stripping HTML out of text
            * '4' -> 'for', '2' -> 'too', 'U' -> 'you'
    * breaking text into one or more tokens
        * token - smaller, meaningful string
        * lucene itself doesn’t act on large strings but on tokens
        * example: splitting text into tokens based on whitespaces
    * token filtering — transforms each token using a token filter
        * example
            * lowercase token filter, 'Good' -> 'good'
            * removing the stopwords ('and', 'the', 'my')
               * note that sometimes (very rarely) stopwords are important and can be helpful: "to be, or not to be"
            * adding synonyms
    * token indexing — stores those tokens into the index
* the query text undergoes the same analysis before the terms are looked up in the index

### node
* node is an instance of Elasticsearch
* multiple nodes can join the same cluster
* cluster
    * same data can be spread across multiple servers (replication)
        * helps performance: adds resources to work with
        * helps reliability: data is replicated
    * all nodes need to be on the same network
        * balancing shards across data centers simply takes too long
            * example: master issues relocation commands if it detects unbalanced shard distribution
        * cross-cluster replication (CCR)
            * allows you to replicate data from one cluster (leader cluster) to another (follower cluster)
            * example: across data centers, regions, or cloud availability zones
* roles
    * master
        * election: Raft-inspired
        * maintains the cluster state (node joins/leaves, index creation, shard allocation)
        * assign shards to nodes
            * example: when new index is created
                * based on node capabilities and existing shard distribution
    * data
        * stores actual index data (primary and replica shards)
    * coordinating
        * maintains a local copy of the cluster state
            * only the master node updates the cluster state, but all nodes subscribe to it
        * routes client requests
            * formula: hash of id % number_of_primary_shards => picks the target shard
                * number of primary shards in an index is fixed at the time that an index is created
            * in particular: Elasticsearch always maps a routing value to a single shard
        * returns final result
            * example: merges responses to aggregate results
        * every node in Elasticsearch can act as a coordinating node

### shard
* is a Lucene shard: a directory of files containing an inverted index
* cannot be split or merged easily
* index is just a logical grouping of physical shards
    * each shard is actually a self-contained index
    * example
        * suppose that an index = 1 terabyte of data
        * there are two nodes: each with 512 gigabytes available for storing data
            * the entire index will not fit on either of the nodes
        * we need some way of splitting the index
            * sharding comes to the rescue
* contains
    * segment files
    * metadata files (how to read, decode, and interpret the raw data files in a segment)
        * example: `.fnm` (field names and types)
    * commit files (which segments to load after a crash or restart)
        * `segments_N` (snapshot of all current segments)
        * `segments.gen` (tracks the latest segments_N file)
        * `write.lock` (prevent concurrent writers)
* can be hosted on any node within the cluster
    * not necessarily be distributed across multiple physical or virtual machines
        * example
            * 1 terabyte index into four shards (256 gb each)
            * shards could be distributed across the two nodes (2 per node)
    * as you add more nodes to the same cluster, existing shards get balanced between all nodes
* two types of shards: primaries and replicas
    * primary shard: all operations that affect the index
        * example: adding, updating, or removing documents
    * flow
        1. operation completes on primary shard => it is forwarded to each of the replica shards
        1. operation completes on every replica => responds to the primary shard
        1. primary shard responds to the client
    * each document is stored in a single primary shard
    * replica shard is a copy of a primary shard
        * are never allocated to the same nodes as the primary shards
        * serves two purposes
            * provide high availability in case nodes or shards fail
            * increase performance for search queries (searches can be executed on all replicas in parallel)
* documents are distributed evenly between shards
    * the shard is determined by hashing document id
    * each shard has an equal hash range
* two main reasons why sharding is important
    * allows you to split and thereby scale volumes of data
    * operations can be distributed across multiple nodes and thereby parallelized
        * multiple machines can potentially work on the same query
    
### segment
* contains
    * inverted index
        * term dictionary
            * maps term to offset in a posting list
            * contains document frequency
            * example
                * term: "shoes" → df = 2, offset = 842
                    * Lucene know that from offset X should read exactly 2 document entries
        * postings lists
            * stores all the information Lucene needs to retrieve and rank documents
                * example: document where the term appears, term frequency
    * stored fields (original document fields)
    * doc values (columnar storage for sorting, aggregations, faceting)
        * example
            ```
            DocID   price (doc value)
            --------------------------
            0       59.99
            1       19.99
            2       129.99
            ```
    * norms (field-level stats for scoring)
        * example: length of field
            * longer fields tend to be less precise
* involves 10+ small files per segment
    * in particular: 100 segments => 1000+ files
        * problem: file handle exhaustion
        * solution: .cfs (compound file format)
            * Lucene can read them as if they were separate files (using random-access lookups inside .cfs)
    * not compressed
        * just a flat concatenation of multiple Lucene data files
* is immutable
    * new ones are created as you index new documents
    * deleting only marks documents as deleted
        * Lucene supports deletes via live docs bitmap, not by physically removing the data immediately
        * cleaned up during segment merges
    * updating documents implies re-indexing
        * updating a document can’t change the actual document; it can only index a new one
    * are easily cached, making searches fast
* Lucene queries all its segments, merge the results, and send them back
    * normal indexing operations create many such small segments
    * the more segments you have to go though, the slower the search
        * solution: merging
            * creating new and bigger segments with combined content
                * commit: writes a new `segments_N` listing new merged segment and not segments that were merged
                * example: excluding the deleted documents
            * tiered - default merge policy
                * segments divided into tiers by size
                    * example
                        ```
                        Tier 1: segments ≤ 5 MB
                        Tier 2: segments ≤ 25 MB
                        Tier 3: segments ≤ 150 MB
                        ...
                        ```
                * each tier has a threshold number of segments
                    * if threshold hit in a tier => merge in that tier
                * prioritizes merging small segments first (cheap, fast)
                    * avoids merging huge segments unless necessary

### scoring
* TF: how often a term occurs in the document
* IDF: the token's importance is inversely proportional to the number of occurrences across all of the documents
    * `IDF = log(N / df)`
        * N = total number of documents
        * df = number of documents containing the term
* Lucene’s default scoring formula, known as TF-IDF
    * apart from normalization & other factors, in general, it is simply: `TF * IDF`
