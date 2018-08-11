(ns re-frame-sample-app.fx
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-fx
  ::init-view
  (fn [route]
    (re-frame-sample-app.views.core/init route)))

(re-frame/reg-fx
  ::navigate
  (fn [{:keys [handler route-params]}]
    (re-frame-sample-app.routes/navigate! handler route-params)))

(re-frame/reg-fx
  ::api-callback
  (fn [{:keys [fn args]}]
    (apply fn args)))
