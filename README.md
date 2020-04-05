# java12-elasticsearch-inverted-index-workshop

* references
    * http://siddhumehta.blogspot.com/2014/06/elasticsearch-tutorial-inverted-index.html
    * https://www.manning.com/books/elasticsearch-in-action
    * https://medium.com/elasticsearch/introduction-to-analysis-and-analyzers-in-elasticsearch-4cf24d49ddab

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
* analysis
    ![alt text](img/analysis_overview.png)
    ![alt text](img/analysis_example.png)
    * is the process Elasticsearch performs on the body of a document before the
      document is sent off to be added to the inverted index
    * steps
        * character filtering — transforms the characters using a character filter
            * are used to transform particular character sequences into other character sequences
            * example stripping HTML out of text, or '4' -> 'for', '2' -> 'too', 'U' -> 'you'
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