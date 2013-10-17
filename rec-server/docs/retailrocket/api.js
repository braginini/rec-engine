var NO_JQUERY = {}; (function (window, $, undefined) { if (!("console" in window)) { var c = window.console = {}; c.log = c.warn = c.error = c.debug = function () { }; } if ($ === NO_JQUERY) { $ = { fn: {}, extend: function () { var a = arguments[0]; for (var i = 1, len = arguments.length; i < len; i++) { var b = arguments[i]; for (var prop in b) { a[prop] = b[prop]; } } return a; } }; } $.fn.pm = function () { console.log("usage: \nto send:    $.pm(options)\nto receive: $.pm.bind(type, fn, [origin])"); return this; }; $.pm = window.pm = function (options) { pm.send(options); }; $.pm.bind = window.pm.bind = function (type, fn, origin, hash, async_reply) { pm.bind(type, fn, origin, hash, async_reply === true); }; $.pm.unbind = window.pm.unbind = function (type, fn) { pm.unbind(type, fn); }; $.pm.origin = window.pm.origin = null; $.pm.poll = window.pm.poll = 200; var pm = { send: function (options) { var o = $.extend({}, pm.defaults, options), target = o.target; if (!o.target) { /*console.warn("postmessage target window required");*/ return; } if (!o.type) { /*console.warn("postmessage type required");*/ return; } var msg = { data: o.data, type: o.type }; if (o.success) { msg.callback = pm._callback(o.success); } if (o.error) { msg.errback = pm._callback(o.error); } if (("postMessage" in target) && !o.hash) { pm._bind(); target.postMessage(JSON.stringify(msg), o.origin || '*'); } else { pm.hash._bind(); pm.hash.send(o, msg); } }, bind: function (type, fn, origin, hash, async_reply) { pm._replyBind(type, fn, origin, hash, async_reply); }, _replyBind: function (type, fn, origin, hash, isCallback) { if (("postMessage" in window) && !hash) { pm._bind(); } else { pm.hash._bind(); } var l = pm.data("listeners.postmessage"); if (!l) { l = {}; pm.data("listeners.postmessage", l); } var fns = l[type]; if (!fns) { fns = []; l[type] = fns; } fns.push({ fn: fn, callback: isCallback, origin: origin || $.pm.origin }); }, unbind: function (type, fn) { var l = pm.data("listeners.postmessage"); if (l) { if (type) { if (fn) { var fns = l[type]; if (fns) { var m = []; for (var i = 0, len = fns.length; i < len; i++) { var o = fns[i]; if (o.fn !== fn) { m.push(o); } } l[type] = m; } } else { delete l[type]; } } else { for (var i in l) { delete l[i]; } } } }, data: function (k, v) { if (v === undefined) { return pm._data[k]; } pm._data[k] = v; return v; }, _data: {}, _CHARS: '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split(''), _random: function () { var r = []; for (var i = 0; i < 32; i++) { r[i] = pm._CHARS[0 | Math.random() * 32]; }; return r.join(""); }, _callback: function (fn) { var cbs = pm.data("callbacks.postmessage"); if (!cbs) { cbs = {}; pm.data("callbacks.postmessage", cbs); } var r = pm._random(); cbs[r] = fn; return r; }, _bind: function () { if (!pm.data("listening.postmessage")) { if (window.addEventListener) { window.addEventListener("message", pm._dispatch, false); } else if (window.attachEvent) { window.attachEvent("onmessage", pm._dispatch); } pm.data("listening.postmessage", 1); } }, _dispatch: function (e) { try { var msg = JSON.parse(e.data); } catch (ex) { /*console.warn("postmessage data invalid json: ", ex);*/ return; } if (!msg.type) { /*console.warn("postmessage message type required");*/ return; } var cbs = pm.data("callbacks.postmessage") || {}, cb = cbs[msg.type]; if (cb) { cb(msg.data); } else { var l = pm.data("listeners.postmessage") || {}; var fns = l[msg.type] || []; for (var i = 0, len = fns.length; i < len; i++) { var o = fns[i]; if (o.origin && o.origin !== '*' && e.origin !== o.origin) { /*console.warn("postmessage message origin mismatch", e.origin, o.origin);*/ if (msg.errback) { var error = { message: "postmessage origin mismatch", origin: [e.origin, o.origin] }; pm.send({ target: e.source, data: error, type: msg.errback }); } continue; } function sendReply(data) { if (msg.callback) { pm.send({ target: e.source, data: data, type: msg.callback }); } } try { if (o.callback) { o.fn(msg.data, sendReply, e); } else { sendReply(o.fn(msg.data, e)); } } catch (ex) { if (msg.errback) { pm.send({ target: e.source, data: ex, type: msg.errback }); } else { throw ex; } } }; } } }; pm.hash = { send: function (options, msg) { var target_window = options.target, target_url = options.url; if (!target_url) { /*console.warn("postmessage target window url is required");*/ return; } target_url = pm.hash._url(target_url); var source_window, source_url = pm.hash._url(window.location.href); if (window == target_window.parent) { source_window = "parent"; } else { try { for (var i = 0, len = parent.frames.length; i < len; i++) { var f = parent.frames[i]; if (f == window) { source_window = i; break; } }; } catch (ex) { source_window = window.name; } } if (source_window == null) { /*console.warn("postmessage windows must be direct parent/child windows and the child must be available through the parent window.frames list");*/ return; } var hashmessage = { "x-requested-with": "postmessage", source: { name: source_window, url: source_url }, postmessage: msg }; var hash_id = "#x-postmessage-id=" + pm._random(); target_window.location = target_url + hash_id + encodeURIComponent(JSON.stringify(hashmessage)); }, _regex: /^\#x\-postmessage\-id\=(\w{32})/, _regex_len: "#x-postmessage-id=".length + 32, _bind: function () { if (!pm.data("polling.postmessage")) { setInterval(function () { var hash = "" + window.location.hash, m = pm.hash._regex.exec(hash); if (m) { var id = m[1]; if (pm.hash._last !== id) { pm.hash._last = id; pm.hash._dispatch(hash.substring(pm.hash._regex_len)); } } }, $.pm.poll || 200); pm.data("polling.postmessage", 1); } }, _dispatch: function (hash) { if (!hash) { return; } try { hash = JSON.parse(decodeURIComponent(hash)); if (!(hash['x-requested-with'] === 'postmessage' && hash.source && hash.source.name != null && hash.source.url && hash.postmessage)) { return; } } catch (ex) { return; } var msg = hash.postmessage, cbs = pm.data("callbacks.postmessage") || {}, cb = cbs[msg.type]; if (cb) { cb(msg.data); } else { var source_window; if (hash.source.name === "parent") { source_window = window.parent; } else { source_window = window.frames[hash.source.name]; } var l = pm.data("listeners.postmessage") || {}; var fns = l[msg.type] || []; for (var i = 0, len = fns.length; i < len; i++) { var o = fns[i]; if (o.origin) { var origin = /https?\:\/\/[^\/]*/.exec(hash.source.url)[0]; if (o.origin !== '*' && origin !== o.origin) { /*console.warn("postmessage message origin mismatch", origin, o.origin);*/ if (msg.errback) { var error = { message: "postmessage origin mismatch", origin: [origin, o.origin] }; pm.send({ target: source_window, data: error, type: msg.errback, hash: true, url: hash.source.url }); } continue; } } function sendReply(data) { if (msg.callback) { pm.send({ target: source_window, data: data, type: msg.callback, hash: true, url: hash.source.url }); } } try { if (o.callback) { o.fn(msg.data, sendReply); } else { sendReply(o.fn(msg.data)); } } catch (ex) { if (msg.errback) { pm.send({ target: source_window, data: ex, type: msg.errback, hash: true, url: hash.source.url }); } else { throw ex; } } }; } }, _url: function (url) { return ("" + url).replace(/#.*$/, ""); } }; $.extend(pm, { defaults: { target: null, url: null, type: null, data: null, success: null, error: null, origin: "*", hash: false } }); })(this, typeof jQuery === "undefined" ? NO_JQUERY : jQuery); if (!("JSON" in window && window.JSON)) { JSON = {} } (function () { function f(n) { return n < 10 ? "0" + n : n } if (typeof Date.prototype.toJSON !== "function") { Date.prototype.toJSON = function (key) { return this.getUTCFullYear() + "-" + f(this.getUTCMonth() + 1) + "-" + f(this.getUTCDate()) + "T" + f(this.getUTCHours()) + ":" + f(this.getUTCMinutes()) + ":" + f(this.getUTCSeconds()) + "Z" }; String.prototype.toJSON = Number.prototype.toJSON = Boolean.prototype.toJSON = function (key) { return this.valueOf() } } var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, gap, indent, meta = { "\b": "\\b", "\t": "\\t", "\n": "\\n", "\f": "\\f", "\r": "\\r", '"': '\\"', "\\": "\\\\" }, rep; function quote(string) { escapable.lastIndex = 0; return escapable.test(string) ? '"' + string.replace(escapable, function (a) { var c = meta[a]; return typeof c === "string" ? c : "\\u" + ("0000" + a.charCodeAt(0).toString(16)).slice(-4) }) + '"' : '"' + string + '"' } function str(key, holder) { var i, k, v, length, mind = gap, partial, value = holder[key]; if (value && typeof value === "object" && typeof value.toJSON === "function") { value = value.toJSON(key) } if (typeof rep === "function") { value = rep.call(holder, key, value) } switch (typeof value) { case "string": return quote(value); case "number": return isFinite(value) ? String(value) : "null"; case "boolean": case "null": return String(value); case "object": if (!value) { return "null" } gap += indent; partial = []; if (Object.prototype.toString.apply(value) === "[object Array]") { length = value.length; for (i = 0; i < length; i += 1) { partial[i] = str(i, value) || "null" } v = partial.length === 0 ? "[]" : gap ? "[\n" + gap + partial.join(",\n" + gap) + "\n" + mind + "]" : "[" + partial.join(",") + "]"; gap = mind; return v } if (rep && typeof rep === "object") { length = rep.length; for (i = 0; i < length; i += 1) { k = rep[i]; if (typeof k === "string") { v = str(k, value); if (v) { partial.push(quote(k) + (gap ? ": " : ":") + v) } } } } else { for (k in value) { if (Object.hasOwnProperty.call(value, k)) { v = str(k, value); if (v) { partial.push(quote(k) + (gap ? ": " : ":") + v) } } } } v = partial.length === 0 ? "{}" : gap ? "{\n" + gap + partial.join(",\n" + gap) + "\n" + mind + "}" : "{" + partial.join(",") + "}"; gap = mind; return v } } if (typeof JSON.stringify !== "function") { JSON.stringify = function (value, replacer, space) { var i; gap = ""; indent = ""; if (typeof space === "number") { for (i = 0; i < space; i += 1) { indent += " " } } else { if (typeof space === "string") { indent = space } } rep = replacer; if (replacer && typeof replacer !== "function" && (typeof replacer !== "object" || typeof replacer.length !== "number")) { throw new Error("JSON.stringify") } return str("", { "": value }) } } if (typeof JSON.parse !== "function") { JSON.parse = function (text, reviver) { var j; function walk(holder, key) { var k, v, value = holder[key]; if (value && typeof value === "object") { for (k in value) { if (Object.hasOwnProperty.call(value, k)) { v = walk(value, k); if (v !== undefined) { value[k] = v } else { delete value[k] } } } } return reviver.call(holder, key, value) } cx.lastIndex = 0; if (cx.test(text)) { text = text.replace(cx, function (a) { return "\\u" + ("0000" + a.charCodeAt(0).toString(16)).slice(-4) }) } if (/^[\],:{}\s]*$/.test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, "@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, "]").replace(/(?:^|:|,)(?:\s*\[)+/g, ""))) { j = eval("(" + text + ")"); return typeof reviver === "function" ? walk({ "": j }, "") : j } throw new SyntaxError("JSON.parse") } } }());
function rrClientAddToBasket(itemId) { }

var rcApi = rrApi = (function ($) {
    if (!$) return null;

    var apiParams = null;
    var console = window["console"] || { log: function () { } };
    var testParams = { segment: "showAll" };

    function setCookie(cName, value, exdays, path) { var exdate = new Date(); exdate.setDate(exdate.getDate() + exdays); var cValue = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toUTCString()) + ";path=" + path || "/"; document.cookie = cName + "=" + cValue; };
    function getCookie(cName) { var i, x, y, arRcookies = document.cookie.split(";"); for (i = 0; i < arRcookies.length; i++) { x = arRcookies[i].substr(0, arRcookies[i].indexOf("=")); y = arRcookies[i].substr(arRcookies[i].indexOf("=") + 1); x = x.replace(/^\s+|\s+$/g, ""); if (x == cName) { return unescape(y); } } return null; };

    //зарос partnerApiParams
    (function (d, partnerId) {
        var paramsJs, paramsJsId = 'rrApiParam';

        if (d.getElementById(paramsJsId)) return;

        var cookieRcuid = getCookie("rcuid");

        var ref = d.getElementsByTagName('script')[0];
        paramsJs = d.createElement('script');
        paramsJs.id = paramsJsId;
        paramsJs.async = true;
        paramsJs.type = "text/javascript";
        paramsJs.src = "http://retailrocket.ru/api/PartnerApiParams/" + partnerId + "/jsonp?callback=rrApi._initialize&rcuid" + cookieRcuid;
        ref.parentNode.insertBefore(paramsJs, ref);
    }(document, window["rrPartnerId"]));

    function eventSendRequest(method, sid, productId, referrer) {
        var uri = "http://retailrocket.ru/api/Event/" + method + "/" + apiParams.PartnerId + "/" + productId + "/";

        uri += "?rcuid=" + apiParams.UserId;

        if (referrer)
            uri += "&referrer=" + encodeURIComponent(referrer);

        uri += "&" + (new Date).getTime();

        var newJs = document.createElement('script');
        newJs.type = 'text/javascript';
        newJs.src = uri;

        if (window["rrDebug"]) console.log(uri);

        document.getElementsByTagName('head')[0].appendChild(newJs);
    }

    function hidePopupWidget() {
        $("#rrFade").hide();
        $("#rrPopupWidgetPopUp").hide();
        $("#rrPopupWidgetFrame").attr("src", "about:blank");
        $("#rrPopupWidgetFrame").hide();
    }

    function showPopupWidget(itemId) {
        if (typeof window.disablePopupWidget != "undefined")
            return;

        if (testParams.segment != "showAll")
            return;

        if (!apiParams || !apiParams.PopupWidgetId || !inArray(apiParams.ActiveWidgetIds, apiParams.PopupWidgetId))
            return;

        var exceptedItemsId = getExceptedItemsIdFromCookie();

        $("#rrPopupWidgetFrame").attr("src", "http://retailrocket.ru/Frontend/PopupWidget?widgetId=" + apiParams.PopupWidgetId + "&itemId=" + itemId + "&exceptItems=" + exceptedItemsId.join(","));

        setTimeout(function () {
            $("#rrFade").css("display", "block");
            $("#rrPopupWidgetPopUp").show();
        }, 100);
    }

    function initPopupWidget() {
        var fadeHtml = "<div id='rrFade' style='display:none;position:fixed; top:0; bottom:0; right:0; left:0; z-index:9999; background-color:black; opacity:0.4; filter:alpha(opacity=50);'></div>";
        var frameId = "rrPopupWidgetFrame";

        var popupHtml = "" +
            "<div " +
            "id='rrPopupWidgetPopUp' " +
            "style='" +
            "display: none; " +
            "background: #fff; " +
            "padding: 17px 19px 10px 20px; " +
            "border: 10px solid #ddd; " +
            "float: left; " +
            "font-size: 1.2em; " +
            "position: fixed; " +
            "top: 50%; left: 50%; " +
            "z-index: 99999; " +
            "-webkit-box-shadow: 0px 0px 20px #000; " +
            "-moz-box-shadow: 0px 0px 20px #000; " +
            "box-shadow: 0px 0px 20px #000; " +
            "-webkit-border-radius: 10px; " +
            "-moz-border-radius: 10px; " +
            "margin-left: -350px; " +
            "margin-top: -340px; " +
            "border-radius: 10px;" +
            "width: 690px;" +
            "height: 640px;" +
            "'" +
            ">" +
            '<a href="javascript: void(0)" class="close">' +
            '<img id="rrPopupWidgetCloseBtn" src="http://retailrocket.ru/Content/img/close_pop.png" class="btn_close" title="Close Window" style="float: right; margin: -39px -38px 0 0;" alt="Close" />' +
            '</a>' +
            "<img src='http://retailrocket.ru/Content/img/load.gif' style='position:absolute;left:50%;top:50%;margin-left:-33px;margin-top:-33px;' />" +
            "<iframe " +
            "id='" + frameId + "' " +
            "name='" + frameId + "'" +
            "frameborder='0' " +
            "scrolling='no'" +
            "style='border:0px;width:690px;height:640px;overflow:hidden;position:absolute;display:none;'>" +
            "</iframe>" +
            "</div>";

        $('body').
            append($(fadeHtml)).
            append($(popupHtml));

        $("#rrFade").click(hidePopupWidget);
        $("#rrPopupWidgetCloseBtn").click(hidePopupWidget);
        var iframe = $("#" + frameId);

        iframe.load(function () {
            pm.bind(frameId, function (data) {
                if (!data || !data.count)
                    hidePopupWidget();
                else
                    iframe.show();
            });

            pm({
                target: window.frames[frameId],
                type: "register",
                data: { frameId: frameId },
                url: iframe[0].contentWindow ? iframe[0].contentWindow.location : ""
            });

        });
    }

    function inArray(array, val) {
        for (var i = 0; i < array.length; i++)
            if (array[i] === val)
                return true;
        return false;
    }

    function initCardAndCategoryWidget() {
        var widgets = $(".rr-widget");

        var exceptedItemsId = getExceptedItemsIdFromCookie();

        for (var i = 0; i < widgets.length; ++i) {
            var widget = $(widgets[i]);

            var productId = null, categoryId = null, productsId = null;

            if (typeof window.disableCardWidget == "undefined")
                productId = widget.data("rr-widget-product-id") || widget.attr("data-rr-widget-product-id");

            if (typeof window.disableCategoryWidget == "undefined")
                categoryId = widget.data("rr-widget-category-id") || widget.attr("data-rr-widget-category-id");

            if (typeof window.disableCartWidget == "undefined")
                productsId = widget.data("rr-widget-products-id") || widget.attr("data-rr-widget-products-id");

            var widgetId = widget.data("rr-widget-id") || widget.attr("data-rr-widget-id");
            var widgetType = widget.data("rr-widget-type") || widget.attr("data-rr-widget-type");
            var widgetWidth = widget.data("rr-widget-width") || widget.attr("data-rr-widget-width");

            var exceptItems = exceptedItemsId.join(",") + (widget.data("except-items") || widget.attr("data-except-items") || "");
            var includeItems = widget.data("include-items") || widget.attr("data-include-items");

            if ((!productId && !categoryId && !productsId && widgetType != "search") || !widgetId)
                continue;

            if (!inArray(apiParams.ActiveWidgetIds, widgetId))
                continue;

            var iframe = $('<iframe frameborder="0" scrolling="no"/>');
            var iframeId = "rc-iframe-" + productId + "-" + i;

            iframe.attr("name", iframeId).attr("id", iframeId);

            iframe.css("border", "0px");
            iframe.css("width", widgetWidth);
            iframe.css("height", "365px");
            iframe.css("overflow", "hidden");

            if (testParams.segment != "showAll")
                iframe.css("height", "0px");

            iframe.load(function () {
                pm.bind(iframeId, function (data) {
                    if (!data || !data.count) iframe.hide();
                });

                pm({
                    target: window.frames[iframeId],
                    type: "register",
                    data: { frameId: iframeId },
                    url: iframe[0].contentWindow ? iframe[0].contentWindow.location : ""
                });

            });
            var src = "";
            if (widgetType == "search") {
                src = 'http://retailrocket.ru/Frontend/SearchWidget?widgetId=' + widgetId + '&referer=' + document.referrer + '&pageurl=' + window.location;
            }
            if (widgetType == "cart") {
                src = 'http://retailrocket.ru/Frontend/CartWidget?widgetId=' + widgetId + '&itemId=' + productsId;
            }
            if (productId) {
                src = 'http://retailrocket.ru/Frontend/CardWidget?widgetId=' + widgetId + '&itemId=' + productId;
            }
            else if (categoryId == 0) {
                src = 'http://retailrocket.ru/Frontend/MainPageWidget?widgetId=' + widgetId + '&categoryId=' + categoryId;
            }
            else if (categoryId) {
                src = 'http://retailrocket.ru/Frontend/CategoryWidget?widgetId=' + widgetId + '&categoryId=' + categoryId;
            }
            src += "&exceptItems=" + (exceptItems || "") + "&includeItems=" + (includeItems || "");
            iframe.attr("src", src);
            widget.append(iframe);
        }
    }

    function initWidgets() {
        initPopupWidget();
        initCardAndCategoryWidget();

        pm.bind("widgetHeight", function (data) {

            $("#" + data.frameId).height(data.height);

            if (!data.height || testParams.segment != "showAll") {
                $("#" + data.frameId).height(0);
                (window["_gaq"] || []).push(['_trackEvent', 'RetailRocketTest', "RecsMissing", 'A/B Test', 0, true]);
            }
        });

        pm.bind("addToBasket", function (data) {
            var itemId = data.itemId;
            rcApi.recomAddToBasket(itemId);
        });

        pm.bind("linkTo", function (data) {
            window.location.href = data.href;
        });

        pm.bind("closePopupRecomWidget", function () {
            hidePopupWidget();
        });
    }

    function addToCookieExceptItem(itemId, expireIn) {
        var key = "rr-" + apiParams.PartnerId + "-" + itemId.join("-");
        setCookie(key, "except", expireIn, "/");
    }

    function getExceptedItemsIdFromCookie() {
        var cookies = document.cookie.split(";");
        var exceptItemsId = [];

        var regExp = new RegExp("\\s*rr-" + apiParams.PartnerId + "-(\\d*)=except");
        for (var i = 0; i < cookies.length; ++i) {
            var match = regExp.exec(cookies[i]);
            if (match && match.length == 2)
                exceptItemsId.push(match[1]);
        }
        return exceptItemsId;
    }

    return new (function () {
        this.view = function (itemId) {
            if (!apiParams) return true;

            eventSendRequest("View", apiParams.PartnerId, itemId);
        };

        this.buy = function (itemsId) {
            if (!apiParams) return true;

            eventSendRequest("Buy", apiParams.PartnerId, itemsId.join(','));
        };

        this.addToBasket = function (itemId, dontShowPopupWidget) {
            if (!apiParams) return true;

            addToCookieExceptItem([itemId], 1);

            eventSendRequest("AddToBasket", apiParams.PartnerId, itemId);
            if (!dontShowPopupWidget)
                showPopupWidget(itemId);
            return true;
        };

        this.recomAddToBasket = function (itemId, dontShowPopupWidget) {
            if (!apiParams) return true;

            rrClientAddToBasket(itemId);
            addToCookieExceptItem([itemId], 1);

            eventSendRequest("RecomAddToBasket", apiParams.PartnerId, itemId);
            if (!dontShowPopupWidget)
                showPopupWidget(itemId);
        };

        this.view0 = function (itemId) { eventSendRequest("View0", apiParams.PartnerId, itemId); };
        this.view10 = function (itemId) { eventSendRequest("View10", apiParams.PartnerId, itemId); };
        this.view30 = function (itemId) { eventSendRequest("View30", apiParams.PartnerId, itemId); };

        this.pageView = function () {
            if (!apiParams) return true;

            eventSendRequest("PageView", apiParams.PartnerId, 0, document.referrer);
        };

        this.order = function (json) {
            if (!apiParams) return true;
            var itemsId = [];
            for (var i = 0; i < json.items.length; ++i) {
                eventSendRequest("Order", apiParams.PartnerId, json.items[i].id, null);
                itemsId.push(json.items[i].id);
            }

            addToCookieExceptItem(itemsId, 365);
        };

        this.setCookie = setCookie;
        this.getCookie = getCookie;

        this._initialize = function (params) {
            apiParams = params;

            if (params.UserId && params.TestEnabled) {
                var userId = params.UserId;
                var flag = (parseInt(userId[userId.length - 1], 16) % 2) == 1;
                testParams.segment = flag ? "showAll" : "hideAll";
            }

            var self = this;
            $(function () {
                var gaq = window["_gaq"] || [];

                if (params.TestEnabled) {
                    gaq.push(['_trackEvent', 'RetailRocketTest', testParams.segment == "showAll" ? 'RecsShown' : "RecsHidden", 'A/B Test', 0, true]);
                    console.log(testParams.segment);
                }

                initWidgets();
                self.setCookie("rcuid", apiParams.UserId, 365, "/");
                self.pageView();

                try {
                    var script = "rrClientAddToBasket = (function(itemId) {";
                    script += apiParams.AddToBasketClientJsCode || "";
                    script += "})";
                    eval(script);
                } catch (exc) {
                    console.log(exc);
                }

                $(function () {
                    if (typeof window.rcAsyncInit == "function")
                        window.rcAsyncInit();

                    if (typeof window.rrAsyncInit == "function")
                        window.rrAsyncInit();
                });
            });
        };

    });
}(typeof jQuery === "undefined" ? null : jQuery));


(function () {
    if (rrApi && typeof window.rrJsParams !== "undefined")
        rrApi._initialize(window.rrJsParams);
})();