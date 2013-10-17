var recApi = (function () {
    /*if (!$) return null;*/

    var apiParams = null;

    (function (apiKey, doc) {
        var jsApiParams, jsApiParamsId = 'recApiParam';

        if (doc.getElementById(jsApiParamsId)) return;

        var ref = doc.getElementsByTagName('script')[0];
        jsApiParams = doc.createElement('script');
        jsApiParams.id = jsApiParamsId;
        jsApiParams.async = true;
        jsApiParams.type = "text/javascript";
        jsApiParams.src = "http://localhost:8080/auth/assign/" + apiKey + "/";
        ref.parentNode.insertBefore(jsApiParams, ref);
    }(window["partnerApiKey"], document));

    this.init = function init(parameters) {
         apiParams = parameters;
         recApi.pageView();
    }

    this.pageView = function () {
        if (!apiParams) return true;

        sendEvent("pageview", 0, document.referrer);
    };

    function sendEvent(method, itemId, referrer) {
        var uri = "http://localhost:8080/event/" + method + "/" + apiParams.apiKey + "/" + itemId + "/";


        uri += "&ts" + (new Date).getTime();

        var result = document.createElement('script');
        result.src = uri;
    }

}());

function setCookie(cName, value, expir, path) {
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + expir);
    var cValue = escape(value) + ((expir == null) ? "" : "; expires=" + exdate.toUTCString()) + ";path=" + path || "/";
    document.cookie = cName + "=" + cValue;
};

function getCookie(cName) {
    var i, x, y, cookies = document.cookie.split(";");
    for (i = 0; i < cookies.length; i++) {
        x = cookies[i].substr(0, cookies[i].indexOf("="));
        y = cookies[i].substr(cookies[i].indexOf("=") + 1);
        x = x.replace(/^\s+|\s+$/g, "");
        if (x == cName) {
            return unescape(y);
        }
    }
    return null;
};

(function () {
    if (recApi && typeof window.jsApiParams !== "undefined")
        recApi.init(window.jsApiParams);
})();
