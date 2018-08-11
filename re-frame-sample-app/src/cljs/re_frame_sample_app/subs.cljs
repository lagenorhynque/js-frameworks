(ns re-frame-sample-app.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::current-route
  (fn [db _]
    (:route db)))

(re-frame/reg-sub
  ::channels
  (fn [db _]
    (:channels db)))

(re-frame/reg-sub
  ::channel-detail
  (fn [db _]
    (get-in db [:channel :detail])))

(re-frame/reg-sub
  ::channel-messages
  (fn [db _]
    (get-in db [:channel :messages])))
