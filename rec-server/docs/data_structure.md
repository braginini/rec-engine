basic requirements for data structure

There are few actions that will be performed with a db access

Real time:
    - fast access to item recommendations
    - adjust item recommendations based on a user profile (view and purchase history, etc)
    - adjust item recommendations based on an availability
    - adjust item recommendations based on external source (optional, see RTB data sources)
Offline:
    - build recommendations

Customer view approach (all statistics a logged under customer)

    Table <customer_statistics>             addAttr(location, browser)
    Row <apiKey+customerID>
    Family <someFamily>
    Column <views>
           Value <itemId, addAttr>
    Column <addToCart>
           Value <itemId, addAttr>
    Column <recClick>
           Value <itemId, recType, addAttr>
    Column <purchase>
           Value <itemIDs [], addAttr>
    Column <addInfo>
           Value <some additional info from external resources, see RTB data sources>

    Can use better approach -->
    Family - statistics types (v(views), p(purchases), c(add to cart))
    Columns <itemId>
            Value <attr>
    //think about if user is registered in store    

    Table <item_recommendations>
    Row <apiKey + itemId>
    Families - <recommendation types>
    Columns - <itemIds>
              Values <similarity ratio>

    //or
    One family
    Columns - <recommendation types>
              Values <itemId + similarity ratio>


    Table <item_meta>
    Row <apiKey + itemId>
    Columns - <item meta from store feed file (e.g. availability, name, price, etc)>



Item view approach (all statistics a logged under items)

    Table <item_statistics>             addAttr(location, browser)
    Row <apiKey + itemId>
    Family <someFamily>
    Column <views>
           Value <ruId, addAttr>
    Column <addToCart>
           Value <ruId, addAttr>
    Column <recClick>
           Value <ruId, recType, addAttr>
    Column <purchase>
           Value <ruId, itemIDs [], addAttr>

    Other tables are same

