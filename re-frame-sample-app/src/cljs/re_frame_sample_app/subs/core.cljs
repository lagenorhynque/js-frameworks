(ns re-frame-sample-app.subs.core
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::current-route
  (fn [db _]
    (:route db)))
