(ns re-frame-sample-app.events
  (:require [ajax.core :as ajax]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
            [re-frame-sample-app.db :as db]
            [re-frame-sample-app.fx :as fx]
            [re-frame.core :as re-frame]))

(def base-url "http://localhost:8080/api")

(re-frame/reg-event-db
  ::initialize-db
  (fn-traced [_ _]
    db/default-db))

(re-frame/reg-event-db
  ::set-current-route
  (fn [db [_ route]]
    (assoc db :route route)))

(re-frame/reg-event-fx
  ::navigate
  (fn [_ [_ handler route-params]]
    {::fx/navigate {:handler handler
                    :route-params route-params}}))

(re-frame/reg-event-fx
  ::fetch-channels
  (fn [_ _]
    {:http-xhrio {:method :get
                  :uri (str base-url "/channels")
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success [::set-channels]}}))

(re-frame/reg-event-db
  ::set-channels
  (fn [db [_ response]]
    (assoc db :channels (:data response))))
