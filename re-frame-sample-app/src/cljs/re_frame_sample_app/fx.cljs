(ns re-frame-sample-app.fx
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-fx
  ::navigate
  (fn [{:keys [handler route-params]}]
    (re-frame-sample-app.routes/navigate! handler route-params)))
