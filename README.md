# java12-elasticsearch-inverted-index-workshop

* references
    * http://siddhumehta.blogspot.com/2014/06/elasticsearch-tutorial-inverted-index.html
    * https://www.manning.com/books/elasticsearch-in-action
    * https://medium.com/elasticsearch/introduction-to-analysis-and-analyzers-in-elasticsearch-4cf24d49ddab
    * https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-mapping-charfilter.html

## elasticsearch
* document
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
    * document-oriented: the smallest unit of data you index or search for is a document
    * properties
        * self-contained: contains both the fields and their values
        * can be hierarchical - documents within documents
        * flexible structure - don’t depend on a predefined schema.
    * is JSON representation of your data
        * can contain arrays of values
* types
    * are logical containers for documents
        * similar to how tables are containers for rows
    * documents with different structures (schemas) should be in different types
        * example: employees and tasks
    * definition of fields in each type is called a mapping
        * name -> string, location -> geo_point
        * different handling: searching for a name that starts with M, searching for a location that is within 30 km
        * the mapping contains all the fields of all the documents indexed so far in that type
        * if a new document gets indexed with a field that’s not already in the mapping, Elasticsearch automatically 
        adds that new field to your mapping (it guesses its type, and might not guess right)
    * mapping types only divide documents logically
        * physically, documents from the same index are written to disk regardless of the mapping type they belong to
* indices
    * are containers for mapping types
    * is an independent chunk of documents
        * like a database in the relational world: each index is stored on the disk in the same set of files
    * you can search across types, you can search across indices
    * searches are often not run on the very latest indexed data (which would be real time) but close to it
    * Elasticsearch keeps a point-in-time view of the index opened, so multiple searches would hit the same files and 
    reuse the same caches
        * during this time, newly indexed documents won’t be visible to those searches until you do a refresh
    * refreshing, as the name suggests, refreshes this point-in-time view of the index so your searches can hit your 
    newly indexed data
        * the default behavior is to refresh every index automatically every second
    * with Elasticsearch the process of refreshing and the process of committing in-memory segments to disk are 
    independent
        * data is indexed first in memory, but after a refresh, Elasticsearch will happily search the in-memory
        segments as well
        * the process of committing in-memory segments to the actual Lucene index you have on disk is called a 
        flush, and it happens whether the segments are searchable or not
    * to make sure that in-memory data isn’t lost when a node goes down or a shard is relocated, Elasticsearch keeps 
    track of the indexing operations that weren’t flushed yet in a transaction log
        * Besides committing in-memory segments to disk, a flush also clears the transaction log
    * a flush is triggered in one of the following conditions
      * the memory buffer is full
      * a certain amount of time passed since the last flush
      * the transaction log hit a certain size threshold
* analysis
    ![alt text](img/analysis_overview.png)
    ![alt text](img/analysis_example.png)
    * is the process Elasticsearch performs on the body of a document before the
      document is sent off to be added to the inverted index
    * steps
        * character filtering — transforms the characters using a character filter
            * are used to transform particular character sequences into other character sequences
            * example
                * stripping HTML out of text
                * '4' -> 'for', '2' -> 'too', 'U' -> 'you'
        * breaking text into tokens — breaks the text into a set of one or more tokens
            * token - smaller, meaningful string
            * lucene itself doesn’t act on large strings of data; instead, it acts on what are known as tokens
            * example: splitting text into tokens based on whitespace
        * token filtering — transforms each token using a token filter
            * example
                * lowercase token filter, 'Good' -> 'good'
                * removing the stopwords ('and', 'the', ...)
                * adding synonyms
        * token indexing — stores those tokens into the index
            * sent to Lucene to be indexed for the document
            * make up the inverted index
* inverted indexing
    * Lucene data structure where it keeps a list of where each word belong
    * example: index in the book with words and what pages they appear
* node
    * node is an instance of Elasticsearch
    * multiple nodes can join the same cluster
    * with a cluster of multiple nodes, the same data can be spread across multiple servers
        * helps performance because Elasticsearch has more resources to work with
        * helps reliability: if you have at least one replica per shard, any node can disappear and Elasticsearch 
        will still serve you all the data
* shard
    * the smallest unit Elasticsearch deals with
    * is a Lucene index: a directory of files containing an inverted index
    * Elasticsearch index is broken down into chunks: shards
    * stores the original document’s content plus additional information, such as term dictionary and term frequencies
        * term dictionary maps each term to identifiers of documents containing that term
        * term frequencies give Elasticsearch quick access to the number of appearances of a term in a document
            * important for calculating the relevancy score of results
            * by default, the ranking algorithm is TF-IDF
    * replicas are exact copies of the primary shard
    * replicas can be added or removed at runtime—primaries can’t
    * by default, documents are distributed evenly between shards: for each document, the shard is determined by
      hashing its ID string
        * each shard has an equal hash range, with equal chances of receiving the new document
        * once the target shard is determined, the current node forwards the document to the node holding that shard
        * subsequently, that indexing operation is replayed by all the replicas of that shard
    * with searching, the node that receives the request forwards it to a set of shards containing all your data
        * using a round-robin, Elasticsearch selects an available shard (which can be primary or replica) and 
        forwards the search request to it
        * Elasticsearch then gathers results from those shards, aggregates them into a single reply, and forwards 
        the reply back to the client application
    * as you add more nodes to the same cluster, existing shards get balanced between all nodes
* segment
    * segment is a chunk of the Lucene index (or a shard, in Elasticsearch terminology) that is created when you’re 
    indexing
    * segments are never appended—only new ones are created as you index new documents
    * data is never removed from them because deleting only marks documents as deleted
    * finally, data never changes because updating documents implies re-indexing
    * when Elasticsearch is performing a query on a shard, Lucene has to query all its segments, merge the results, 
    and send them back—much like the process of querying multiple shards within an index. As with shards, the more 
    segments you have to go though, the slower the search
    * normal indexing operations create many such small segments
        * to avoid having an extremely large number of segments in an index, Lucene merges them from time to time
    * merging some documents implies reading their contents, excluding the deleted documents, and creating new and 
    bigger segments with their combined content. This process requires resources—specifically, CPU and disk I/O
    * merges run asynchronously
    * because they don’t change, segments are easily cached, making searches fast
    * updating a document can’t change the actual document; it can only index a new one
    * default merge policy is tiered, divides segments into tiers, and if you have more than the set maximum number 
    of segments in a tier, a merge is triggered in that tier