(ns re-frame-sample-app.subs.channel-list
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::channels
  (fn [db _]
    (:channels db)))
