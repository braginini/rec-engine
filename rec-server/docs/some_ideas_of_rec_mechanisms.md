1) User profile research (basically for users that are registered in store)
   - user has a set of viewed/purchased items
   - process these items and select top attributes
   - select top items from whole catalog matching top attributes from the previous step

2) Simple recs by attributes ( category and price )
   - user is viewing item -> show him items from same category and price in 10% dispersion
   - it is done in real time or pre calculated for each item

3) Advanced recs by attributes
   - items have a set of attributes
   - weight the attributes (category and price are with the biggest weights)
   - for each item if category is different similarity is 0
   - calculate cosine similarity between 2 attribute vectors
   - we can later weight parameters depends on history of purchases/views.



4) Advanced user profile research (Bayes Classification)
   - user has a set of viewed
   - user has a set of purchased items
   - define 2 classes "viewed did not buy" and "viewed bought" (select top attrs)
   - select items from whole catalog matching "viewed bought" items

5) Advanced CF (think about it more)
   - there are viewed items
   - there are bought items
   - define 2 classes "viewed did not buy" and "viewed bought" (select top attrs)
   - show "viewed bought" items
