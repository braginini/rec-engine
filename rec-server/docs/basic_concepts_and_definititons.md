1) Recommendation types:

        type 1 - frequently bought
        type 2 - people who viewed this item also interested in ...
        type 3 - people who bought this item bought these items ...
        type 4 - cart recommendations - u may be interested in these ...
        type 5 - on main store page when user comes back show him his history and related items
        type 6 - recommendations for registered users (later, not described)
        type 7 - when user adds item to basket - display him popup that he just added item and show there recommendations


2) Algorithms/techniques/description to calculate recommendations:

        for type 1 - get the each user purchase transactions and for each item get the most bought together
        for type 2 - get all user views of this item and build an item-to-item matrix for each item (e.g. Amazon item-to-item CF)
        for type 3 - get all user purchases of this item and build an item-to-item matrix for each item (e.g. Amazon item-to-item CF)
        for type 4 - combine 3 types described before
        for type 5 - if user was already on store combine 3 types described before


3) How to collect data for calculating recommendations:

        for type 1 - log all user purchase transactions (when user confirmed purchase)
            ruId, partnerId, array of items (ids)

        for type 2 - log all user views of the item (when user opens item page and maybe when recommendations are displayed)
            ruId, partnerId, itemId

        for type 3 - log all user purchases of the item (when user confirmed purchase)
            ruId, partnerId, itemId

        log user registration

        log add to cart

        All these can be done by placing javascript code in seller's online store


4) How to collect statistics or a recommendations engine (for marketing purposes, user actions, displaying
   statistics for our customers and prove that system works):

       for all recommendation  types log all user clicks on displayed recommendations and discover if the purchase was
       done as result of a click on a  recommendations


