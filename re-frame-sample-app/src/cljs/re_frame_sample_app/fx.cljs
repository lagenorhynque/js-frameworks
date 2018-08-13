(ns re-frame-sample-app.fx
  (:require [re-frame-sample-app.routes :as routes]
            [re-frame-sample-app.views.core :as views]
            [re-frame.core :as re-frame]))

(re-frame/reg-fx
  :init-view
  (fn [route]
    (views/init route)))

(re-frame/reg-fx
  :navigate
  (fn [{:keys [handler route-params]}]
    (routes/navigate! handler route-params)))

(re-frame/reg-fx
  :api-callback
  (fn [{:keys [fn args]}]
    (apply fn args)))
