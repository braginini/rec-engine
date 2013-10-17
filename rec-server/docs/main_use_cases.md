1) Main points for non registered user:
            recommendations:
                - on main page:
                    1) Show nothing on cold start
                    2) Show recently viewed and related items not on cold start

                 - on item page:
                     1) Show similar items (collaborative) to an item on both cold and not start
                     2) Show recently viewed items on not cold start
                     3) Show frequently bought items with this item on both cold and not cold start
                     4) Show alternate items for an item on both cold and not cold start
                     5) Show ultimate items to an item (customers see this item and by another from this list, item-to item collaborative)
                        on both cold and not start

            For each recommendation type retailer's client code makes a request to recommendation engine server
                    recommendation engine returns prepared recommendations in form of widget/json (depends on client needs)

             1. when the user is on main page

                if (user is returning user) //he has cookies set
                    log store attendance
                    REQUEST recommendations based on history (type 5)
                    show recommendations widget

                else {
                    REQUEST user params ( REQUEST params shop unique id
                                          RESPONSE params "UserId": "5157d6bc0d422d08cc22a2f9","PartnerId": "50b64fc3b994b3176ce60c9e")
                    set received params to cookies
                }

                for each recommendation click
                    REQUEST server to log recommendation click (or save this info and send it with assign params method on
                    page of item user clicked, to reduce server requests)
             2. when the user is on item page

                if (user is returning user) // cookies are set
                   log store attendance
                   REQUEST recommendations based on item (types 1-3)
                   show recommendations widgets

                else {
                    request user params ( REQUEST params shop unique id
                                          RESPONSE params "UserId": "5157d6bc0d422d08cc22a2f9","PartnerId": "50b64fc3b994b3176ce60c9e")
                    set received params to cookies
                }

                for each recommendation click
                    REQUEST server to log recommendation click (or save this info and send it with assign params method on
                    page of item user clicked, to reduce server requests)

             3. when the user opens cart (item page)
                    REQUEST for cart recommendations (type 4)
                    show recommendation widgets

                for each recommendation click
                    REQUEST server to log recommendation click (or save this info and send it with assign params method on
                    page of item user clicked, to reduce server requests)

             4. when the user confirms purchase in cart
                    REQUEST for server to log purchase with a set of purchased items




Summary:

    -log item view
    -log add to cart
    -log items purchase
    -log clicks on recommendations

    -set params
    -display item page recommendations
    -display cart recommendations
    -display recommendations when add to cart (displaying popup)

    -display statistics for customers (internal)

