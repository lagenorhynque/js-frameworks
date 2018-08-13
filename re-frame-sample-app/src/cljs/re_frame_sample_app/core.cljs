(ns re-frame-sample-app.core
  (:require [accountant.core :as accountant]
            [cljsjs.material-ui]
            [day8.re-frame.http-fx]
            [re-frame-sample-app.config :as config]
            [re-frame-sample-app.events.core :as events]
            [re-frame-sample-app.fx]
            [re-frame-sample-app.routes :refer [match-route-with-query-params routes]]
            [re-frame-sample-app.views.core :as views]
            [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [stylefy.core :as stylefy]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (accountant/configure-navigation!
   {:nav-handler (fn [path]
                   (re-frame/dispatch [::events/set-current-route
                                       (match-route-with-query-params routes path)]))
    :path-exists? (fn [path]
                    (boolean (match-route-with-query-params routes path)))})
  (accountant/dispatch-current!)
  (stylefy/init)
  (dev-setup)
  (mount-root))
