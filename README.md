# java12-elasticsearch-inverted-index-workshop

* references
    * http://siddhumehta.blogspot.com/2014/06/elasticsearch-tutorial-inverted-index.html
    * https://www.manning.com/books/elasticsearch-in-action
    * https://medium.com/elasticsearch/introduction-to-analysis-and-analyzers-in-elasticsearch-4cf24d49ddab
    * https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-mapping-charfilter.html
    * https://www.elastic.co/guide/en/elasticsearch/reference/7.6/index.html

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
    * Elasticsearch is a distributed document store
    * document is the smallest unit of data you index or search for
    * properties
        * self-contained: both fields and their values
        * can be hierarchical: documents within documents
        * flexible structure: don’t depend on a predefined schema
    * JSON representation
        * can contain arrays of values
* types
    * are logical containers for documents
        * similar to how tables are containers for rows
    * documents with different structures (schemas) should be in different types
        * example: employees and tasks
    * mapping: definition of fields in each type
        * example: name -> string, location -> geo_point
            * different handling: searching for a name that starts with M, searching for a location that is within 30 km
        * contains all the fields of all the documents indexed in that type
        * new fields in an indexed document => Elasticsearch automatically adds them to your mapping
            * guessing it types
    * mapping divide documents logically
        * physically, documents from the same index are written to disk regardless of the mapping type they belong to
* indices
    * can be thought of as an optimized collection of documents
        * Elasticsearch indexes all data in every field and each indexed field has a dedicated, optimized data 
        structure
            * for example, text fields -> inverted indices, numeric and geo fields -> BKD trees
        * useful to index the same field in different ways for different purposes
    * like a relational database: each index is stored on the disk in the same set of files
    * you can search across types and search across indices
    * near-real time 
        * searches not run on the latest indexed data
        * a point-in-time view of the index - multiple searches hit the same files and reuse the same caches
            * newly indexed documents are not visible until refresh
    * refresh - refreshes point-in-time view
        * default: every 1s
    * process of refreshing and process of committing in-memory segments to disk are independent
        * data is indexed first in memory (search disk + in-memory segments as well)
        * flush: the process of committing in-memory segments to disk (Lucene index)
            * transaction log: in cae of a node goes down or a shard is relocated - track of not flushed operations 
                * flush also clears the transaction log
    * a flush is triggered by
      * the memory buffer is full
      * time since last flush
      * the transaction log hit a threshold
* analysis
    ![alt text](img/analysis_overview.png)
    ![alt text](img/analysis_example.png)
    * is the process Elasticsearch performs on the body of a document before the document is sent off to be added to 
    the inverted index
        * the same process is then applied to the query string
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
    * The analysis chain that is applied to a full-text field during indexing is also used at search time. 
        * When you query a full-text field, the query text undergoes the same analysis before the terms are looked up in the index
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
    * For performance reasons, the nodes within a cluster need to be on the same network. 
        * Balancing shards in a cluster across nodes in different data centers simply takes too long
        * Cross-cluster replication (CCR)
* shard
    * an Elasticsearch index is really just a logical grouping of one or more physical shards, where each shard is 
    actually a self-contained index
    *  By distributing the documents in an index across multiple shards, and distributing those shards across multiple 
    nodes, Elasticsearch can ensure redundancy, which both protects against hardware failures and increases query 
    capacity as nodes are added to a cluster
    * There are two types of shards: primaries and replicas. Each document in an index belongs to one primary shard. 
        * A replica shard is a copy of a primary shard.
    * The number of primary shards in an index is fixed at the time that an index is created, but the number of replica 
    shards can be changed at any time,
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
* scoring
    * TF: The first way to think of scoring a document is to look at how often a term occurs in the text
    * IDF: token is less important the more times it occurs across all of the documents in the index
    * Lucene’s default scoring formula, known as TF-IDF
        * apart from normalization & other factors, in general, it is simply: `TF * 1/IDF`