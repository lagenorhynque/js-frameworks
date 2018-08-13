(ns re-frame-sample-app.subs.channel
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::detail
  (fn [db _]
    (get-in db [:channel :detail])))

(re-frame/reg-sub
  ::messages
  (fn [db _]
    (get-in db [:channel :messages])))
