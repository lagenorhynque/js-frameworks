(ns re-frame-sample-app.events.core
  (:require [re-frame-sample-app.db :as db]
            [re-frame-sample-app.events.channel-list :as events.channel-list]
            [re-frame.core :as re-frame]))

(re-frame/reg-event-fx
  ::initialize-db
  (fn [{:keys [db]} _]
    {:db db/default-db
     :dispatch [::events.channel-list/fetch-channels]}))

(re-frame/reg-event-fx
  ::set-current-route
  (fn [{:keys [db]} [_ route]]
    {:db (assoc db :route route)
     :init-view route}))

(re-frame/reg-event-fx
  ::navigate
  (fn [_ [_ handler route-params]]
    {:navigate {:handler handler
                :route-params route-params}}))
