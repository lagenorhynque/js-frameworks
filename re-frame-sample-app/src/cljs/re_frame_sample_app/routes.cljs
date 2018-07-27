(ns re-frame-sample-app.routes
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]
            [cemerick.url]
            [re-frame-sample-app.views.channel :as channel]
            [re-frame-sample-app.views.home :as home]))

(def routes
  ["/" {"" ::home/view
        ["channels/" [#"\d+" :channel-id]] ::channel/view}])

(defn match-route-with-query-params
  [route path & {:as options}]
  (let [query-params (->> (:query (cemerick.url/url path))
                          (map (fn [[k v]] [(keyword k) v]))
                          (into {}))]
    (cond-> (bidi/match-route* route path options)
      (seq query-params) (assoc :query-params query-params))))

(defn navigate
  ([handler]
   (accountant/navigate! (bidi/path-for routes handler)))
  ([handler route-params]
   (accountant/navigate! (apply bidi/path-for routes handler (flatten (seq route-params))))))
