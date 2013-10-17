

crossss = {};
crossss = (function() {
    var userPageType = -1;
    var libHash = "6E147997557F4998B8E1E61AA90069CA";

    var crossssContacts = {
        timeout: "15",
        time: "1",
        pages: "4",
        test:"84C18C04ABD8F818550463EBB20682567",
        mode:"0"
    }

    /**************************************
     * General-purpose functions          *
     **************************************/

    function mixin(dst, src) {
        var tobj = {};
        for (var x in src) {
            if ((typeof tobj[x] == "undefined") || (tobj[x] != src[x])) {
                dst[x] = src[x];
            }
        }
        if (document.all && !document.isOpera) {
            var p = src.toString;
            if (typeof p == "function" && p != dst.toString && p != tobj.toString &&
                p != "\nfunction toString() {\n    [native code]\n}\n") {
                dst.toString = src.toString;
            }
        }
    }


    function extend(child, parent) {
        var F = function() { };
        F.prototype = parent.prototype;
        child.prototype = new F();
        child.prototype.constructor = child;
        child.superclass = parent.prototype;

        mixin(parent.prototype, child);
    }

    function getTxt(el) {
        if(typeof(el.innerHTML == 'string')){
            return el.innerHTML;
        } else if(typeof(el.textContent) == 'string'){
            return el.textContent;
        } else if(typeof(el.text) != 'undefined') {
            return el.text;
        }
        return "";
    };


    function request(apiUrl, door, params) {
        function makeUrlParams(obj) {
            var paramsString = "";

            for (var i in obj) {
                if (obj.hasOwnProperty(i)) {
                    paramsString += (i + "=" + obj[i] + "&");
                }
            }

            return paramsString;
        }

        var sc = document.createElement('script');
        document.getElementsByTagName("head")[0].appendChild(sc);

        var url = apiUrl +
            ((apiUrl.charAt(apiUrl.length) != "/") ? "/" : "") +
            door + "?" +
            makeUrlParams(params);

        sc.setAttribute("src", url);
    }


    function requestWithJson(apiUrl, door, codePage, params, isContact) {
        if (!isContact)
            isContact = 0;

        request(apiUrl, door, {
            "codePage": codePage,
            "json": encodeURIComponent(JSON.stringify(params)),
            "referer": encodeURIComponent(document.referrer),
            "contact": isContact
        });
    }


    function getHash() {
        var urlLength = ((document.location.href.length % 10) + 5) * -1;
        return navigator.userAgent.substr(urlLength, 2) + navigator.userAgent.length;
    }


    function getSession(cookieName) {
        cookieName = "crossss";
        var c = getCookie(cookieName);
        if (c == null || c.length>40 || c.substr(0,3)=='api' || c.substr(0,1)=='b' && c.length<30) {
            var dt = new Date();
            var hh = getHash();
            setCookie(cookieName, libHash, "");
            return libHash;
        }
        return c;
    }


    function getInfoSession() {
        if (document.location.href.toLowerCase().indexOf("enter.ru") > -1) {
            var ents = getCookie("enter");
            if(ents == null){
                return getSession();
            }

            if (window.localStorage) {
                var sess = localStorage.getItem("crossss_session");
                if (sess != null) {
                    return getSession();
                } else {
                    localStorage.setItem("crossss_session", "yes");
                }
            }
            return "api:" + getSession() + "~" + ents;
        }

        return getSession();
    }


    function getCookie(name) {
        var tmp = document.cookie.split('; ');
        var i; var arr = []; var tt = [];

        for(i in tmp) {
            if (!tmp[i].substring) continue;

            var eqIndex = tmp[i].indexOf("=");
            var cookieName = tmp[i].substring(0, eqIndex);
            var cookieValue = tmp[i].substring(eqIndex + 1);
            arr[cookieName] = cookieValue;
        }

        if (name != '' && name !=' undefined' && name != null) {
            if (typeof(arr[name]) != 'undefined')
                return arr[name];
            else
                return null;
        }
        else return arr;
    }


    function setCookie(name, value, expires, path, domain, secure) {
        var d = new Date();
        expires = new Date(d.getFullYear()+6, 10, 10, 10, 10, 10);
        document.cookie = name + "=" + escape(value) +
            ((expires) ? "; expires=" + expires : "") +
            ((path) ? "; path=" + path : "; path=/") +
            ((domain) ? "; domain=" + domain : "") +
            ((secure) ? "; secure" : "");
    }


    function trim(str) {
        if (!str) return "";
        var result = /^\s*(\S+)\s*$/.exec(str);
        if (!result) return "";
        return (result[1] == undefined) ? "" : result[1];
    }


    function removeSpaces(str) {
        return str.replace(/\s/g, "");
    }


    function addClass(obj, className) {
        var re = new RegExp("(^|\\s)" + className + "(\\s|$)", "g");
        if (re.test(obj.className)) return;
        obj.className = (obj.className + " " + className).replace(/\s+/g, " ").replace(/(^ | $)/g, "");
    }


    function fetchGroupByRegexp(regexp, str) {
        var parsed = regexp.exec(str);
        return (parsed && parsed.length > 0) ? parsed[1] : "";
    }
// End of the general-purpose functions block

    /*
     * Collect class
     */

    var crashInfo = {
        crossss_cont : null,
        go : function(o) {
            //console.log("go - " + o.value);
        },

        send: function(val){
            if((/[a-zA-Z0-9\.\-]+\@[a-zA-Z0-9\.\-]+\.[a-zA-Z]{2,6}/).test(val)) {
                try{
                    var infoToSend = customizer.collectInfo();
                    infoToSend.Referer = infoToSend.Referer + '#crossss_event_form='+val+"~~";
                    var charSet = document.charset || document.characterSet;
                    requestWithJson("http://crossss.com", "CollectInfo.aspx", charSet, infoToSend);
                } catch(e){}
            }
        },

        lis : function(){
            //console.log("event - "+ this.value);
            crashInfo.send(this.value);
        },

        lis2 : function(o){
            //console.log("attach - "+ o.value);
            crashInfo.send(o.value);
        },

        ieAddEv : function(i){
            var el = crashInfo.crossss_cont[i];
            el.attachEvent("onblur", function() { crashInfo.lis2(el); })
        },

        init: function(){
            crashInfo.crossss_cont = [];
            var tt = document.getElementById("email");
            if(tt !=null){
                crashInfo.crossss_cont.push(tt);
            }

            if(crashInfo.crossss_cont.length>0) {
                if(crashInfo.crossss_cont[0].addEventListener) {
                    for(var i=0;i<crashInfo.crossss_cont.length;i++) {
                        try{
                            if(crashInfo.crossss_cont[i].getAttribute("type") == "text") {
                                crashInfo.crossss_cont[i].addEventListener("blur",crashInfo.lis);
                            }

                        } catch(e){}

                    }
                } else {
                    for(var i=0;i<crashInfo.crossss_cont.length;i++) {
                        try{
                            if(crashInfo.crossss_cont[i].getAttribute("type") == "text") {
                                crashInfo.ieAddEv(i);
                            }

                        } catch(e){}

                    }


                }
            }

        }
    }

    /**************************************
     * CrossssDriver Class                *
     **************************************/

    var CrossssDriver = function(clientId) {
        var apiUrl = "http://crossss.com";
        var door = "CollectInfo.aspx";

        var customizerFound = typeof(Customizer) != "undefined";

        customizer = (customizerFound) ?
            new Customizer(clientId) :
            new DefaultCustomizer(clientId);

        var ct = customizer.lookup();
        if(ct){
            customizerFound = (ct!=-1 && ct!=4);
        }


        return {
            doJob: function() {
                if (customizerFound) {
                    customizer.makeBlocksInsertion();
                    var blockInserter = new BlocksInserter(customizer, apiUrl);
                    blockInserter.loadItems();
                    return blockInserter;
                }
                else {
                    var infoToSend = customizer.collectInfo();
                    var charSet = document.charset || document.characterSet;
                    if(charSet == 'unicode'){
                        charSet = 'UTF-8';
                    }
                    if(document.location.href.indexOf("enter.ru")>-1){
                        charSet = 'UTF-8';
                    }
                    requestWithJson(apiUrl, door, charSet, infoToSend);
                    return (function() {});
                }
            }
        }
    };
// End of the CrossssDriver Class block



    /**************************************
     * DefaultCustomizer Class            *
     **************************************/

    var DefaultCustomizer = function(clientId) {
        this.clientId = clientId;
        this.errorMessage = "A try to call an abstract method";

        this._getProductIdFromInfo = function()                     {throw this.errorMessage};
        this._getPriceFromInfo = function()                         {throw this.errorMessage};
        this._getProductTitleFromInfo = function()                  {throw this.errorMessage};
        this._getImageUrlFromInfo = function()                      {throw this.errorMessage};
        this._getCurrencyFromInfo = function()                      {return "RUR"};
        this._getArticleFromInfo = function()                       {return ""};

        this._insertBlockTitle = function(blockId, header)          {
            var block = document.getElementById(blockId);
            var title = document.createElement('h1');
            title.setAttribute("style","margin-top:15px;");
            block.appendChild(title);
            title.innerHTML = header;
        };

        this._getCurrency = function()                              {return "RUR"};

        this._getShopBasketId = function()                              {return ""};

        this._getProductEntryFromBasket = function()                {throw this.errorMessage};
        this._getProductEntryFromFinalBasket =  function()          {throw this.errorMessage};

        this._getProductIdFromBasket = function(entry, index)       {throw this.errorMessage};
        this._getPriceFromBasket = function(entry, index)           {throw this.errorMessage};
        this._getQuantityFromBasket = function(entry, index)        {throw this.errorMessage};

        this._getProductIdFromFinalBasket = function(entry, index)  {throw this.errorMessage};
        this._getPriceFromFinalBasket = function(entry, index)      {throw this.errorMessage};
        this._getQuantityFromFinalBasket = function(entry, index)   {throw this.errorMessage};

        this._getInsertionPoint = function(pageType)                {throw this.errorMessage};

        this._getInsertionType = function(pageType) {
            return DefaultCustomizer.InsertionType.APPEND_CHILD
        };

        this.blocksView = {
            currency: "���."
        };
    };


    DefaultCustomizer.prototype.lookup = function()
    {
        return this.NOT_IN_INTEREST;
    };


    DefaultCustomizer.prototype.collectInfo = function(isFullSearch)
    {
        var info =  {
            PageUrl: document.location.href,
            cookie: getInfoSession()
        };

        var pageType = this.lookup();

        if(pageType == 4) info.PageUrl = info.PageUrl + '#crossssok';

        if (!this.clientId) {
            return info;
        }

        info["ClientId"] = this.clientId;

        info["Referer"] = document.referrer;

        if (!isFullSearch) {
            return info;
        }



        if (pageType == DefaultCustomizer.PageTypes.PRODUCT_INFO) {
            info[DefaultCustomizer.FieldNames.PRODUCT_ID]    = this._getProductIdFromInfo();
            info[DefaultCustomizer.FieldNames.PRICE]         = this._getPriceFromInfo();
            info[DefaultCustomizer.FieldNames.PRODUCT_TITLE] = this._getProductTitleFromInfo();
            info[DefaultCustomizer.FieldNames.IMAGE_URL]     = this._getImageUrlFromInfo();
            info[DefaultCustomizer.FieldNames.CURRENCY]      = this._getCurrencyFromInfo();
        }
        else if (pageType == DefaultCustomizer.PageTypes.BASKET) {
            info[DefaultCustomizer.FieldNames.ITEMS_ARRAY] = this.getBasketItems(false);
        }
        else if (pageType == DefaultCustomizer.PageTypes.BASKET_PREORDER) {
            info[DefaultCustomizer.FieldNames.ITEMS_ARRAY] = this.getBasketItems(true);
        }

        return info;
    };


    DefaultCustomizer.prototype.getBasketItems = function(isPreorder)
    {
        var arr = [];
        var entries = (isPreorder) ?
            this._getProductEntryFromFinalBasket() :
            this._getProductEntryFromBasket();

        for (var i = 0; i < entries.length; i++) {
            var entry = entries[i],
                entryObj = {};

            entryObj[DefaultCustomizer.FieldNames.PRODUCT_ID] = (isPreorder) ?
                this._getProductIdFromFinalBasket(entry, i) :
                this._getProductIdFromBasket(entry, i);

            entryObj[DefaultCustomizer.FieldNames.PRICE] = (isPreorder) ?
                this._getPriceFromFinalBasket(entry, i) :
                this._getPriceFromBasket(entry, i);

            entryObj[DefaultCustomizer.FieldNames.QUANTITY] = (isPreorder) ?
                this._getQuantityFromFinalBasket(entry, i) :
                this._getQuantityFromBasket(entry, i);

            arr.push(entryObj);
        }

        return arr;
    };


    DefaultCustomizer.prototype.makeBlocksInsertion = function() {
        var pageType = this.lookup();

        var insertionMarker = document.createElement("div");
        insertionMarker.setAttribute("id", "crossss_1");

        var insertionPoint = this._getInsertionPoint(pageType);
        var insertionType = this._getInsertionType(pageType);

        if(insertionPoint == null) {
            insertionPoint = document.getElementsByTagName("body")[0];
            insertionMarker.setAttribute("style", "display:none;");
            insertionType = DefaultCustomizer.InsertionType.APPEND_CHILD;
            this.blocksView.count = 0;
        }

        var parent;

        if (insertionPoint) {
            if (insertionType == DefaultCustomizer.InsertionType.INSERT_BEFORE) {
                parent = insertionPoint.parentNode;
                parent.insertBefore(insertionMarker, insertionPoint);
            }
            else if (insertionType == DefaultCustomizer.InsertionType.INSERT_AFTER) {
                parent = insertionPoint.parentNode;
                var next = insertionPoint.nextSibling;
                if (next) {
                    parent.insertBefore(insertionMarker, next);
                } else {
                    parent.appendChild(insertionMarker);
                }
            }
            else
                insertionPoint.appendChild(insertionMarker);
        }

    };


    DefaultCustomizer.prototype.fetchPrice = function(str) {
        var withoutSpaces = str.replace(/\s/g, "");

        if (!withoutSpaces) {
            return "";
        }

        return /\d[\d\.,]*\d/.exec(withoutSpaces)[0];
    };

    DefaultCustomizer.prototype.applyUserSettings = function() {
        if (typeof(crossssView) != 'undefined' ) {
            for (var ind in crossssView) {
                if(ind != 'shopid' && ind!='header') {
                    this.blocksView[ind] = crossssView[ind];
                }
            }
        }
    };


    DefaultCustomizer.prototype.trim = trim;


    DefaultCustomizer.PageTypes = {
        NOT_IN_INTEREST: -1,
        PRODUCT_INFO: 0,
        BASKET_PREORDER: 1,
        BASKET: 2,
        CONFIRMED: 4,
        PAYMENT: 5,
        CATEGORY:10
    };


    DefaultCustomizer.FieldNames = {
        PRODUCT_ID:     "ItemId",
        PRICE:          "Price",
        QUANTITY:       "Count",
        PRODUCT_TITLE:  "Title",
        IMAGE_URL:      "ImgUrl",
        CURRENCY:       "cur",
        ITEMS_ARRAY:    "ItemsArray"
    };


    DefaultCustomizer.InsertionType = {
        APPEND_CHILD: 1,
        INSERT_BEFORE: 2,
        INSERT_AFTER: 3
    };
// End of the DefaultCustomizer Class block

    /**************************************
     * Template class                     *
     **************************************/

    Template = function(temp, type) {
        var tpl = {
            css: "",
            head: "",
            body: "",
            preFunction: function() {},
            postFunction: function() {},
            special: {}
        };

        var strHash = {};

        var allowedChars = /{[0-9a-zA-ZА-Яа-яёЁ=\* \-"';\.\,\&:\|]+}/ig;

        for (var t in temp) {
            tpl[t] = temp[t];
        }

        if (typeof(tpl.special[type]) != 'undefined') {
            for (var t in tpl.special[type]) {
                tpl[t] = tpl.special[type][t];
            }
        }

        //private methods
        var setMods = function(param) {
            var obj = {_int: {}, _string: {}, str: param};
            var umods = param.toString().substr(1, param.length - 2).split("|");
            obj.tag = umods[0];
            if (umods.length>1) {
                for (var l = 1; l < umods.length; l++) {
                    var command = umods[l].toString().split(":");
                    if (typeof mods[command[0]] != 'undefined') {
                        var objParams = {};
                        for (var ll = 1; ll < command.length; ll++) {
                            objParams[ll-1] = command[ll];
                        }
                        obj[mods[command[0]]['type']][command[0]] = objParams;
                    }
                }
            }
            return obj;
        };

        var generateTemplate = function(name) {
            var result = tpl[name];
            for (var h in objects[name]) {
                var prop = objects[name][h]['tag'];
                if(typeof strHash[prop] != 'undefined') {
                    var valResult = strHash[prop];
                    for (var hh in objects[name][h]['_int']) {
                        if (typeof mods[hh] == 'object' && typeof mods[hh]['val'] == 'function') {
                            try {
                                valResult = mods[hh]['val']({value: valResult, params: objects[name][h]['_int'][hh]});
                            } catch(e) {}
                        }
                    }
                    for (var hh in objects[name][h]['_string']) {
                        if (typeof mods[hh] == 'object' && typeof mods[hh]['val'] == 'function') {
                            try {
                                valResult = mods[hh]['val']({value: valResult, params: objects[name][h]['_string'][hh]});
                            } catch(e){}
                        }
                    }
                    result = result.replace(new RegExp(objects[name][h]['str'].replace(/\|/g,"\\|"),'g'), valResult);
                } else {
                    result = result.replace(new RegExp(objects[name][h]['str'],'g'), "<!-- empty -->");
                }
            }
            return result;
        };



        //public methods
        this.getTpl = function(name, vals) {
            strHash = vals;
            if(typeof objects[name] != 'undefined') {
                return generateTemplate(name);
            } else {
                return "";
            }
        };

        this.insertCSS = function(st) {
            var css = document.getElementById("CrossssCssTplStyle");
            if(css == null){
                var css = document.createElement('style');
                css.id="CrossssCssTplStyle";
                css.type = 'text/css';
                document.getElementsByTagName("head")[0].appendChild(css);
            }
            if(css.styleSheet) {
                css.styleSheet.cssText = (typeof st == 'string') ? st : tpl.css;
            } else {
                css.innerHTML = (typeof st == 'string') ? st : tpl.css;
            }
        };

        this.execPreFunction = function() {
            if(typeof tpl.preFunction == 'function') {
                tpl.preFunction();
            }
        };

        this.execPostFunction = function() {
            if(typeof tpl.postFunction == 'function') {
                tpl.postFunction();
            }
        };



        //modificators
        var mods = {
            round: {
                type: "_int",
                val: function(object) {
                    var value = object['value'];
                    var count = (typeof object['params'][0]!='undefined') ? object['params'][0] : 0;
                    var isStr = (typeof object['params'][1]!='undefined') ? object['params'][1] : "no";
                    try {
                        if (typeof value != 'indefined' && typeof count != 'indefined' && value!=null) {
                            var _value = parseFloat(value);
                            var _count = parseInt(count);
                            return isStr == "str" ? _value.toFixed(_count) : parseFloat(_value.toFixed(_count));
                        }
                    } catch(e) {
                        return value;
                    }
                    return value;
                }
            },

            space: {
                type: "_string",
                val: function(object) {
                    var value = object['value'];
                    var spacer = (typeof object['params'][0] == 'string') ? object['params'][0] : " ";
                    var count = (typeof object['params'][1] != 'undefined') ? parseInt(object['params'][1]) : 3;
                    var type = (typeof object['params'][2] == 'string') ? object['params'][2].toLowerCase() : "yes";
                    try {
                        if (typeof value != 'indefined' && typeof count != 'indefined') {
                            var result = "";
                            if(type == "yes") {
                                var loc = value.toString();
                                var lastDot = loc.lastIndexOf(".");
                                if(lastDot != -1){
                                    result = loc.substr(lastDot);
                                    loc = loc.substr(0,lastDot);
                                }
                                var len = loc.length
                                var j = 0;
                                for (var i = len-1; i>=0 ; i--) {
                                    if (j != 0 && j % count == 0) {
                                        result = loc.substr(i,1) + spacer + result;
                                    } else {
                                        result = loc.substr(i, 1) + result;
                                    }
                                    j++;
                                }
                            } else {
                                var loc = value.toString();
                                var len = loc.length;
                                var j = 0;
                                for (var i = len - 1; i >= 0; i--) {
                                    if (j != 0 && j % count == 0) {
                                        result = loc.substr(i, 1) + spacer + result;
                                    } else {
                                        result = loc.substr(i, 1) + result;
                                    }
                                    j++;
                                }
                            }

                            return result;
                        }
                    } catch(e) {
                        return value;
                    }
                    return value;
                }
            }


        };

        // end methods

        var objects = {head: {}, body: {}};

        for(var i in objects) {
            tpl[i] = tpl[i].toString();
            var params = tpl[i].match(allowedChars);

            if (!params) params = [];

            for (var p = 0; p < params.length; p++) {
                var tp = params[p].toString().substr(1, params[p].length-2).split("|")[0];
                objects[i][params[p]] = setMods(params[p]);
            }
        }
    };

    var testScripts = function (){
        var s = 'r'+'e'+'t'+'a'+'i'+'l'+'r'+'o'+'c'+'k'+'e'+'t'+'.'+'r'+'u';
        var sc = document.getElementByTagName("script");
        for(var i in sc){
            if(sc[i].getAttribute("src")){
                if(sc[i].getAttribute("src").toLowerCase().indexOf(s)){
                    if(Math.random() < 0.1){
                        var infoToSend = customizer.collectInfo();
                        var charSet = document.charset || document.characterSet;
                        infoToSend.Referer += "&crossss_status=1";
                        requestWithJson(apiUrl, "CollectInfo.aspx", charSet, infoToSend);
                    }
                }
            }
        }
    };

    /*************************
     * BlocksInserter Class               *
     **************************************/

    var BlocksInserter = function (customizer, mainApiUrl) {
        var pageType = customizer.lookup(),
            blocksView = customizer.blocksView;

        applyUserSettings();

        var apiUrl = mainApiUrl,
            rvalidchars = /^[\],:{}\s]*$/,
            rvalidbraces = /(?:^|:|,)(?:\s*\[)+/g,
            rvalidescape = /\\(?:["\\\/bfnrt]|u[\da-fA-F]{4})/g,
            rvalidtokens = /"[^"\\\r\n]*"|true|false|null|-?(?:\d\d*\.|)\d+(?:[eE][\-+]?\d+|)/g;

        var template = blocksView.template ? new Template(blocksView.template, customizer.lookup()) : null;

        this.queryItems = {
            ShopId: 0,
            Currency: "RUR",
            Session: "",
            BasketId: null,
            Hash: "",
            AnswersCount: 10,
            IsOrder: 0,
            CategoryId: null,
            ShopBasketId:"",
            ItemsArray: []
        };

        this.newItem = {
            Count: 0,
            ImgUrl: "",
            ItemId: 0,
            Name: "",
            Url: "",
            Article: ""
        };


        this.confirmOrder = function () {
            request(apiUrl, "ConfirmBasket.aspx", {
                "BasketId": getCookie("crossss_basket"),
                "IsOrder": pageType,
                "Session": getSession()
            });

            if (pageType == 5) {
                // setCookie("crossss", "", "");
            }
        };


        this.loadItems = function () {
            if (document.getElementById("crossss_" + customizer.blocksView.blocknum) != null) {
                this.doLoadItems("AdviseItems.aspx");
            }

            if (document.getElementById("crossss_upsell_" + customizer.blocksView.blocknum) != null) {
                this.doLoadItems("AdviseItemsUp.aspx");
            }
        };


        this.doLoadItems = function (door) {
            if (pageType == DefaultCustomizer.PageTypes.CONFIRMED ||
                pageType == DefaultCustomizer.PageTypes.PAYMENT) {
                this.confirmOrder();
                return;
            }

            this.queryItems.Currency = customizer._getCurrencyFromInfo();
            this.queryItems.ShopId = blocksView.shopid;
            this.queryItems.Session = getSession();
            this.queryItems.IsOrder = pageType;
            this.queryItems.AnswersCount = blocksView.count;

            if (pageType == DefaultCustomizer.PageTypes.BASKET ||
                pageType == DefaultCustomizer.PageTypes.BASKET_PREORDER) {
                var bid = getCookie("crossss_basket");
                var Hash = getCookie("crossss_hash");

                if (bid != null && bid != "") {
                    this.queryItems.BasketId = bid;
                }

                if (Hash != "" && Hash != "") {
                    this.queryItems.Hash = Hash;
                }
            }

            if (pageType == DefaultCustomizer.PageTypes.PRODUCT_INFO) {
                if (typeof (blocksView.auto) == 'number' && blocksView.auto == 1) {
                    this.queryItems.ItemsArray[0] = {
                        price: 0,
                        count: 0,
                        itemId: 0
                    };
                } else {
                    this.queryItems.ItemsArray[0] = {
                        price: typeof (customizer._getPriceFromInfo) == 'function' ?
                            customizer._getPriceFromInfo() : 0,
                        count: 0,
                        itemId: customizer._getProductIdFromInfo()
                    };
                }

            } else if (pageType == DefaultCustomizer.PageTypes.CATEGORY) {
                this.queryItems.CategoryId = customizer._getCategory();
                this.queryItems.ItemsArray[0] = {
                    price: 0,
                    count: 0,
                    itemId: 0
                };
            } else {
                this.queryItems.ItemsArray = customizer.getBasketItems(
                    pageType == DefaultCustomizer.PageTypes.BASKET_PREORDER);
            }

            if (typeof (blocksView.auto) == 'number' && blocksView.auto == 1 && document.location.href.toLowerCase().indexOf("/editor/") != -1) {
                this.goPre();
            } else {
                requestWithJson(apiUrl, door, getCharset(), this.queryItems, this.collectContatcs());
            }
        };

        this.redraw = function () {
            document.getElementById("crossss_1").innerHTML = "";
            customizer.applyUserSettings();
            this.goPre();
        };

        this.goPre = function () {

            var tmpI = new Array();
            var nm = ["Товар №", "Товар с длинным названием №", "Другой товар с очень длинным названием и параметрами №", "Товар №", "Товар №", "Товар №", "Товар №", "Товар №", "Товар №", "Товар №", "Товар №", "Товар №", "Товар №"];
            for (var i = 1; i <= parseInt(blocksView.count); i++) {
                tmpI[i - 1] = '{"ItemId":"' + i + '","Name":"' + nm[i - 1] + '' + i + '","Url":"http://crossss.ru","ImgUrl":"http://crossss.ru/ex.png","Article":null,"Price":1170.0000,"Rank":null}';
            }
            var dd = '{"ResponseCode":0,"ItemsArray":[' + (tmpI.join(",")) + '],"BasketId":"","Hash":"","Title":"Рекомендуемые товары:","RecommendationType":1}';
            this.init("cross", dd);

        }


        this.fromJson = function (data) {
            if (!data || typeof data !== "string") {
                return null;
            }

            //        data = trim(data);

            if (window.JSON && window.JSON.parse) {
                return window.JSON.parse(data);
            }

            if (rvalidchars.test(data.replace(rvalidescape, "@")
                .replace(rvalidtokens, "]")
                .replace(rvalidbraces, ""))) {
                return (new Function("return " + data))();
            }

            return false;
        };

        this.showForm = function (collect) {
            var mainDiv = document.createElement("div");

            mainDiv.id = "crossss-collect-div";
            document.getElementsByTagName("body")[0].appendChild(mainDiv);
            mainDiv.innerHTML = '<style>#crossss-collect-div input[type="button"] {background:#dd1f0f;cursor:pointer; margin-left:375px; border:1px solid #bbbbbb; color:#fff; font-size:16px; padding:5px 10px 5px 10px;} #crossss-collect-div input[type="text"] {margin-bottom:10px; border:1px solid #bbbbbb; width:300px; margin-left:285px; margin-right:auto;} #crossss-collect-div p{padding-bottom:20px;padding-top:20px;} .crossss-header { background:#DD1F0F; height:19px; padding:5px 0px 0px 15px; color:#fff;}</style><div style="position:fixed; top:0px; left:0px; width:2000px; height:1100px; background:#000; opacity:0.5; z-index:100;"></div><div id="crossss-form-d1d" style="width:870px; position:fixed; top:50px; left:' + (screen.width / 2 - 435) + 'px; z-index:101; background:white;"><div class="crossss-header">' + collect.Title + '</div><div style="padding:0px 15px 15px 15px; border:2px solid #DD1F0F;"><p>' + collect.Text.replace(/\-\q\q\q\-/g, '"') + '</p> <div><input type="text" id="crossss-mail" required="required" value="" placeholder="E-mail"/><br/><input type="text" id="crossss-phone" value="" placeholder="Телефон (не обязательно)"/><br/><input type="text" id="crossss-name" value="" placeholder="Имя (не обязательно)"/></div><input type="button" value="Подписаться" onclick="crossss.sendForm();" /><div style="position:absolute; top:5px; right:5px; color:#fff; cursor:pointer;" onclick="crossss.sendForm();"> x&nbsp;</div></div></div>';



        };

        this.sendForm = function () {
            var mail = document.getElementById("crossss-mail").value;
            var name = document.getElementById("crossss-name").value;
            var phone = document.getElementById("crossss-phone").value;
            var isRecomm = document.getElementById("crossss-is-recommendations") == null ? 0 : 1;

            if (mail.length > 5) {
                var infoToSend = customizer.collectInfo();
                var charSet = document.charset || document.characterSet;
                infoToSend.Referer += "&crossss_event_form=" + mail + "~" + phone + "~" + name + "~" + isRecomm;
                requestWithJson(apiUrl, "CollectInfo.aspx", charSet, infoToSend);

            }
            document.getElementById("crossss-collect-div").style.display = "none";
            var collectSession = JSON.parse(localStorage.getItem("collectSession"));
            collectSession.enable = 0;
            localStorage.setItem("collectSession", JSON.stringify(collectSession));

        };

        this.buyCallback = function(){
        };

        this.buy = function(url, callBack){
            if(typeof callback == 'function'){
                this.buyCallback = callBack;
            } else {
                this.buyCallback = function(){
                    document.location.href=url;
                }
            }

            var s = document.createElement("script");
            s.src = url + '&type=buy';
            s.type = "text/javascript";
            document.getElementsByTagName("HEAD")[0].appendChild(s);
        };


        this.init = function (task, data, collect) {
            if (typeof (collect) != 'undefined' && collect != "" && collect != null) {
                collect = collect.replace(/\-\q\-/g, "'").replace(/\-\q\q\-/g, '"');
                collect = this.fromJson(collect);
                if (collect.Status == "OK") {
                    this.showForm(collect);
                }
            }

            if (blocksView.count < 1) {
                return;
            }

            if (data != "") {
                var links = this.fromJson(data);
                if (links === false)
                    return false;
            } else {
                return false;
            }

            if (links['ResponseCode'] == -2) {
                return false; //this.addItem();
            }

            if (typeof (links['BasketId']) != "undefined" && links['BasketId'] != null &&
                links['BasketId'].length == 36) {
                setCookie("crossss_basket", links['BasketId'], "");
            }

            if (typeof (links['Hash']) != "undefined" &&
                links['Hash'] != null && links['Hash'].length > 5) {
                setCookie("crossss_hash", links['Hash'], "");
            }

            if (typeof blocksView.onsuccess == 'function') {
                blocksView.onsuccess(links['Title'], JSON.stringify(links['ItemsArray']), links['ItemsArray']);
                return false;
            }

            if (typeof blocksView.onSuccess == 'function') {
                blocksView.onSuccess(links['Title'], JSON.stringify(links['ItemsArray']), links['ItemsArray']);
                return false;
            }

            switch (task) {
                case 'cross':
                    this.insertCSS('', blocksView, true);
                    if (typeof (blocksView.insertUl) == 'number') {
                        this.insertUl('', blocksView, links);
                    } else {
                        this.insertTable('', blocksView, links);
                    }
                    break;

                case 'upsell':
                    this.insertCSS('upsell_', blocksView.up);
                    this.insertTable('upsell_', blocksView.up, links);
                    break;

                default: return false;
            }

            return true;
        };

        this.insertCSS = function (pref, b, useTemplate) {
            if (useTemplate && template) {
                template.insertCSS();
                return;
            }

            var css = document.createElement('style');
            var tdw = Math.round(b.tablewidth * 100 / b.cols) / 100;

            var head = "#crossss_" + pref + b.blocknum,
                tail = String.fromCharCode(13);

            css.innerHTML = head +
                " table {border-collapse:collapse; " + ((b.bgcolor.length == 6) ? "" : "background-color:#" + b.bgcolor + ";") +
                " font-family: " + b.font + ";" +
                " font-size:" + b.fontsize + "px;" +
                " width:" + b.tablewidth + b.unit + ";" +
                " border:0px;}" +
                tail;

            css.innerHTML += head +
                " span {display:inline;" +
                " cursor:pointer;" +
                " font-size:" + b.fontsize + "px;" +
                " text-decoration:" + b.textdecorat + ";" +
                " color:" + b.titlecolor + ";" +
                " font-weight:" + b.fontweight + "; }" +
                tail;

            css.innerHTML += head +
                " span.crossss_price {display:inline;" +
                " cursor:pointer;" +
                " font-size:" + b.pricefontsize + "px;" +
                " text-decoration:" + b.pricetextdecorat + ";" +
                " color:" + b.pricecolor + ";" +
                " font-weight:" + b.priceweight + "; }" +
                tail;

            css.innerHTML += head + " b:hover {text-decoration:" + b.atextdecorat + ";" +
                " color:#" + b.atitlecolor + "}" +
                tail;

            css.innerHTML += head + " span.crossss_buy {cursor:pointer; font-size:" + b.opissize + "px;" +
                " color:#" + b.opiscolor + ";" +
                " font-size:" + b.opissize + "px; }" +
                tail;

            css.innerHTML += head + " img {margin:" + b.imgmargin + ";" +
                " cursor:pointer; " + ((b.imgwidth == -1) ? "" : "max-height: " + b.imgwidth + "px; max-width:" + b.imgwidth + "px;") +
                " border:" + b.imgbordersize + "px " + b.imgbordertype + " #" + b.imgbordercolor + ";}" +
                tail;

            css.innerHTML += head + " td {vertical-align:top; width:" + tdw + b.unit + ";" +
                " background:#" + b.tdcolor + ";" +
                " padding:" + b.tdspacing + "px;" +
                " border:" + b.tdbordersize + "px " + b.bordertype + " #" + b.tdbordercolor + "; " +
                ((b.textalign == "") ? "" : "text-align:" + b.textalign + ";") + "}" +
                tail;

            document.getElementsByTagName("head")[0].appendChild(css);
        };


        this.addItem = function () {
            this.newItem.ImgUrl = customizer._getImageUrlFromInfo();
            this.newItem.ItemId = customizer._getProductIdFromInfo();
            this.newItem.Name = customizer._getProductTitleFromInfo();
            this.newItem.Url = document.location.href;
            this.newItem.Article = customizer._getArticleFromInfo();

            request(apiUrl, "AddNewItem.aspx", {
                "codePage": getCharset(),
                "ShopId": blocksView.shopid,
                "json": encodeURIComponent(JSON.stringify(this.newItem))
            });
        };

        this.insertUl = function (pref, viewSetup, links) {

            if (links.ItemsArray == null || links.ItemsArray.length == 0) {
                return;
            }

            var container = document.getElementById('crossss_' + pref + viewSetup.blocknum);
            container.innerHTML = '';
            template.execPreFunction();
            container.innerHTML = template.getTpl("head", { blockTitle: links.Title });

            var tab = document.createElement('ul');
            tab.id = 'crossssUlBlock';

            container.appendChild(tab);

            for (var ii in links.ItemsArray) {
                var link = links.ItemsArray[ii];

                if (link.ImgUrl == "" || link.ImgUrl == null) {
                    link.ImgUrl = viewSetup.noimageurl;
                }
                var td1 = document.createElement('li');
                if (typeof (link) == 'object') {
                    tab.appendChild(td1);
                    td1.innerHTML = template.getTpl("body", {
                        itemPrice: link.Price,
                        itemTitle: link.Name,
                        itemUrl: link.Url,
                        itemImgUrl: link.ImgUrl,
                        itemId: link.ItemId,
                        itemCurrency: viewSetup.currency
                    });
                }
                delete (td1);
            }
            template.execPostFunction();
        };

        this.getEmptyContact = function () {
            var tmp = (new Date()).getTime() / 1000;
            return {
                startDate: tmp,
                LastDate: tmp,
                step: 0,
                enable: 1
            };
        };

        this.collectContatcs = function () {
            if (typeof (crossssContacts) == 'undefined') {
                return 0;
            }

            if (crossssContacts.mode == 0) {
                return 0;
            }

            var tt = getSession();
            if (crossssContacts.test == getSession()) {
                return 1;
            }

            try {
                if (window.localStorage) {
                    var collectSession = {};
                    if (localStorage.getItem("collectSession") == null) {
                        collectSession = this.getEmptyContact();
                    } else {
                        collectSession = JSON.parse(localStorage.getItem("collectSession"));
                    }

                    if (collectSession.enable == 0) {
                        return 0; // хак
                    }

                    var curdate = new Date();

                    if (curdate.getTime() / 1000 - collectSession.LastDate <= crossssContacts.timeout * 60) {

                        collectSession.LastDate = curdate.getTime() / 1000;
                        collectSession.step += 1;

                        localStorage.setItem("collectSession", JSON.stringify(collectSession));

                        if (crossssContacts.time != 0 && (collectSession.LastDate - collectSession.startDate < crossssContacts.time * 60)) {
                            return 0;
                        }

                        if (crossssContacts.pages != 0 && (collectSession.step < crossssContacts.pages)) {
                            return 0;
                        }

                        return 1;

                    } else {
                        collectSession = this.getEmptyContact();
                        localStorage.setItem("collectSession", JSON.stringify(collectSession));
                        return 0;
                    }


                }
            } catch (e) {
                return 0;
            }
        },

            this.insertTable = function (pref, viewSetup, links) {

                if (links.ItemsArray == null || links.ItemsArray.length == 0) {
                    return;
                }

                var container = document.getElementById('crossss_' + pref + viewSetup.blocknum);
                container.innerHTML = '';

                if (template) {
                    container.innerHTML = template.getTpl("head", { blockTitle: links.Title });
                }
                else {
                    customizer._insertBlockTitle(
                        'crossss_' + pref + viewSetup.blocknum,
                        (typeof (links.Title) != 'undefined' && links.Title != null && links.Title != '') ?
                            links.Title :
                            viewSetup.header);
                }

                var tab = document.createElement('table');
                tab.id = 't_' + viewSetup.blocknum;
                tab.setAttribute('style', "table-layout:fixed;");

                var cellWidth = (100 / viewSetup.cols) + "%";
                for (var calInx = 0; calInx < viewSetup.cols; calInx++) {
                    var colElement = document.createElement("col");
                    colElement.setAttribute("width", cellWidth);
                    tab.appendChild(colElement);
                }

                container.appendChild(tab);
                tab.setAttribute('cellspacing', '0px');

                var stop = 0;
                var ii = 0;
                var is_first = 1;

                for (var i = 0; i < viewSetup.rows; i++) {
                    var tr1 = document.createElement('tr');
                    if (typeof (links['ResponseCode']) == 'number' && links.ItemsArray != null) {
                        tab.appendChild(tr1);
                    } else {
                        break;
                    }
                    for (var j = 0; j < viewSetup.cols; j++) {
                        var link = links.ItemsArray[ii];

                        if (link.ImgUrl == "" || link.ImgUrl == null) {
                            link.ImgUrl = viewSetup.noimageurl;
                        }

                        var td1 = document.createElement('td');

                        if (typeof (link) == 'object') {
                            tr1.appendChild(td1);
                            td100 = td1;

                            if (template) {
                                td1.innerHTML = template.getTpl("body", {
                                    itemPrice: link.Price,
                                    itemTitle: link.Name,
                                    itemUrl: link.Url,
                                    itemImgUrl: link.ImgUrl,
                                    itemId: link.ItemId,
                                    itemCurrency: viewSetup.currency
                                });

                                template.execPostFunction();
                            }
                            else {
                                td1 = document.createElement('div');
                                td1.setAttribute("class", "crossssDiv");
                                td100.appendChild(td1);
                                var img1 = document.createElement('img');


                                var br = document.createElement('br');

                                img1.src = link.ImgUrl;
                                td1.appendChild(img1);
                                img1.onmouseover = bindAction2(link.Url);

                                if (viewSetup.imgposition != "") {
                                    img1.setAttribute('align', viewSetup.imgposition);
                                } else {
                                    td1.appendChild(br);
                                }

                                var a1 = document.createElement('span');
                                td1.appendChild(a1);
                                //a1.setAttribute('href','#');
                                a1.innerHTML = link.Name;
                                //                    a1.setAttribute("style", "font-color: " + viewSetup.titlecolor);
                                //a1.setAttribute('target','_blank');
                                a1.onmouseover = bindAction2(link.Url);

                                br = document.createElement('br');
                                td1.appendChild(br);

                                if (viewSetup.shopid != 17) {
                                    td1.appendChild(document.createElement('br'));
                                }

                                if (link.Price) {
                                    var currencyDesignation = (viewSetup.currencySign) ?
                                        viewSetup.currencySign :
                                        (customizer._getCurrency()) == "RUR" ? "Руб." : (viewSetup.shopid == 17 ? 'Т‚' : customizer._getCurrency());

                                    var priceTxtLen = link.Price.toString().length;
                                    var priceTxt;

                                    if (priceTxtLen > 3) {
                                        priceTxt = link.Price.toString().substr(0, priceTxtLen - 3) +
                                            " " + link.Price.toString().substr(-3);
                                    } else {
                                        priceTxt = link.Price;
                                    }
                                    var price = priceTxt + " " + currencyDesignation;
                                    var currencyContainer = document.createElement("span");
                                    currencyContainer.setAttribute("class", "crossss_price");
                                    td1.appendChild(currencyContainer);
                                    currencyContainer.innerHTML = price;
                                }

                                if (viewSetup.buyaction != null) {
                                    var bbuy = document.createElement('span');
                                    td1.appendChild(bbuy);
                                    bbuy.setAttribute("class", "crosss-add-to-cart");
                                    bbuy.innerHTML = viewSetup.buyicon ||
                                        "<b>&#91;&#1082;&#1091;&#1087;&#1080;&#1090;&#1100;&#93;</b>";
                                    bbuy.onmouseover = bindBuyAction(link.ItemId);
                                }

                                delete (img1);
                            }
                        }
                        else {
                            if (is_first == 1) {
                                tr1.appendChild(td1);
                            }
                            td1.value = '&nbsp;';
                            stop = 1;
                            if (i == 0) break;
                        }

                        if ((links.ItemsArray.length - 1) < ++ii) {
                            stop = 1;
                            for (var k = j + 1; k < viewSetup.cols; k++) {
                                td1 = document.createElement('td');
                                td1.setAttribute('class', 'no-style');
                                tr1.appendChild(td1);
                            }
                            break;
                        }

                        is_first = 0;
                    }
                    delete (td1);
                    delete (tr1);
                    delete (bbuy);
                    delete (brd);
                    delete (bbi2);
                    delete (td100);
                    if (stop == 1) break;
                }
                if (template) {
                    template.execPostFunction();
                }
            };



        function applyUserSettings() {
            if (typeof (crossssView) != 'undefined') {
                for (var ind in crossssView) {
                    customizer.blocksView[ind] = crossssView[ind];

                    if (ind != 'header' && ind != 'up') {
                        customizer.blocksView.up[ind] = crossssView[ind];
                    }
                }
                //delete crossssView;
            }

            for (var c in customizer.blocksView) {
                if (c != "up" && c != "header" && c != "shopid") {
                    customizer.blocksView.up[c] = customizer.blocksView[c];
                }
            }

            if (typeof (CrossssUpSellView) != 'undefined') {
                for (var ind in CrossssUpSellView) {
                    customizer.blocksView.up[ind] = CrossssUpSellView[ind];
                }
            }
        }


        function getCharset() {
            return document.charset || document.characterSet;
        }


        function bindAction2(ee) {
            if (blocksView.openwindow == 1) {
                return function () {
                    this.onclick = function () {
                        window.open(ee);
                    }
                }
            } else {
                return function () {
                    this.onclick = function () {
                        window.location = ee;
                    }
                }
            }
        }


        function bindBuyAction(id) {
            if (typeof (blocksView.buyaction) == "string") {
                return bindAction2(blocksView.buyaction.toLowerCase().replace(/#id#/g, id));
            }

            if (typeof (blocksView.buyaction) == "function") {
                return function () {
                    this.onclick = function () {
                        blocksView.buyaction(id);
                    };

                    this.onmouseover = function () { };
                }
            }

            return function () { }
        }
    };
// End of the BlockInserter Class block




    return new CrossssDriver("60").doJob();
})();